package com.payconiq.stocks.online.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class StockPriceDTOTest {

    @Test
    public void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(StockPriceDTO.class).verify();
    }

}
