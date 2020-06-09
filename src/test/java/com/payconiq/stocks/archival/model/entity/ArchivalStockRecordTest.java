package com.payconiq.stocks.archival.model.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class ArchivalStockRecordTest {

    @Test
    public void equalsAndHashCodeTest() {
        EqualsVerifier.forClass(ArchivalStockRecord.class).verify();
    }

    public static class KeyStockIdTest {

        @Test
        public void equalsAndHashCodeTest() {
            EqualsVerifier.forClass(ArchivalStockRecord.KeyStockId.class).verify();
        }

    }

}
