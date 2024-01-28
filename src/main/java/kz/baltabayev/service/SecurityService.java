package kz.baltabayev.service;

import kz.baltabayev.model.User;

import java.util.Optional;

/**
 * The service interface for security-related functionality.
 */
public interface SecurityService {

    /**
     * Registers a new user.
     *
     * @param login    the login of the user
     * @param password the password of the user
     * @return the registered user
     */
    User register(String login, String password);
    /**
     * Authorizes a user with the provided login and password.
     *
     * @param login    the login of the user
     * @param password the password of the user
     * @return an Optional containing the authorized user if login and password are valid, otherwise empty Optional
     */
    Optional<User> authorize(String login, String password);
}
