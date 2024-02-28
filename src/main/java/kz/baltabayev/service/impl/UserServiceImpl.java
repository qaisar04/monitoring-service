package kz.baltabayev.service.impl;

import kz.baltabayev.repository.UserRepository;
import kz.baltabayev.model.entity.User;
import kz.baltabayev.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementation of the {@link UserService} interface.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users
     */
    @Override
    public List<User> showAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return an optional containing the user if found, or empty if not found
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No user found with ID: " + id));
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
