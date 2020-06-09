package com.payconiq.stocks.online.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stocks.online.model.entity.Stock;
import java.util.UUID;
import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArchivalDataService {

    private final String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public ArchivalDataService(@Value("${kafka.topic}") final String topic,
                               final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public void produce(final Stock s) {
        kafkaTemplate.send(new ProducerRecord<>(topic, UUID.randomUUID().toString(), objectMapper.writeValueAsString(s)));
    }

}
