package com.mihaela.orderplatform.kafka;

import com.mihaela.orderplatform.domain.PublishedOrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, PublishedOrderEvent> kafkaTemplate;

    @Value("${app.kafka.topics}")
    private String topic;

    public void publishOrderEvent(PublishedOrderEvent event) {
        kafkaTemplate.send(topic, event.orderId().toString(), event);
    }
}
