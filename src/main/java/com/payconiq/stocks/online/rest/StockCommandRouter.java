package com.payconiq.stocks.online.rest;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.StockPriceDTO;
import com.payconiq.stocks.online.service.StockCommandService;
import javax.persistence.EntityExistsException;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;
import javax.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class StockCommandRouter {

    private static final String API_STOCKS = "/api/stocks";

    private static final String ID = "id";

    private static final String _ID_ = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> commandRoute(final Validator validator, final StockCommandService stockCommandService) {
        return RouterFunctions.nest(RequestPredicates.path(API_STOCKS),
                RouterFunctions.route(RequestPredicates.POST(""), request ->
                        request.bodyToMono(StockDTO.class).flatMap(e -> {
                            if (!CollectionUtils.isEmpty(validator.validate(e))) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                            }
                            try {
                                stockCommandService.create(e);
                            } catch (final EntityExistsException | RollbackException ex) {
                                throw new ResponseStatusException(HttpStatus.CONFLICT);
                            }
                            return ServerResponse.ok().build();
                        })
                ).andRoute(RequestPredicates.PUT(_ID_), request ->
                        request.bodyToMono(StockPriceDTO.class).flatMap(e -> {
                            try {
                                if (!CollectionUtils.isEmpty(validator.validate(e))
                                        || Long.parseLong(request.pathVariable(ID)) != e.getId()) {
                                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                                }
                            } catch (final NumberFormatException ex) {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                            }
                            try {
                                stockCommandService.updatePrice(e);
                            } catch (final IllegalStateException ex) {
                                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                            } catch (final OptimisticLockException ex) {
                                throw new ResponseStatusException(HttpStatus.CONFLICT);
                            }
                            return ServerResponse.ok().build();
                        })
                ));
    }

}
