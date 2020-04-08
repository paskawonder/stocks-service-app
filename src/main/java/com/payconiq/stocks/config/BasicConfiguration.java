package com.payconiq.stocks.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@ComponentScan("com.payconiq.stocks")
@EnableTransactionManagement
@EnableWebFlux
@Import(KafkaConfiguration.class)
public class BasicConfiguration {

    private static final String PERSISTENCE = "persistence";

    private static final String HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        return objectMapper;
    }

    @Bean
    public EntityManager entityManager(@Value("${jpa.datasource.url}") final String url,
                                       @Value("${jpa.datasource.username}") final String username,
                                       @Value("${jpa.datasource.password}") final String password,
                                       @Value("${jpa.datasource.driver-class-name}") final String driverClassName,
                                       @Value("${jpa.ddl-auto}") final String ddlAuto) {
        final Map<String, String> persistenceMap = Map.of(
                "javax.persistence.jdbc.url", url,
                "javax.persistence.jdbc.user", username,
                "javax.persistence.jdbc.password", password,
                "javax.persistence.jdbc.driver", driverClassName,
                HIBERNATE_HBM2DDL_AUTO, ddlAuto
        );
        return Persistence.createEntityManagerFactory(PERSISTENCE, persistenceMap).createEntityManager();
    }

}
