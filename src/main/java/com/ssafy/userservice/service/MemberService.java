package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.MemberDto;
import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.vo.RequestMember;

public interface MemberService {

    MemberDto createMember(MemberDto memberDto);

    MemberDto creteMember(Auth auth);

    MemberDto updateMember(Integer id, RequestMember requestMember);
}
