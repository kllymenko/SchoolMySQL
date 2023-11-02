package org.example.DAO;

import java.util.List;

public interface CRUDRepository<T> {
    int insert(T entity);
    boolean update(T entity);
    boolean delete(int id);
    List<T> findAll();
    T findById(int id);
}
