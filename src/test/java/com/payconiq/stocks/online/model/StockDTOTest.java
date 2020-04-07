package com.payconiq.stocks.online.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class StockDTOTest {

    @Test
    public void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(StockDTO.class).verify();
    }

}
