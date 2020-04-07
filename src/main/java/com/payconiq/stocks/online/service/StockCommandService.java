package com.payconiq.stocks.online.service;

import com.payconiq.stocks.online.model.converter.StockConverter;
import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.StockPriceDTO;
import com.payconiq.stocks.online.model.entity.Stock;
import com.payconiq.stocks.online.repository.StockRepository;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StockCommandService {

    private final StockRepository stockRepository;

    private final StockConverter stockConverter;

    public StockCommandService(final StockRepository stockRepository, final StockConverter stockConverter) {
        this.stockRepository = stockRepository;
        this.stockConverter = stockConverter;
    }

    @Transactional
    public void create(final StockDTO s) {
        final Stock e = stockConverter.convert(s, 0);
        stockRepository.persist(e);
    }

    @Transactional
    public void updatePrice(final StockPriceDTO s) {
        final long id = s.getId();
        final Stock e = stockRepository.getById(id);
        if (e == null) {
            throw new IllegalStateException();
        }
        final Stock updated = new Stock(id, s.getVersion(), e.getName(), s.getPrice(), s.getLastUpdate());
        stockRepository.update(updated);
    }

}
