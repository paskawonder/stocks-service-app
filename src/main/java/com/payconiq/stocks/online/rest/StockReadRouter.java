package com.payconiq.stocks.online.rest;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.service.StockReadService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class StockReadRouter {

    private static final String API_STOCKS = "/api/stocks";

    private static final String START = "start";

    private static final String LIMIT = "limit";

    private static final String ID = "id";

    private static final String _ID_ = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> readRoute(final StockReadService stockReadService) {
        return RouterFunctions.nest(RequestPredicates.path(API_STOCKS),
                RouterFunctions.route(RequestPredicates.GET(""), request -> {
                    final int start;
                    final int limit;
                    try {
                        start = Integer.parseInt(request.queryParam(START)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
                        limit = Integer.parseInt(request.queryParam(LIMIT)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)));
                    } catch (final NumberFormatException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    if (start < 0 || limit < 1) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON)
                            .body(stockReadService.getAll(start, limit), StockDTO.class);
                }).andRoute(RequestPredicates.GET(_ID_), request -> {
                    final long id;
                    try {
                        id = Long.parseLong(request.pathVariable(ID));
                    } catch (final NumberFormatException e) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    if (id < 0) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                    return ServerResponse.ok().contentType(MediaType.APPLICATION_STREAM_JSON).body(stockReadService.getById(id), StockDTO.class);
                })
        );
    }

}
