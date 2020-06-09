package com.payconiq.stocks.online.model.converter;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.entity.Stock;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StockConverterTest {

    private static final String NAME = "name";

    private static final BigDecimal PRICE = BigDecimal.ONE;

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2020-04-07T15:00:08.441156Z");

    private final StockConverter stockConverter = new StockConverter();

    @Test
    public void convertTest_stockDTO() {
        final StockDTO s = new StockDTO(1L, NAME, PRICE, NOW);
        final Stock expected = new Stock(1, 0, NAME, PRICE, NOW);
        final Stock actual = stockConverter.convert(s, 0);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void convertTest_stockEntity() {
        final Stock e = new Stock(1, 0, NAME, PRICE, NOW);
        final StockDTO expected = new StockDTO(1L, NAME, PRICE, NOW);
        final StockDTO actual = stockConverter.convert(e);
        Assertions.assertEquals(expected, actual);
    }

}
