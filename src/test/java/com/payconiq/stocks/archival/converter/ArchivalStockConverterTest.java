package com.payconiq.stocks.archival.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stocks.archival.model.converter.ArchivalStockConverter;
import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ArchivalStockConverterTest {

    private static final String KEY = "key";

    private static final String PAYLOAD = "payload";

    private final ObjectMapper objectMapper;

    private final ArchivalStockConverter archivalStockConverter;

    public ArchivalStockConverterTest() {
        objectMapper = Mockito.mock(ObjectMapper.class);
        archivalStockConverter = new ArchivalStockConverter(objectMapper);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void convertPayloadTest() throws JsonProcessingException {
        final Map<String, Object> expected = Mockito.mock(Map.class);
        Mockito.when(objectMapper.readValue(Mockito.eq(PAYLOAD), Mockito.any(TypeReference.class))).thenReturn(expected);
        final ArchivalStockRecord e = new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY, 1), PAYLOAD);
        Assertions.assertEquals(expected, archivalStockConverter.convertPayload(e));
    }

}
