package com.ssafy.userservice.vo;

import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMember {
    private String email;
    private String name;
    private String memberId;

}
