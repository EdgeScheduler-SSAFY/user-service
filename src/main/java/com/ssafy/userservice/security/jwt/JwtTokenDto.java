package com.ssafy.userservice.security.jwt;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class JwtTokenDto {

    private Integer authId;
    private String accessToken;
    private String refreshToken;


    public Map<String, Object> responseDto() {
        Map<String, Object> result = new HashMap<>();
        result.put(JwtProperties.TOKEN_TYPE, JwtProperties.TOKEN_PREFIX.substring(0, 6));
        result.put(JwtProperties.ACCESS_TOKEN, this.getAccessToken());
        result.put(JwtProperties.REFRESH_TOKEN, this.getRefreshToken());
        result.put(JwtProperties.EXPRIES_IN, JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);
        return result;
    }

}
