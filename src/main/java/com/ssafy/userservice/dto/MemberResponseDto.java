package com.ssafy.userservice.dto;

import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.entity.Member;
import com.ssafy.userservice.entity.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberResponseDto {

    private Integer id;
    private String name;
    private Role role;
    private String email;
    private String department;
    private String zoneId;

    public static MemberResponseDto getMemberResponse(Auth auth) {
        return MemberResponseDto.builder()
            .id(auth.getId())
            .name(auth.getName())
            .role(auth.getRole())
            .email(auth.getMember().getEmail())
            .department(auth.getMember().getDepartment())
            .zoneId(auth.getMember().getZoneId())
            .build();
    }

}
