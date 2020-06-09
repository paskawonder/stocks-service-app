package com.payconiq.stocks.online.model.converter;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.entity.Stock;
import org.springframework.stereotype.Component;

@Component
public class StockConverter {

    public Stock convert(final StockDTO s, final int version) {
        return new Stock(s.getId(), version, s.getName(), s.getCurrentPrice(), s.getLastUpdate());
    }

    public StockDTO convert(final Stock e) {
        return new StockDTO(e.getId(), e.getName(), e.getCurrentPrice(), e.getLastUpdate());
    }

}
