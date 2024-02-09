package kz.baltabayev.service;

import kz.baltabayev.model.User;

import java.util.List;
import java.util.Optional;

/**
 * The service interface for user-related functionality.
 */
public interface UserService {

    /**
     * Retrieves a list of all users.
     *
     * @return the list of all users
     */
    List<User> showAllUsers();

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return an Optional containing the user if found, otherwise empty Optional
     */
    Optional<User> getUserById(Long id);

    Optional<User> getUserByLogin(String login);
}
