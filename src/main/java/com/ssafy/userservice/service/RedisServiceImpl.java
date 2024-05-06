package com.ssafy.userservice.service;

import com.ssafy.userservice.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> refreshTokenRedisTemplate;
    private final RedisTemplate<String, String> verificationCodeRedisTemplate;
    private final RedisTemplate<String, String> jwtBlackListRedisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final String RT_PREFIX = "rt:{";
    private final String VC_PREFIX = "vc:{";
    private final String SUFFIX = "}";
    private final String BL_PREFIX = "bl:{";
    private final Long RT_EXPIREATION_TIME = 60 * 24L; // 1day
    private final Long VC_EXPIREATION_TIME = 10L; // 10분

    public void saveRefreshToken(Integer authId, String refreshToken) {
        log.info("authId = {}", authId);
        log.info("refreshToken = {}", refreshToken);
        refreshTokenRedisTemplate.opsForValue()
            .set(RT_PREFIX + authId + SUFFIX, refreshToken, RT_EXPIREATION_TIME,
                TimeUnit.MINUTES);
    }

    public String getRefreshToken(Integer authId) {
        return refreshTokenRedisTemplate.opsForValue().get(RT_PREFIX + authId + SUFFIX);
    }

    public void deleteRefreshToken(Integer userId) {
        refreshTokenRedisTemplate.delete(RT_PREFIX + userId + SUFFIX);
    }

    public void saveVerificationCode(String email, String verificationCode) {
        verificationCodeRedisTemplate.opsForValue()
            .set(VC_PREFIX + email + SUFFIX, verificationCode, VC_EXPIREATION_TIME,
                TimeUnit.MINUTES);
    }

    public boolean hasRefreshToken(Integer userId) {
        return Boolean.TRUE.equals(refreshTokenRedisTemplate.hasKey(RT_PREFIX + userId + SUFFIX));
    }

    public String getVerificationCode(String email) {
        return verificationCodeRedisTemplate.opsForValue().get(VC_PREFIX + email + SUFFIX);
    }

    public void deleteVerificationCode(String email) {
        verificationCodeRedisTemplate.delete(VC_PREFIX + email + SUFFIX);
    }

    public void saveJwtBlackList(String accessToken) {
        long expirationTime = jwtTokenProvider.getTokenExpiration(accessToken);
        jwtBlackListRedisTemplate.opsForValue()
            .set(BL_PREFIX + jwtTokenProvider.resolveToken(accessToken) + SUFFIX, "logout",
                expirationTime,
                TimeUnit.MILLISECONDS);
    }

    public boolean hasJwtBlackList(String token) {
        return Boolean.TRUE.equals(jwtBlackListRedisTemplate.hasKey(BL_PREFIX + token + SUFFIX));
    }
}