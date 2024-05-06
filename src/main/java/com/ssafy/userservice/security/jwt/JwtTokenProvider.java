package com.ssafy.userservice.security.jwt;

import com.ssafy.userservice.dto.AuthDto;
import com.ssafy.userservice.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "role";

    private final Key key;

    public JwtTokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtProperties.SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // accessToken, refreshToken을 생성
    public JwtTokenDto generateToken(Authentication authentication, AuthDto authDto) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date accessTokenExpirationTime = new Date(now + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);
        String accessToken = Jwts.builder()
            .setSubject(String.valueOf(authDto.getId())) // authId 담기
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpirationTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return new JwtTokenDto(authDto.getId(), accessToken, refreshToken);
    }

    public JwtTokenDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        long now = (new Date()).getTime();
        Date accessTokenExpirationTime = new Date(now + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);
        String accessToken = Jwts.builder()
            .setSubject(String.valueOf(((CustomUserDetails) authentication.getPrincipal()).getAuth()
                .getId())) // 사용자 userId
            .claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpirationTime)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        String refreshToken = Jwts.builder()
            .setExpiration(new Date(now + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

        return JwtTokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    // 토큰으로부터 정보 추출
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 토큰으로부터 추출한 정보를 기반으로 AuthenticationToken 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        Collection<? extends GrantedAuthority> authorities =
            Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.debug("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty.", e);
        }
        return false;
    }

    public String resolveToken(String accessToken) {
        log.debug("accessToken = {}", accessToken);
        if (accessToken != null && accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            return accessToken.substring(7);
        }
        throw new UnsupportedJwtException("지원하지 않는 토큰 형식입니다.");
    }

    public Integer extractAuthId(String accessToken) {
        return Integer.valueOf(parseClaims(resolveToken(accessToken)).getSubject());
    }

    public long getTokenExpiration(String accessToken) {
        String token = resolveToken(accessToken);
        Claims claims = parseClaims(token);
        long tokenExpirationTime = claims.getExpiration().getTime();
        long remainingTime = tokenExpirationTime - System.currentTimeMillis();
        return remainingTime > 0 ? remainingTime : 0;
    }
}
