package com.payconiq.stocks.archival.service;

import com.payconiq.stocks.archival.model.ArchivalMessage;
import com.payconiq.stocks.archival.model.converter.ArchivalStockConverter;
import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import com.payconiq.stocks.archival.repository.ArchivalStockRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;

public class ArchivalStockServiceTest {

    private static final String KEY = "key";

    private static final String PAYLOAD = "payload";

    private final ArchivalStockRepository archivalStockRepository;

    private final ArchivalStockConverter archivalStockConverter;

    private final ArchivalStockService archivalStockService;

    public ArchivalStockServiceTest() {
        archivalStockRepository = Mockito.mock(ArchivalStockRepository.class);
        archivalStockConverter = Mockito.mock(ArchivalStockConverter.class);
        archivalStockService = new ArchivalStockService(archivalStockRepository, archivalStockConverter);
    }

    @Test
    public void addTest() {
        final ArchivalStockRecord e = new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY, 1), PAYLOAD);
        Mockito.doNothing().when(archivalStockRepository).persist(e);
        archivalStockService.add(KEY, new ArchivalMessage(1L, PAYLOAD));
        Mockito.verify(archivalStockRepository, Mockito.times(1)).persist(e);
    }

    @Test
    public void getAllByStockIdTest() {
        final ArchivalStockRecord e = new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY, 1), PAYLOAD);
        Mockito.when(archivalStockRepository.getAllByStockId(1, 1, 2)).thenReturn(List.of(e));
        @SuppressWarnings("unchecked")
        final Map<String, Object> expected = Mockito.mock(Map.class);
        Mockito.when(archivalStockConverter.convertPayload(e)).thenReturn(expected);
        final Flux<Map<String, Object>> actual = archivalStockService.getAllByStockId(1, 1, 2);
        Assertions.assertEquals(List.of(expected), actual.collectList().block());
        Mockito.verify(archivalStockRepository, Mockito.times(1)).getAllByStockId(1, 1, 2);
    }

}
