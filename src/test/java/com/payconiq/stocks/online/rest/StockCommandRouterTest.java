package com.payconiq.stocks.online.rest;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.model.StockPriceDTO;
import com.payconiq.stocks.online.service.StockCommandService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Set;
import javax.persistence.EntityExistsException;
import javax.persistence.OptimisticLockException;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = StockCommandRouter.class)
@WebFluxTest(controllers = StockCommandRouter.class)
public class StockCommandRouterTest {

    private static final String API_STOCKS = "/api/stocks";

    private static final String API_STOCKS_ = "/api/stocks/";

    private static final String NAME = "name";

    private static final BigDecimal PRICE = BigDecimal.ONE;

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2020-04-07T15:00:08.441156Z");

    @MockBean
    private Validator validator;

    @MockBean
    private StockCommandService stockCommandService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void commandRouteTest_post() {
        final StockDTO s = new StockDTO(1L, NAME, PRICE, NOW);
        Mockito.when(validator.validate(s)).thenReturn(Collections.emptySet());
        Mockito.doNothing().when(stockCommandService).create(s);
        webTestClient.post().uri(API_STOCKS).body(Mono.just(s), StockDTO.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(stockCommandService, Mockito.times(1)).create(s);
    }

    @Test
    public void commandRouteTest_post409() {
        final StockDTO s = new StockDTO(1L, NAME, PRICE, NOW);
        Mockito.when(validator.validate(s)).thenReturn(Collections.emptySet());
        Mockito.doThrow(new EntityExistsException()).when(stockCommandService).create(s);
        webTestClient.post().uri(API_STOCKS).body(Mono.just(s), StockDTO.class)
                .exchange()
                .expectStatus().is4xxClientError();
        Mockito.doThrow(new RollbackException()).when(stockCommandService).create(s);
        webTestClient.post().uri(API_STOCKS).body(Mono.just(s), StockDTO.class)
                .exchange()
                .expectStatus().is4xxClientError();
        Mockito.verify(stockCommandService, Mockito.times(2)).create(s);
    }

    @Test
    public void commandRouteTest_post400() {
        final StockDTO s = new StockDTO(1L, NAME, PRICE, NOW);
        @SuppressWarnings("unchecked")
        final Set<ConstraintViolation<StockDTO>> violations = Set.of(Mockito.mock(ConstraintViolation.class));
        Mockito.when(validator.validate(s)).thenReturn((violations));
        webTestClient.post().uri(API_STOCKS).body(Mono.just(s), StockDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
        Mockito.verify(stockCommandService, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void commandRouteTest_put() {
        final long id = 1;
        final StockPriceDTO stockPriceDTO = new StockPriceDTO(id, 0, PRICE, NOW);
        Mockito.when(validator.validate(stockPriceDTO)).thenReturn(Collections.emptySet());
        Mockito.doNothing().when(stockCommandService).updatePrice(stockPriceDTO);
        webTestClient.put().uri(API_STOCKS_ + id).body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(stockCommandService, Mockito.times(1)).updatePrice(stockPriceDTO);
    }

    @Test
    public void commandRouteTest_put400() {
        final long id = 1;
        final StockPriceDTO stockPriceDTO = new StockPriceDTO(id, 0, PRICE, NOW);
        @SuppressWarnings("unchecked")
        final Set<ConstraintViolation<StockPriceDTO>> violations = Set.of(Mockito.mock(ConstraintViolation.class));
        Mockito.when(validator.validate(stockPriceDTO)).thenReturn((violations));
        webTestClient.put().uri(API_STOCKS_ + id).body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
        Mockito.when(validator.validate(stockPriceDTO)).thenReturn((Collections.emptySet()));
        webTestClient.put().uri(API_STOCKS_ + NAME).body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.put().uri(API_STOCKS_ + 2).body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.put().uri(API_STOCKS_ + "1.1").body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.put().uri(API_STOCKS_ + "1,1").body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
        Mockito.verify(stockCommandService, Mockito.times(0)).updatePrice(Mockito.any());
    }

    @Test
    public void commandRouteTest_put404() {
        final long id = 1;
        final StockPriceDTO stockPriceDTO = new StockPriceDTO(id, 0, PRICE, NOW);
        Mockito.when(validator.validate(stockPriceDTO)).thenReturn(Collections.emptySet());
        Mockito.doThrow(new IllegalStateException()).when(stockCommandService).updatePrice(stockPriceDTO);
        webTestClient.put().uri(API_STOCKS_ + id).body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().isNotFound();
        Mockito.verify(stockCommandService, Mockito.times(1)).updatePrice(stockPriceDTO);
    }

    @Test
    public void commandRouteTest_put409() {
        final long id = 1;
        final StockPriceDTO stockPriceDTO = new StockPriceDTO(id, 0, PRICE, NOW);
        Mockito.when(validator.validate(stockPriceDTO)).thenReturn(Collections.emptySet());
        Mockito.doThrow(new OptimisticLockException()).when(stockCommandService).updatePrice(stockPriceDTO);
        webTestClient.put().uri(API_STOCKS_ + id).body(Mono.just(stockPriceDTO), StockPriceDTO.class)
                .exchange()
                .expectStatus().is4xxClientError();
        Mockito.verify(stockCommandService, Mockito.times(1)).updatePrice(stockPriceDTO);
    }

}
