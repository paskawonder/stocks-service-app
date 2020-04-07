package com.payconiq.stocks.online.service;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.StockPriceDTO;
import com.payconiq.stocks.online.model.converter.StockConverter;
import com.payconiq.stocks.online.model.entity.Stock;
import com.payconiq.stocks.online.repository.StockRepository;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class StockCommandServiceTest {

    private static final String NAME = "name";

    private static final BigDecimal PRICE = BigDecimal.ONE;

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2020-04-07T15:00:08.441156Z");

    private final StockRepository stockRepository;

    private final StockConverter stockConverter;

    private final StockCommandService stockCommandService;

    public StockCommandServiceTest() {
        stockRepository = Mockito.mock(StockRepository.class);
        stockConverter = Mockito.mock(StockConverter.class);
        stockCommandService = new StockCommandService(stockRepository, stockConverter);
    }

    @Test
    public void createTest() {
        final StockDTO s = new StockDTO(1L, NAME, PRICE, NOW);
        final Stock e = Mockito.mock(Stock.class);
        Mockito.when(stockConverter.convert(s, 0)).thenReturn(e);
        stockCommandService.create(s);
        Mockito.verify(stockRepository, Mockito.times(1)).persist(e);
    }

    @Test
    public void updatePriceTest() {
        final long id = 1;
        final StockPriceDTO s = new StockPriceDTO(id, 0, PRICE, NOW);
        final Stock e = Mockito.mock(Stock.class);
        Mockito.when(e.getName()).thenReturn(NAME);
        Mockito.when(stockRepository.getById(1)).thenReturn(e);
        stockCommandService.updatePrice(s);
        final Stock expected = new Stock(1, 0, NAME, PRICE, NOW);
        Mockito.verify(stockRepository, Mockito.times(1)).update(expected);
    }

    @Test
    public void updatePriceTest_absent() {
        final long id = 1;
        final StockPriceDTO s = new StockPriceDTO(id, 0, PRICE, NOW);
        Mockito.when(stockRepository.getById(1)).thenReturn(null);
        Assertions.assertThrows(IllegalStateException.class, () -> stockCommandService.updatePrice(s));
        Mockito.verify(stockRepository, Mockito.times(0)).update(Mockito.any(Stock.class));
    }

}
