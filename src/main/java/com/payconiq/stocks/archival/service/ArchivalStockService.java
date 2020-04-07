package com.payconiq.stocks.archival.service;

import com.payconiq.stocks.archival.model.ArchivalMessage;
import com.payconiq.stocks.archival.model.converter.ArchivalStockConverter;
import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import com.payconiq.stocks.archival.repository.ArchivalStockRepository;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ArchivalStockService {

    private final ArchivalStockRepository archivalStockRepository;

    private final ArchivalStockConverter archivalStockConverter;

    public ArchivalStockService(final ArchivalStockRepository archivalStockRepository,
                                final ArchivalStockConverter archivalStockConverter) {
        this.archivalStockRepository = archivalStockRepository;
        this.archivalStockConverter = archivalStockConverter;
    }

    @Transactional
    public void add(final String key, final ArchivalMessage message) {
        archivalStockRepository.persist(
                new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(key, message.getStockId()), message.getPayload())
        );
    }

    @Transactional
    public Flux<Map<String, Object>> getAllByStockId(final long stockId, final int start, final int limit) {
        return Flux.fromStream(
                archivalStockRepository.getAllByStockId(stockId, start, limit).stream().map(archivalStockConverter::convertPayload)
        );
    }

}
