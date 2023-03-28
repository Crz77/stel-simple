package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static EntityManagerFactory emFactory;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emFactory == null)
            emFactory = Persistence.createEntityManagerFactory("TelemetryPU");
        return emFactory;
    }

    public static synchronized void closeEntityManagerFactory() {
        if (emFactory != null) {
            emFactory.close();
            emFactory = null;
        }
    }

    public static EntityManager getTransactionalEntityManager() {
        EntityManager em = getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        return em;
    }

    public static void commit(EntityManager em) {
        var tx = em.getTransaction();
        if (tx.isActive()) tx.commit();
    }

    public static void rollback(EntityManager em) {
        var tx = em.getTransaction();
        if (tx.isActive()) tx.rollback();
    }

    public static void executeInTransaction(EntityManagerCallback callback) {
        try (EntityManager em = getTransactionalEntityManager()) {
            try {
                callback.execute(em);
                commit(em);
            } catch (Exception ex) {
                rollback(em);
                throw ex;
            }

        } // em.close(); emFactory.close();
    }
}
