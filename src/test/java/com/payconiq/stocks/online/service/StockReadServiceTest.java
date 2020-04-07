package com.payconiq.stocks.online.service;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.converter.StockConverter;
import com.payconiq.stocks.online.model.entity.Stock;
import com.payconiq.stocks.online.repository.StockRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class StockReadServiceTest {

    private static final String NAME = "name";

    private static final BigDecimal PRICE = BigDecimal.ONE;

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2020-04-07T15:00:08.441156Z");

    private final StockRepository stockRepository;

    private final StockConverter stockConverter;

    private final StockReadService stockReadService;

    public StockReadServiceTest() {
        stockRepository = Mockito.mock(StockRepository.class);
        stockConverter = Mockito.mock(StockConverter.class);
        stockReadService = new StockReadService(stockRepository, stockConverter);
    }

    @Test
    public void getAllTest() {
        final Stock e = Mockito.mock(Stock.class);
        Mockito.when(stockRepository.getAll(1, 2)).thenReturn(List.of(e));
        final StockDTO s = new StockDTO(1L, NAME, PRICE, NOW);
        Mockito.when(stockConverter.convert(e)).thenReturn(s);
        final Flux<StockDTO> actual = stockReadService.getAll(1, 2);
        Assertions.assertEquals(List.of(s), actual.collectList().block());
        Mockito.verify(stockRepository, Mockito.times(1)).getAll(1, 2);
    }

    @Test
    public void getByIdTest() {
        final Stock e = Mockito.mock(Stock.class);
        final long id = 1;
        Mockito.when(stockRepository.getById(id)).thenReturn(e);
        final StockDTO expected = new StockDTO(id, NAME, PRICE, NOW);
        Mockito.when(stockConverter.convert(e)).thenReturn(expected);
        final Mono<StockDTO> actual = stockReadService.getById(id);
        Assertions.assertEquals(expected, actual.block());
        Mockito.verify(stockRepository, Mockito.times(1)).getById(id);
    }

    @Test
    public void getByIdTest_empty() {
        final long id = 1;
        Mockito.when(stockRepository.getById(id)).thenReturn(null);
        final Mono<StockDTO> actual = stockReadService.getById(id);
        Assertions.assertEquals(Mono.empty(), actual);
        Mockito.verify(stockRepository, Mockito.times(1)).getById(id);
    }

}
