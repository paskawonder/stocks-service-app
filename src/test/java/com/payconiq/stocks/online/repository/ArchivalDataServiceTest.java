package com.payconiq.stocks.online.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stocks.online.model.entity.Stock;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

public class ArchivalDataServiceTest {

    private static final String TOPIC = "topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    private final ArchivalDataService archivalDataService;

    @SuppressWarnings("unchecked")
    public ArchivalDataServiceTest() {
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);
        objectMapper = new ObjectMapper();
        this.archivalDataService = new ArchivalDataService(TOPIC, kafkaTemplate, objectMapper);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void produceTest() {
        archivalDataService.produce(new Stock(1, 0, "", BigDecimal.ZERO, OffsetDateTime.now()));
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(Mockito.any(ProducerRecord.class));
    }

}
