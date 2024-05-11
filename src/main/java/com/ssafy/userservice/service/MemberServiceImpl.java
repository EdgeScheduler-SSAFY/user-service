package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.ChangeTimeZoneMessage;
import com.ssafy.userservice.dto.MemberDto;
import com.ssafy.userservice.entity.Auth;
import com.ssafy.userservice.entity.Member;
import com.ssafy.userservice.repository.MemberRepository;
import com.ssafy.userservice.vo.RequestMember;
import com.ssafy.userservice.vo.RequestMemberTimeZone;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final KafkaProducer kafkaProducer;

    @Value("${KAFKA_TOPIC}")
    private String topic;

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
    public void createMember(Auth auth, String email) {
        Member member = Member.builder()
            .auth(auth)
            .email(email)
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
        if (requestMember.getRegion() != null && requestMember.getZoneId() != null) {
            member.changeRegion(requestMember.getRegion());
            member.changeZoneId(requestMember.getZoneId());
            kafkaProducer.send(topic, ChangeTimeZoneMessage.builder()
                .memberId(id)
                .zoneId(requestMember.getZoneId())
                .build());
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
        kafkaProducer.send(topic, ChangeTimeZoneMessage.builder()
            .memberId(id)
            .zoneId(memberTimeZone.getZoneId())
            .build());
        memberRepository.save(member);
        return MemberDto.getMember(member);
    }

}
