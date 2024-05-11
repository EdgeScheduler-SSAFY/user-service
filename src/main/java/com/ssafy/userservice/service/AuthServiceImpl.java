package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.AuthDto;
import com.ssafy.userservice.dto.MemberResponseDto;
import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.entity.Member;
import com.ssafy.userservice.exception.RefreshTokenException;
import com.ssafy.userservice.repository.AuthRepository;
import com.ssafy.userservice.security.jwt.JwtTokenDto;
import com.ssafy.userservice.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AuthRepository authRepository;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthDto getAuthByUsername(String username) {
        Auth auth = authRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "Can't find user with this username. -> " + username));
        return AuthDto.getAuth(auth);
    }

    @Override
    public MemberResponseDto getAuthById(int id) {
        Auth auth = authRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Can't find auth with this id. -> " + id));
        return MemberResponseDto.getMemberResponse(auth);
    }

    @Override
    public List<MemberResponseDto> getAllMember() {
        List<Auth> authList = authRepository.findAll();
        return authList.stream().map(MemberResponseDto::getMemberResponse)
                .toList();
    }

    @Override
    @Transactional
    public JwtTokenDto reissueToken(String accessToken, String refreshToken) {
        Authentication authentication = jwtTokenProvider.getAuthentication(
                jwtTokenProvider.resolveToken(accessToken));

        Integer principal = Integer.valueOf(authentication.getName());
        String refreshTokenInDB = redisService.getRefreshToken(principal);

        log.debug("Auth Id = {}", principal);
        Auth auth = authRepository.findById(principal).orElseThrow(
                () -> new NotFoundException("Can't find auth with this id. -> " + principal));

        if (refreshTokenInDB == null) { // Redis에 RT 없을 경우
            log.debug("Refresh Token is not in Redis.");
            refreshTokenInDB = auth.getRefreshToken();
            if (refreshTokenInDB == null) { // MySQL에 RT 없을 경우
                log.debug("Refresh Token is not in MySQL.");
                throw new RefreshTokenException("refresh token 값이 존재하지 않습니다.");
            }
        }
        log.info("Refresh Token in DB = {}", refreshTokenInDB);

        //토큰 꺼내기
        refreshToken = refreshToken.substring(7);

        log.info("resolved refresh Token in= {}", refreshToken);

        if (!refreshTokenInDB.equals(refreshToken)) {
            redisService.deleteRefreshToken(principal);
            auth.deleteRefreshToken();
            log.info("Refresh Token is not identical.");
            throw new RefreshTokenException("Refresh Token 값이 일치하지 않습니다.");
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            redisService.deleteRefreshToken(principal);
            auth.deleteRefreshToken();
            log.debug("Refresh Token is invalidate.");
            throw new MalformedJwtException("유효하지 않은 토큰입니다.");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (redisService.getRefreshToken(principal) != null) {
            // Redis에 저장되어 있는 RT 삭제
            redisService.deleteRefreshToken(principal);
        }
        // 토큰 재발급
        AuthDto authDto = AuthDto.getAuth(auth);
        JwtTokenDto reissueTokenDto = jwtTokenProvider.generateToken(authentication, authDto);

        String reissueRefreshToken = reissueTokenDto.getRefreshToken();
        // Redis, DB 에 새로 발급 받은 RT 저장
        redisService.saveRefreshToken(principal, reissueRefreshToken);

        log.debug("Auth Id = {}", principal);
        log.debug("RefreshToken save in Redis = {}", reissueTokenDto.getRefreshToken());

        auth.setRefreshToken(reissueRefreshToken);
        authRepository.save(auth);

        return reissueTokenDto;
    }

}
