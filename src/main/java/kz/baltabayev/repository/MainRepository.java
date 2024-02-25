package kz.baltabayev.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic interface for data access objects (DAOs) providing basic CRUD operations.
 *
 * @param <U> the type of the entity's identifier
 * @param <T> the type of the entity
 */
public interface MainRepository<U, T> {

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity
     * @return an optional containing the found entity, or empty if not found
     */
    Optional<T> findById(U id);

    /**
     * Retrieves a list of all entities.
     *
     * @return a list of all entities
     */
    List<T> findAll();

    /**
     * Saves or updates the given entity.
     *
     * @param entity the entity to be saved or updated
     * @return the saved or updated entity
     */
    T save(T entity);
}
