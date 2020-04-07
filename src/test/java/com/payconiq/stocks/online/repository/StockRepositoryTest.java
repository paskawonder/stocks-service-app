package com.payconiq.stocks.online.repository;

import com.payconiq.stocks.online.model.entity.Stock;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StockRepositoryTest {

    private static final String DELETE_FROM_STOCK_ENTITY = "delete from Stock";

    private static final String NAME = "name";

    private static final BigDecimal PRICE = BigDecimal.ONE;

    private static final OffsetDateTime NOW = OffsetDateTime.parse("2020-04-07T15:00:08.441156Z");

    private final EntityManager entityManager;

    private final StockRepository stockRepository;

    public StockRepositoryTest() {
        final EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("persistence");
        entityManager = entityManagerFactory.createEntityManager();
        stockRepository = new StockRepository(entityManager);
    }

    @BeforeEach
    private void dbClear() {
        entityManager.getTransaction().begin();
        entityManager.createQuery(DELETE_FROM_STOCK_ENTITY).executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    public void persistTest() {
        final Stock expected = new Stock(1, 0, NAME, PRICE, NOW);
        entityManager.getTransaction().begin();
        stockRepository.persist(expected);
        entityManager.getTransaction().commit();
        Assertions.assertEquals(expected, entityManager.find(Stock.class, 1L));
    }

    @Test
    public void persistTest_exception() {
        final EntityManagerFactory entityManagerFactory = Persistence
                .createEntityManagerFactory("persistence");
        final EntityManager concurrentEntityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        stockRepository.persist(new Stock(1, 0, NAME, PRICE, NOW));
        entityManager.getTransaction().commit();
        concurrentEntityManager.getTransaction().begin();
        new StockRepository(concurrentEntityManager).persist(new Stock(1, 0, NAME, PRICE, NOW));
        Assertions.assertThrows(RollbackException.class, () -> concurrentEntityManager.getTransaction().commit());
        entityManager.getTransaction().begin();
        Assertions.assertThrows(EntityExistsException.class,
                () -> stockRepository.persist(new Stock(1, 0, NAME, PRICE, NOW)));
    }

    @Test
    public void updateTest() {
        entityManager.getTransaction().begin();
        stockRepository.persist(new Stock(1, 0, NAME, PRICE, NOW));
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        stockRepository.update(new Stock(1, 0, NAME, BigDecimal.ZERO, NOW));
        entityManager.getTransaction().commit();
        final Stock expected = new Stock(1, 1, NAME, BigDecimal.ZERO, NOW);
        Assertions.assertEquals(expected, entityManager.find(Stock.class, 1L));
    }

    @Test
    public void updateTest_exception() {
        entityManager.getTransaction().begin();
        stockRepository.persist(new Stock(1, 0, NAME, PRICE, NOW));
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        stockRepository.update(new Stock(1, 0, NAME, BigDecimal.ZERO, NOW));
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        Assertions.assertThrows(OptimisticLockException.class, () -> stockRepository.update(new Stock(1, 0, NAME, PRICE, NOW)));
        Assertions.assertThrows(OptimisticLockException.class, () -> stockRepository.update(new Stock(1, 2, NAME, PRICE, NOW)));
        entityManager.getTransaction().rollback();
    }

    @Test
    public void getAllTest() {
        final List<Stock> expected = List.of(
                new Stock(1, 0, NAME, PRICE, NOW), new Stock(2, 0, NAME, BigDecimal.ZERO, NOW), new Stock(3, 0, NAME, BigDecimal.TEN, NOW)
        );
        entityManager.getTransaction().begin();
        expected.forEach(entityManager::persist);
        entityManager.getTransaction().commit();
        Assertions.assertEquals(expected.subList(0, 3), stockRepository.getAll( 0, 3));
        Assertions.assertEquals(expected.subList(0, 2), stockRepository.getAll( 0, 2));
        Assertions.assertEquals(expected.subList(1, 3), stockRepository.getAll( 1, 3));
    }

    @Test
    public void getAllTest_empty() {
        Assertions.assertEquals(Collections.emptyList(), stockRepository.getAll( 0, 1));
        Assertions.assertEquals(Collections.emptyList(), stockRepository.getAll( 1, 10));
    }

    @Test
    public void getByIdTest() {
        final Stock expected = new Stock(1, 0, NAME, PRICE, NOW);
        entityManager.getTransaction().begin();
        entityManager.persist(expected);
        entityManager.getTransaction().commit();
        Assertions.assertEquals(expected, stockRepository.getById(1));
    }

    @Test
    public void getByIdTest_null() {
        Assertions.assertNull(stockRepository.getById(1));
    }

}
