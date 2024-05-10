package com.ssafy.userservice.controller;

import com.ssafy.userservice.dto.MemberDto;
import com.ssafy.userservice.dto.MemberResponseDto;
import com.ssafy.userservice.security.annotation.AuthID;
import com.ssafy.userservice.service.AuthService;
import com.ssafy.userservice.service.MemberService;
import com.ssafy.userservice.vo.Greeting;
import com.ssafy.userservice.vo.RequestMember;
import com.ssafy.userservice.vo.RequestMemberTimeZone;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;
    private final AuthService authService;
    private final Greeting greeting;


    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request, HttpServletResponse response) {
        return greeting.getMessage();
    }

    /**
     * 내 정보 수정
     * @param id
     * @param requestMember
     * @return
     */
    @PatchMapping("/me")
    public ResponseEntity<MemberDto> updateMember(
        @AuthID Integer id,
        @RequestBody RequestMember requestMember) {
        log.debug("id= {}", id);
        MemberDto memberDto = memberService.updateMember(id, requestMember);
        return ResponseEntity.ok(memberDto);
    }
    /**
     * 회원 타임존 정보 수정
     * @param id
     * @param zoneId
     * @return
     */
    @PutMapping("/my/timezone")
    public ResponseEntity<MemberDto> updateMemberTimeZone(
        @AuthID Integer id,
        @RequestBody RequestMemberTimeZone requestMemberTimeZone) {
        MemberDto memberDto = memberService.changeTimeZone(id, requestMemberTimeZone);
        return ResponseEntity.ok(memberDto);
    }

    /**
     * 전체 유저 정보 조회
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> memberDtoList= authService.getAllMember();
        return ResponseEntity.ok(memberDtoList);
    }
    /**
     * 유저 상세 조회
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getOneMember(@PathVariable("id") Integer id) {
        MemberResponseDto memberResponseDto = authService.getAuthById(id);
        return ResponseEntity.ok(memberResponseDto);
    }

}
