package kz.baltabayev.dao;

import kz.baltabayev.model.User;

import java.util.Optional;

/**
 * Data Access Object (DAO) interface for managing User entities.
 * Extends the generic MainDAO interface with specific operations for User entities.
 */
public interface UserDAO extends MainDAO<Long, User> {

    /**
     * Retrieves an optional User entity based on the provided login.
     *
     * @param login The login associated with the User entity.
     * @return An optional containing the User entity if found, otherwise an empty optional.
     */
    Optional<User> findByLogin(String login);

    /**
     * Updates the provided User entity.
     *
     * @param user The User entity to be updated.
     * @return The updated User entity.
     */
    User update(User user);
}
