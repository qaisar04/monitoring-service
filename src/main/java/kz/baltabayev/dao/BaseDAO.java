package kz.baltabayev.dao;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BaseDAO<U, T> {

    Optional<T> findById(U id);

    List<T> findAll();

    T save(T entity);
}
