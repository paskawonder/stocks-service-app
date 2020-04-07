package com.payconiq.stocks.archival.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ArchivalMessage {

    @NotNull
    @Min(0)
    private final Long stockId;

    @NotBlank
    private final String payload;

    @JsonCreator
    public ArchivalMessage(@JsonProperty("stockId") final Long stockId,
                           @JsonProperty("payload") final String payload) {
        this.stockId = stockId;
        this.payload = payload;
    }

}
