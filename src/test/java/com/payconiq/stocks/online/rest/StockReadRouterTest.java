package com.payconiq.stocks.online.rest;

import com.payconiq.stocks.online.model.StockDTO;
import com.payconiq.stocks.online.service.StockReadService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = StockReadRouter.class)
@WebFluxTest(controllers = StockReadRouter.class)
public class StockReadRouterTest {

    private static final String API_STOCKS = "/api/stocks/";

    private static final String API_STOCKS_ = API_STOCKS + "/";

    private static final String ID = "id";

    private static final String NAME = "name";

    private static final BigDecimal PRICE = BigDecimal.ONE;

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2020-04-07T15:00:08.441156Z");

    @MockBean
    private StockReadService stockReadService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void readRouteTest() {
        final long id1 = 1;
        final long id2 = 1;
        final List<StockDTO> expected = List.of(
                new StockDTO(id1, NAME, PRICE, NOW.minusDays(3)), new StockDTO(id1, NAME, PRICE, NOW.minusDays(2)),
                new StockDTO(id2, NAME, PRICE, NOW.minusDays(1)), new StockDTO(id2, NAME, PRICE, NOW)
        );
        Mockito.when(stockReadService.getAll(1, 2)).thenReturn(Flux.fromIterable(expected));
        webTestClient.get().uri(API_STOCKS + "?start=1&limit=2")
                .exchange()
                .expectStatus().isOk().expectBodyList(StockDTO.class)
                .value(e -> Assertions.assertEquals(expected, e));
        Mockito.verify(stockReadService, Mockito.times(1)).getAll(1, 2);
    }

    @Test
    public void readRouteTest_400() {
        webTestClient.get().uri(API_STOCKS)
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=A&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=1&limit=B")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=1.5&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=1,5&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=1&limit=2.5")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=1&limit=2,5")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=-1&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS + "?start=1&limit=0")
                .exchange()
                .expectStatus().isBadRequest();
        Mockito.verify(stockReadService, Mockito.times(0)).getAll(Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void readRouteTest_byId() {
        final long id = 1;
        final StockDTO expected = new StockDTO(id, NAME, PRICE, NOW);
        Mockito.when(stockReadService.getById(id)).thenReturn(Mono.just(expected));
        webTestClient.get().uri(API_STOCKS_ + id)
                .exchange()
                .expectStatus().isOk().expectBody(StockDTO.class)
                .value(e -> Assertions.assertEquals(expected, e));
        Mockito.verify(stockReadService, Mockito.times(1)).getById(1);
    }

    @Test
    public void readRouteTest_byId400() {
        webTestClient.get().uri(API_STOCKS_ + ID)
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + "1.1")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + "1,1")
                .exchange()
                .expectStatus().isBadRequest();
        Mockito.verify(stockReadService, Mockito.times(0)).getById(Mockito.anyLong());
    }

}
