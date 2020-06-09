package com.payconiq.stocks.online.service;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.converter.StockConverter;
import com.payconiq.stocks.online.model.entity.Stock;
import com.payconiq.stocks.online.repository.StockRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StockReadService {

    private final StockRepository stockRepository;

    private final StockConverter stockConverter;

    public StockReadService(final StockRepository stockRepository, final StockConverter stockConverter) {
        this.stockRepository = stockRepository;
        this.stockConverter = stockConverter;
    }

    public Flux<StockDTO> getAll(final int start, final int limit) {
        return Flux.fromStream(stockRepository.getAll(start, limit).stream().map(stockConverter::convert));
    }

    public Mono<StockDTO> getById(final long id) {
        final Stock e = stockRepository.getById(id);
        final Mono<StockDTO> result;
        if (e != null) {
            result = Mono.just(stockConverter.convert(e));
        } else {
            result = Mono.empty();
        }
        return result;
    }

}
