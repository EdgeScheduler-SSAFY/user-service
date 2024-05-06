package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.MemberDto;
import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.entity.Member;
import com.ssafy.userservice.repository.MemberRepository;
import com.ssafy.userservice.vo.RequestMember;
import jakarta.ws.rs.NotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public MemberDto createMember(MemberDto memberDto) {
//        memberDto.setMemberId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Member member = mapper.map(memberDto, Member.class);

        memberRepository.save(member);

        MemberDto returnUserDto = mapper.map(member, MemberDto.class);

        return returnUserDto;
    }

    @Override
    public MemberDto creteMember(Auth auth) {
        Member member = Member.builder()
            .auth(auth)
            .build();
        auth.setMember(member);
        return MemberDto.getMember(member);
    }

    @Override
    public MemberDto updateMember(Integer id, RequestMember requestMember) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundException::new);

        if (requestMember.getDepartment() != null) {
            member.changeDepartment(requestMember.getDepartment());
        }
        if (requestMember.getZoneId() != null) {
            member.changeZoneId(requestMember.getZoneId());
        }
        if (requestMember.getEmail() != null) {
            member.changeEmail(requestMember.getEmail());
        }
        member.changeProfile(requestMember.getProfile());
        memberRepository.save(member);
        return MemberDto.getMember(member);
    }
}
