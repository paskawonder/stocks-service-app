package com.payconiq.stocks.online.model.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STOCK")
@NamedQueries(
        @NamedQuery(name = "Stock.getAll", query = "from Stock")
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Stock {

    @Id
    @Column(name = "ID")
    @EqualsAndHashCode.Exclude
    private long id;

    @Version
    @Column(name = "VERSION")
    private int version;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CURRENT_PRICE")
    private BigDecimal currentPrice;

    @Column(name = "LAST_UPDATE")
    private OffsetDateTime lastUpdate;

}
