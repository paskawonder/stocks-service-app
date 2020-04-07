package com.payconiq.stocks.online.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class StockPriceDTO {

    @NotNull
    @Min(0)
    private final Long id;

    @NotNull
    @Min(0)
    private final Integer version;

    @NotNull
    @Min(0)
    private final BigDecimal price;

    @NotNull
    private final OffsetDateTime lastUpdate;

    public StockPriceDTO(@JsonProperty("id") final Long id,
                         @JsonProperty("version") final Integer version,
                         @JsonProperty("price") final BigDecimal price,
                         @JsonProperty("lastUpdate") final OffsetDateTime lastUpdate) {
        this.id = id;
        this.version = version;
        this.price = price;
        this.lastUpdate = lastUpdate;
    }

}
