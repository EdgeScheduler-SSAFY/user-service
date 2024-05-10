package com.ssafy.userservice.dto;

import com.ssafy.userservice.entity.Member;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberDto {

    private Integer id;
    private String email;
    private String department;
    private String region;
    private String zoneId;

    public static MemberDto getMember(Member member) {
        return MemberDto.builder()
            .id(member.getId())
            .email(member.getEmail())
            .department(member.getDepartment())
            .region(member.getRegion())
            .zoneId(member.getZoneId())
            .build();
    }

}
