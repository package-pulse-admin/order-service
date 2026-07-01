package com.mihaela.orderplatform.config.kafka;

import com.mihaela.orderplatform.domain.PublishedOrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, PublishedOrderEvent> kafkaTemplate(
            ProducerFactory<String, PublishedOrderEvent> factory) {
        return new KafkaTemplate<>(factory);
    }
}
