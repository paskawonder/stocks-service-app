package com.payconiq.stocks.archival.repository;

import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ArchivalStockRepositoryTest {

    private static final String DELETE_FROM_ARCHIVAL_STOCK = "delete from ArchivalStockRecord";

    private static final String KEY = "key";

    private static final String PAYLOAD = "PAYLOAD";

    private final EntityManager entityManager;

    private final ArchivalStockRepository archivalStockRepository;

    public ArchivalStockRepositoryTest() {
        final EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("persistence");
        entityManager = entityManagerFactory.createEntityManager();
        archivalStockRepository = new ArchivalStockRepository(entityManager);
    }

    @BeforeEach
    private void dbClear() {
        entityManager.getTransaction().begin();
        entityManager.createQuery(DELETE_FROM_ARCHIVAL_STOCK).executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    public void persistTest() {
        final ArchivalStockRecord expected = new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY, 1), PAYLOAD);
        entityManager.getTransaction().begin();
        archivalStockRepository.persist(expected);
        entityManager.getTransaction().commit();
        Assertions.assertEquals(expected, entityManager.find(ArchivalStockRecord.class, new ArchivalStockRecord.KeyStockId(KEY, 1)));
    }

    @Test
    public void getAllByStockIdTest() {
        final List<ArchivalStockRecord> expected = List.of(
                new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY + 1, 1), PAYLOAD),
                new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY + 2, 1), PAYLOAD),
                new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY + 3, 1), PAYLOAD),
                new ArchivalStockRecord(new ArchivalStockRecord.KeyStockId(KEY + 1, 2), PAYLOAD)
        );
        entityManager.getTransaction().begin();
        expected.forEach(entityManager::persist);
        entityManager.getTransaction().commit();
        Assertions.assertEquals(expected.subList(0, 3), archivalStockRepository.getAllByStockId(1, 0, 3));
        Assertions.assertEquals(expected.subList(0, 2), archivalStockRepository.getAllByStockId(1, 0, 2));
        Assertions.assertEquals(expected.subList(1, 3), archivalStockRepository.getAllByStockId(1, 1, 3));
        Assertions.assertEquals(expected.subList(3, 4), archivalStockRepository.getAllByStockId(2, 0, 3));
    }

    @Test
    public void getAllByStockIdTest_empty() {
        Assertions.assertEquals(Collections.emptyList(), archivalStockRepository.getAllByStockId(1, 1, 10));
    }

}
