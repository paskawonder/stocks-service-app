package com.payconiq.stocks.archival.service;

import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import com.payconiq.stocks.archival.repository.ArchivalStockRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;

public class ArchivalStockServiceTest {

    private static final String KEY = "key";

    private final ArchivalStockRepository archivalStockRepository;

    private final ArchivalStockService archivalStockService;

    public ArchivalStockServiceTest() {
        archivalStockRepository = Mockito.mock(ArchivalStockRepository.class);
        archivalStockService = new ArchivalStockService(archivalStockRepository);
    }

    @Test
    public void addTest() {
        final ArchivalStockRecord e = new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY, 1), new HashMap<>());
        final EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);
        Mockito.when(archivalStockRepository.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.doNothing().when(archivalStockRepository).persist(e);
        archivalStockService.add(KEY, 1, Collections.emptyMap());
        Mockito.verify(entityTransaction, Mockito.times(1)).begin();
        Mockito.verify(archivalStockRepository, Mockito.times(1)).persist(e);
        Mockito.verify(entityTransaction, Mockito.times(1)).commit();
    }

    @Test
    public void addTest_exception() {
        final ArchivalStockRecord e = new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY, 1), new HashMap<>());
        final EntityTransaction entityTransaction = Mockito.mock(EntityTransaction.class);
        Mockito.when(archivalStockRepository.getTransaction()).thenReturn(entityTransaction);
        Mockito.doNothing().when(entityTransaction).begin();
        Mockito.doNothing().when(entityTransaction).commit();
        Mockito.doThrow(new RuntimeException()).when(archivalStockRepository).persist(e);
        Assertions.assertThrows(RuntimeException.class, () -> archivalStockService.add(KEY, 1, Collections.emptyMap()));
        Mockito.verify(entityTransaction, Mockito.times(1)).begin();
        Mockito.verify(archivalStockRepository, Mockito.times(1)).persist(e);
        Mockito.verify(entityTransaction, Mockito.times(1)).rollback();
    }

    @Test
    public void getAllByStockIdTest() {
        final Map<String, Object> expected = Collections.emptyMap();
        final ArchivalStockRecord e = new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY, 1), new HashMap<>());
        Mockito.when(archivalStockRepository.getAllByStockId(1, 1, 2)).thenReturn(List.of(e));
        final Flux<Map<String, Object>> actual = archivalStockService.getAllByStockId(1, 1, 2);
        Assertions.assertEquals(List.of(expected), actual.collectList().block());
        Mockito.verify(archivalStockRepository, Mockito.times(1)).getAllByStockId(1, 1, 2);
    }

}
