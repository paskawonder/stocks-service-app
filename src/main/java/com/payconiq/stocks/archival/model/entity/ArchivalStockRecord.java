package com.payconiq.stocks.archival.model.entity;

import java.io.Serializable;
import java.util.HashMap;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ARCHIVAL_STOCK_DATA")
@NamedQueries(
        @NamedQuery(name = "ArchivalStockRecord.getAllByStockId", query = "from ArchivalStockRecord where keyStockId.stockId = :stockId")
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ArchivalStockRecord {

    @EmbeddedId
    @EqualsAndHashCode.Exclude
    private KeyStockId keyStockId;

    @Column(name = "PAYLOAD")
    @Lob
    private HashMap<String, Object> payload;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class KeyStockId implements Serializable {

        @Column(name = "RECORD_KEY")
        private String key;

        @Column(name = "STOCK_ID")
        private long stockId;

    }

}
