package com.ssafy.userservice.vo;

import lombok.Data;


@Data
public class RequestMember {

    private Integer profile;
    private String department;
    private String region;
    private String zoneId;
}
