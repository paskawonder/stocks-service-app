package com.payconiq.stocks.archival.rest;

import com.payconiq.stocks.archival.service.ArchivalStockService;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class ArchivalStockRouter {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_PARAMETERIZED_TYPE_REFERENCE = new ParameterizedTypeReference<>() {};

    private static final String API_STOCKS_ID_HISTORY = "/api/stocks/{id}/history";

    private static final String START = "start";

    private static final String LIMIT = "limit";

    private static final String ID = "id";

    @Bean
    public RouterFunction<ServerResponse> archivalReadRoute(final ArchivalStockService archivalStockService) {
        return RouterFunctions.route(RequestPredicates.GET(API_STOCKS_ID_HISTORY), request -> {
            final long id;
            final int start;
            final int limit;
            try {
                id = Long.parseLong(request.pathVariable(ID));
                start = Integer.parseInt(request.queryParam(START)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
                limit = Integer.parseInt(request.queryParam(LIMIT)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
            } catch (final NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if (id < 0 || start < 0 || limit < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(archivalStockService.getAllByStockId(id, start, limit), MAP_PARAMETERIZED_TYPE_REFERENCE);
        });
    }

}
