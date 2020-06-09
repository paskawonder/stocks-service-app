package com.payconiq.stocks.online.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class StockDTO {

    @NotNull
    @Min(0)
    private final Long id;

    @NotBlank
    @Size(max = 255)
    private final String name;

    @NotNull
    @Min(0)
    private final BigDecimal currentPrice;

    @NotNull
    private final OffsetDateTime lastUpdate;

    @JsonCreator
    public StockDTO(@JsonProperty("id") final Long id,
                    @JsonProperty("name") final String name,
                    @JsonProperty("currentPrice") final BigDecimal currentPrice,
                    @JsonProperty("lastUpdate") final OffsetDateTime lastUpdate) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.lastUpdate = lastUpdate;
    }

}
