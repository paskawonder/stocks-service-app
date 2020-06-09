package com.payconiq.stocks.archival.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stocks.archival.service.ArchivalStockService;
import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ArchivalMessageListener {

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    private static final String ID = "id";

    private static final String VALIDATION_ERROR_PATTERN = "VALIDATION ERROR; KEY:{} VALUE:{}";

    private final ArchivalStockService archivalStockService;

    private final ObjectMapper objectMapper;

    public ArchivalMessageListener(final ArchivalStockService archivalStockService, final ObjectMapper objectMapper) {
        this.archivalStockService = archivalStockService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "stock-archival-data")
    public void listen(final ConsumerRecord<String, String> consumerRecord, final Acknowledgment acknowledgment) {
        Map<String, Object> payload;
        try {
            payload = objectMapper.readValue(consumerRecord.value(), MAP_TYPE_REFERENCE);
        } catch (final IllegalArgumentException | JsonProcessingException e) {
            payload = Collections.emptyMap();
        }
        final String key = consumerRecord.key();
        long stockId;
        try {
            stockId = ((Number) payload.get(ID)).longValue();
        } catch (final Exception e) {
            stockId = -1;
        }
        if (StringUtils.isNotBlank(key) && key.length() < 256 && stockId >= 0) {
            archivalStockService.add(key, stockId, payload);
        } else {
            log.error(VALIDATION_ERROR_PATTERN, key, consumerRecord.value());
        }
        acknowledgment.acknowledge();
    }

}
