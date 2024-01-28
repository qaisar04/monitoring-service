package kz.baltabayev.service.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.model.User;
import kz.baltabayev.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface.
 */
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users
     */
    @Override
    public List<User> showAllUsers() {
        return userDAO.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return an optional containing the user if found, or empty if not found
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }
}
