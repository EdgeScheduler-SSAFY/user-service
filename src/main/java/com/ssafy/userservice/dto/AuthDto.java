package com.ssafy.userservice.dto;

import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.entity.Member;
import com.ssafy.userservice.entity.Role;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AuthDto {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String provider;
    private String providerId;
    private Role role = Role.USER;
    private String refreshToken;
    private LocalDate lastLoginDate;
    private Member member;

    public static AuthDto getAuth(Auth auth) {
        return AuthDto.builder()
            .id(auth.getId())
            .username(auth.getUsername())
            .password(auth.getPassword())
            .name(auth.getName())
            .lastLoginDate(auth.getLastLoginDate())
            .provider(auth.getProvider())
            .providerId(auth.getProviderId())
            .role(auth.getRole())
            .refreshToken(auth.getRefreshToken())
            .member(auth.getMember())
            .build();

    }
}
