package com.payconiq.stocks.online.service;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.StockPriceDTO;
import com.payconiq.stocks.online.model.converter.StockConverter;
import com.payconiq.stocks.online.model.entity.Stock;
import com.payconiq.stocks.online.repository.ArchivalDataService;
import com.payconiq.stocks.online.repository.StockRepository;
import javax.persistence.EntityTransaction;
import org.springframework.stereotype.Service;

@Service
public class StockCommandService {

    private final StockRepository stockRepository;

    private final StockConverter stockConverter;

    private final ArchivalDataService archivalDataService;

    public StockCommandService(final StockRepository stockRepository, final StockConverter stockConverter,
                               final ArchivalDataService archivalDataService) {
        this.stockRepository = stockRepository;
        this.stockConverter = stockConverter;
        this.archivalDataService = archivalDataService;
    }

    public void create(final StockDTO s) {
        final Stock e = stockConverter.convert(s, 0);
        final EntityTransaction tx = stockRepository.getTransaction();
        tx.begin();
        stockRepository.persist(e);
        tx.commit();
        archivalDataService.produce(e);
    }

    public void updatePrice(final StockPriceDTO s) {
        final long id = s.getId();
        final Stock e = stockRepository.getById(id);
        if (e == null) {
            throw new IllegalStateException();
        }
        final Stock updated = new Stock(id, s.getVersion(), e.getName(), s.getPrice(), s.getLastUpdate());
        final EntityTransaction tx = stockRepository.getTransaction();
        tx.begin();
        stockRepository.update(updated);
        tx.commit();
        archivalDataService.produce(updated);
    }

}
