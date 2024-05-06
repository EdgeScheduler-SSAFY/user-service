package com.ssafy.userservice.service;

public interface RedisService {

    void saveRefreshToken(Integer authId, String refreshToken);
    String getRefreshToken(Integer authId);
    void deleteRefreshToken(Integer userId);
    void saveVerificationCode(String email, String verificationCode);
    boolean hasRefreshToken(Integer userId);
    String getVerificationCode(String email);
    void deleteVerificationCode(String email);
    void saveJwtBlackList(String accessToken);
    boolean hasJwtBlackList(String token);
}
