package dao.implementations;

import dao.interfaces.IGenericDAOClass;
import jakarta.persistence.EntityManager;

import java.util.List;


public abstract class GenericDAO<T> implements IGenericDAOClass<T> {
    protected final EntityManager em;
    protected final Class<T> entityClass;

    public GenericDAO(EntityManager em, TypeToken<T> token){
        this.em = em;
        entityClass =  (Class<T>) token.getType();
   }

   @Override
    public T getEntityByID(Long id){
        return em.find(entityClass, id);
    }

    public List<T> getAllEntities() {
        return em.createQuery("Select e from %s e".formatted(entityClass.getSimpleName()),
                                                             entityClass).getResultList();
    }

    @Override
    public void insertEntity(T entity){
        em.persist(entity);
    }

    @Override
    public T updateEntity(T entity){
       return em.merge(entity);
    }

    @Override
    public void deleteEntity(Long id){
        T entity = em.find(entityClass, id);
        if(entity != null)
            em.remove(entity);
    }


}
