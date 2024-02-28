package kz.baltabayev.service;

import kz.baltabayev.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * The service interface for user-related functionality.
 */
public interface UserService {

    List<User> showAllUsers();

    User getUserById(Long id);

    Optional<User> getUserByLogin(String login);

    User save(User user);
}
