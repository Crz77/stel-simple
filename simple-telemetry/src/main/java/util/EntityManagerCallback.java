package util;

import jakarta.persistence.EntityManager;

@FunctionalInterface
public interface EntityManagerCallback {
    void execute(EntityManager em);
}
