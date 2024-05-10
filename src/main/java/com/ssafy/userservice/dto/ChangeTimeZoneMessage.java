package com.ssafy.userservice.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChangeTimeZoneMessage {

    private Integer memberId;
    private String zoneId;
}
