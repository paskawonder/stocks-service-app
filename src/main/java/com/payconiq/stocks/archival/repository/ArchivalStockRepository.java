package com.payconiq.stocks.archival.repository;

import com.payconiq.stocks.archival.model.entity.ArchivalStockRecord;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.springframework.stereotype.Repository;

@Repository
public class ArchivalStockRepository {

    private static final String GET_ALL_BY_STOCK_ID = "ArchivalStockRecord.getAllByStockId";

    private static final String STOCK_ID = "stockId";

    private final EntityManager entityManager;

    public ArchivalStockRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void persist(final ArchivalStockRecord e) {
        entityManager.merge(e);
    }

    public List<ArchivalStockRecord> getAllByStockId(final long stockId, final int start, final int limit) {
        return entityManager.createNamedQuery(GET_ALL_BY_STOCK_ID, ArchivalStockRecord.class)
                .setParameter(STOCK_ID, stockId)
                .setFirstResult(start).setMaxResults(limit)
                .getResultList();
    }

    public EntityTransaction getTransaction() {
        return entityManager.getTransaction();
    }

}
