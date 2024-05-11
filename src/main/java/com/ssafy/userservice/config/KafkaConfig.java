package com.ssafy.userservice.config;

import com.ssafy.userservice.dto.ChangeTimeZoneMessage;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public ProducerFactory<String, ChangeTimeZoneMessage> producerFactory(KafkaProperties properties) {
        return new DefaultKafkaProducerFactory<>(properties.buildProducerProperties(null));
    }

    @Bean
    public KafkaTemplate<String, ChangeTimeZoneMessage> kafkaTemplate(KafkaProperties properties) {
        return new KafkaTemplate<>(producerFactory(properties));
    }
}
