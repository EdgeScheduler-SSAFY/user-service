package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.MemberDto;
import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.vo.RequestMember;
import com.ssafy.userservice.vo.RequestMemberTimeZone;

public interface MemberService {

    void createMember(Auth auth, String email);

    MemberDto updateMember(Integer id, RequestMember requestMember);

    MemberDto changeTimeZone(Integer id, RequestMemberTimeZone memberTimeZone);

}
