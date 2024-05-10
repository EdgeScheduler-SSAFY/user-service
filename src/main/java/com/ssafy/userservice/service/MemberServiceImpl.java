package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.MemberDto;
import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.entity.Member;
import com.ssafy.userservice.repository.MemberRepository;
import com.ssafy.userservice.vo.RequestMember;
import com.ssafy.userservice.vo.RequestMemberTimeZone;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

//    @Override
//    public MemberDto createMember(MemberDto memberDto) {
////        memberDto.setMemberId(UUID.randomUUID().toString());
//        ModelMapper mapper = new ModelMapper();
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        Member member = mapper.map(memberDto, Member.class);
//
//        memberRepository.save(member);
//
//        MemberDto returnUserDto = mapper.map(member, MemberDto.class);
//
//        return returnUserDto;
//    }

    @Override
    @Transactional
    public void createMember(Auth auth) {
        Member member = Member.builder()
            .auth(auth)
            .build();
        auth.setMember(member);
    }

    @Override
    @Transactional
    public MemberDto updateMember(Integer id, RequestMember requestMember) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);

        if (requestMember.getDepartment() != null) {
            member.changeDepartment(requestMember.getDepartment());
        }
        if (requestMember.getRegion() != null) {
            member.changeRegion(requestMember.getRegion());
        }
        if (requestMember.getZoneId() != null) {
            member.changeZoneId(requestMember.getZoneId());
        }
        member.changeProfile(requestMember.getProfile());
        memberRepository.save(member);
        return MemberDto.getMember(member);
    }

    @Override
    @Transactional
    public MemberDto changeTimeZone(Integer id, RequestMemberTimeZone memberTimeZone) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);
        member.changeRegion(memberTimeZone.getRegion());
        member.changeZoneId(memberTimeZone.getZoneId());
        memberRepository.save(member);
        return MemberDto.getMember(member);
    }

}
