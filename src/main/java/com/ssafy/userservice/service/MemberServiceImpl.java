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

    @Value("${kafka.topic.timezone-configured}")
    private String topic;

    @Override
    @Transactional
    public void createMember(Auth auth, String email) {
        Member member = Member.builder()
            .auth(auth)
            .department("Development - EdgeScheduler")
            .email(email)
            .region("Korea")
            .zoneId("Asia/Seoul")
            .build();
        auth.setMember(member);
        kafkaProducer.send(topic, ChangeTimeZoneMessage.builder()
            .memberId(member.getId())
            .zoneId(member.getZoneId())
            .build());
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
