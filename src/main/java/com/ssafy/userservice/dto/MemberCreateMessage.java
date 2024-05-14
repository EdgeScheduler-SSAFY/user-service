package com.ssafy.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateMessage {

    private Integer memberId;
    private String email;
    private String zoneId;
}
