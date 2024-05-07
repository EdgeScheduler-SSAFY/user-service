package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.AuthDto;
import com.ssafy.userservice.dto.MemberResponseDto;

import java.util.List;

public interface AuthService {
    AuthDto getAuthByUsername(String username);

    MemberResponseDto getAuthById(int id);

    List<MemberResponseDto> getAllMember();

}
