package com.payconiq.stocks.online.repository;

import com.payconiq.stocks.online.model.entity.Stock;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.springframework.stereotype.Repository;

@Repository
public class StockRepository {

    private static final String GET_ALL = "Stock.getAll";

    private final EntityManager entityManager;

    public StockRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void persist(final Stock e) {
        entityManager.persist(e);
    }

    public void update(final Stock e) {
        entityManager.merge(e);
    }

    public List<Stock> getAll(final int start, final int limit) {
        return entityManager.createNamedQuery(GET_ALL, Stock.class)
                .setFirstResult(start).setMaxResults(limit)
                .getResultList();
    }

    public Stock getById(final long id) {
        return entityManager.find(Stock.class, id);
    }

    public EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

}
