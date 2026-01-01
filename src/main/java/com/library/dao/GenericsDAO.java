package com.library.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public interface GenericsDAO<T,ID extends Serializable> {
    void create(T entity);
    void update(T entity);
    List<T> findAll();
    Optional<T> findById(ID id);
    void delete(ID id);
}
