package com.payconiq.stocks.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import com.payconiq.stocks.online.model.entity.Stock;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

@Profile("dev")
@Configuration
public class DevConfiguration {

    private static final String FROM_ARCHIVAL_STOCK_RECORD = "from ArchivalStockRecord";

    private static final String FROM_STOCK = "from Stock";

    private static final TypeReference<HashMap<String, Object>> HASH_MAP_TYPE_REFERENCE = new TypeReference<>() {};

    @Bean
    public int dbRoll(@Value("${jpa.datasource.url}") final String url,
                          @Value("${jpa.datasource.username}") final String username,
                          @Value("${jpa.datasource.password}") final String password,
                          final EntityManager entityManager, final ObjectMapper objectMapper) {
        final int migrate = Flyway.configure().dataSource(url, username, password).load().migrate();
        final EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        final List<ArchivalStockRecord> archivalStockRecordList = entityManager.createQuery(FROM_ARCHIVAL_STOCK_RECORD, ArchivalStockRecord.class).getResultList();
        if (archivalStockRecordList.isEmpty()) {
            final List<Stock> stockList = entityManager.createQuery(FROM_STOCK, Stock.class).getResultList();
            for (int i = 0; i < stockList.size(); i++) {
                final Stock s = stockList.get(i);
                for (int j = i; j >= 0; j--) {
                    final Stock e = new Stock(s.getId(), s.getVersion(), s.getName(), s.getCurrentPrice(), s.getLastUpdate().minusYears(j));
                    entityManager.persist(
                            new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(UUID.randomUUID().toString(), s.getId()), objectMapper.convertValue(e, HASH_MAP_TYPE_REFERENCE))
                    );

                }
            }
        }
        tx.commit();
        return migrate;
    }

    @Bean
    public EmbeddedKafkaBroker embeddedKafkaBroker(@Value("${kafka.port}") final int port,
                                                   @Value("${kafka.topic}") final String topic) {
        final EmbeddedKafkaBroker embeddedKafkaBroker = new EmbeddedKafkaBroker(1, true, topic);
        embeddedKafkaBroker.kafkaPorts(port);
        return embeddedKafkaBroker;
    }

}
