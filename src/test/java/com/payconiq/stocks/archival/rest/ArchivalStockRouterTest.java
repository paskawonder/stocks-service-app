package com.payconiq.stocks.archival.rest;

import com.payconiq.stocks.archival.service.ArchivalStockService;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ArchivalStockRouter.class)
@WebFluxTest(controllers = ArchivalStockRouter.class)
public class ArchivalStockRouterTest {

    private static final ParameterizedTypeReference<Map<String, Object>> MAP_PARAMETERIZED_TYPE_REFERENCE = new ParameterizedTypeReference<>() {};

    private static final String API_STOCKS_ = "/api/stocks/";

    private static final String _HISTORY = "/history";

    private static final String ID = "id";

    @MockBean
    private ArchivalStockService archivalStockService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void archivalReadRouteTest() {
        final long id = 1;
        final List<Map<String, Object>> expected = List.of(Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap());
        Mockito.when(archivalStockService.getAllByStockId(id, 1, 2)).thenReturn(Flux.fromIterable(expected));
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=1&limit=2")
                .exchange()
                .expectStatus().isOk().expectBodyList(MAP_PARAMETERIZED_TYPE_REFERENCE)
                .value(e -> Assertions.assertEquals(expected, e));
        Mockito.verify(archivalStockService, Mockito.times(1)).getAllByStockId(1, 1, 2);
    }

    @Test
    public void archivalReadRouteTest_400() {
        webTestClient.get().uri(API_STOCKS_ + ID + _HISTORY)
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + "1.1" + _HISTORY)
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + "1,1" + _HISTORY)
                .exchange()
                .expectStatus().isBadRequest();
        final long id = 1;
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=A&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=1&limit=B")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=1.5&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=1,5&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=1&limit=2.5")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=1&limit=2,5")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=-1&limit=2")
                .exchange()
                .expectStatus().isBadRequest();
        webTestClient.get().uri(API_STOCKS_ + id + _HISTORY + "?start=1&limit=0")
                .exchange()
                .expectStatus().isBadRequest();
        Mockito.verify(archivalStockService, Mockito.times(0)).getAllByStockId(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());
    }

}
