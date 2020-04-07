package com.payconiq.stocks.archival.model.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class ArchivalStockConverter {

    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    private final ObjectMapper objectMapper;

    public ArchivalStockConverter(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    public Map<String, Object> convertPayload(final ArchivalStockRecord record)   {
        return objectMapper.readValue(record.getPayload(), MAP_TYPE_REFERENCE);
    }

}
