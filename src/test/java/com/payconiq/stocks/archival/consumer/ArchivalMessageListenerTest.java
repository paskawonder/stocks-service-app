package com.payconiq.stocks.archival.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stocks.archival.service.ArchivalStockService;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.support.Acknowledgment;

public class ArchivalMessageListenerTest {

    private static final String TOPIC = "topic";

    private static final String KEY = "key";

    private static final String ID = "id";

    private final ArchivalStockService archivalStockService;

    private final ArchivalMessageListener archivalMessageListener;

    public ArchivalMessageListenerTest() {
        archivalStockService = Mockito.mock(ArchivalStockService.class);
        archivalMessageListener = new ArchivalMessageListener(archivalStockService, new ObjectMapper());
    }

    @Test
    public void listenTest() {
        final Acknowledgment acknowledgment = Mockito.mock(Acknowledgment.class);
        Mockito.doNothing().when(archivalStockService).add(KEY, 1, Map.of(ID, 1));
        Mockito.doNothing().when(acknowledgment).acknowledge();
        final String value = "{\"id\":1}";
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, KEY, value), acknowledgment);
        Mockito.verify(archivalStockService, Mockito.times(1)).add(KEY, 1, Map.of(ID, 1));
        Mockito.verify(acknowledgment, Mockito.times(1)).acknowledge();
    }

    @Test
    public void listenTest_violations() {
        final Acknowledgment acknowledgment = Mockito.mock(Acknowledgment.class);
        Mockito.doNothing().when(acknowledgment).acknowledge();
        String value = "{\"id\"}";
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, KEY, value), acknowledgment);
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, KEY, null), acknowledgment);
        value = "{\"id\":-1}";
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, KEY, value), acknowledgment);
        value = "{\"id\":\"A\"}";
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, KEY, value), acknowledgment);
        value = "{\"id\":1}";
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, null, value), acknowledgment);
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, "", value), acknowledgment);
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, " ", value), acknowledgment);
        final String key256 = IntStream.range(0, 255).boxed().map(String::valueOf).collect(Collectors.joining(""));
        archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, key256, value), acknowledgment);
        Mockito.verify(archivalStockService, Mockito.times(0)).add(Mockito.anyString(), Mockito.anyLong(), Mockito.anyMap());
        Mockito.verify(acknowledgment, Mockito.times(8)).acknowledge();
    }

    @Test
    public void listenTest_exception() {
        final Acknowledgment acknowledgment = Mockito.mock(Acknowledgment.class);
        Mockito.doThrow(new RuntimeException()).when(archivalStockService).add(KEY, 1, Map.of(ID, 1));
        Mockito.doNothing().when(acknowledgment).acknowledge();
        final String value = "{\"id\":1}";
        Assertions.assertThrows(RuntimeException.class, () -> archivalMessageListener.listen(new ConsumerRecord<>(TOPIC, 0, 0, KEY, value), acknowledgment));
        Mockito.verify(archivalStockService, Mockito.times(1)).add(KEY, 1, Map.of(ID, 1));
        Mockito.verify(acknowledgment, Mockito.times(0)).acknowledge();
    }

}
