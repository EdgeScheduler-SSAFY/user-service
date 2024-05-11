package com.ssafy.userservice.service;

import com.ssafy.userservice.dto.ChangeTimeZoneMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, ChangeTimeZoneMessage> kafkaTemplate;

    public void send(String topic, ChangeTimeZoneMessage message) {
        log.info("message: memberId = {} | zoneId = {}", message.getMemberId(), message.getZoneId());
        kafkaTemplate.send(topic, message);
    }
}
