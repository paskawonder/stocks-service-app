package com.payconiq.stocks.archival.service;

import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import com.payconiq.stocks.archival.repository.ArchivalStockRepository;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityTransaction;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ArchivalStockService {

    private final ArchivalStockRepository archivalStockRepository;

    public ArchivalStockService(final ArchivalStockRepository archivalStockRepository) {
        this.archivalStockRepository = archivalStockRepository;
    }

    public void add(final String key, final long stockId, final Map<String, Object> payload) {
        final EntityTransaction tx = archivalStockRepository.getTransaction();
        tx.begin();
        try {
            archivalStockRepository.persist(
                    new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(key, stockId), new HashMap<>(payload))
            );
        } catch (final Exception ex) {
            tx.rollback();
            throw ex;
        }
        tx.commit();
    }

    public Flux<Map<String, Object>> getAllByStockId(final long stockId, final int start, final int limit) {
        return Flux.fromStream(
                archivalStockRepository.getAllByStockId(stockId, start, limit).stream().map(ArchivalStockRecord::getPayload)
        );
    }

}
