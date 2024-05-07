package com.ssafy.userservice.controller;


import com.ssafy.userservice.dto.MemberResponseDto;
import com.ssafy.userservice.security.jwt.JwtProperties;
import com.ssafy.userservice.security.jwt.JwtTokenDto;
import com.ssafy.userservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 회원 id로 회원 정보 조회하는 API
     *
     * @param id : 회원 식별 Primary key
     * @return
     */
    @GetMapping("/me")
    public ResponseEntity<MemberResponseDto> getAuth(
            @RequestHeader(name = "Authorization", required = true) Integer id) {
        log.debug("id:{}", id);
        MemberResponseDto memberResponseDto = authService.getAuthById(id);
        return ResponseEntity.ok(memberResponseDto);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<?> reissue(@RequestHeader(name = "Authorization", required = true) String accessToken,
                                     @RequestHeader(name = JwtProperties.REFRESH_TOKEN, required = true) String refreshToken) {
        JwtTokenDto reissuedToken = authService.reissueToken(accessToken, refreshToken);
        return ResponseEntity.ok(reissuedToken.responseDto());
    }
}
