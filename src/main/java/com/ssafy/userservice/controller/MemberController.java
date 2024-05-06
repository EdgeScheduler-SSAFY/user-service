package com.ssafy.userservice.controller;

import com.ssafy.userservice.dto.MemberDto;
import com.ssafy.userservice.service.MemberService;
import com.ssafy.userservice.vo.Greeting;
import com.ssafy.userservice.vo.RequestMember;
import com.ssafy.userservice.vo.ResponseMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);
    private final Environment env;
    private final MemberService memberService;

//    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private Greeting greeting;


    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request, HttpServletResponse response) {
        return greeting.getMessage();
    }

    @PatchMapping("/me")
    public ResponseEntity<MemberDto> updateMember(
        @RequestHeader(name = "Authorization", required = true) Integer id,
        @RequestBody RequestMember requestMember) {
        log.info("id= {}", id);
        MemberDto memberDto = memberService.updateMember(id, requestMember);
        return ResponseEntity.ok(memberDto);
    }

//    @PostMapping("/users")
//    public ResponseEntity<ResponseMember> createUser(@RequestBody RequestMember requestMember) {
//        ModelMapper mapper = new ModelMapper();
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//
//        MemberDto memberDto = mapper.map(requestMember, MemberDto.class);
//        memberService.createMember(memberDto);
//
//        ResponseMember responseUser = mapper.map(memberDto, ResponseMember.class);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
//    }

}
