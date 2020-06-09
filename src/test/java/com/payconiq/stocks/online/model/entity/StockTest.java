package com.payconiq.stocks.online.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class StockTest {

    @Test
    public void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(Stock.class).verify();
    }

}