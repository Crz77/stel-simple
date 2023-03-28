package dao.interfaces;


import java.util.List;

public interface IGenericDAOClass<T> {
    T getEntityByID(Long id);
    public List<T> getAllEntities();
    void insertEntity(T entity);
    T updateEntity(T entity);
    void deleteEntity(Long id);
    }
