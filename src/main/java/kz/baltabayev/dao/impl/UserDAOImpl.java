package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of the UserDAO interface using an in-memory map.
 * Provides methods for CRUD operations on User entities and initializes
 * the map with a predefined admin User entity during construction.
 */
public class UserDAOImpl implements UserDAO {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    /**
     * Constructs a new UserDAOImpl and initializes the in-memory storage
     * with a predefined admin User entity.
     */
    public UserDAOImpl() {
        save(
                User.builder()
                        .id(-1L)
                        .login("admin")
                        .password("admin")
                        .role(Role.ADMIN)
                        .registrationDate(DateTimeUtils.parseDateTime(LocalDateTime.now()))
                        .build()
        );
    }

    /**
     * Retrieves a User entity by its ID.
     *
     * @param id The ID of the User entity to retrieve.
     * @return An Optional containing the found User entity, or an empty Optional if not found.
     */
    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);
        return user == null ? Optional.empty() : Optional.of(user);
    }

    /**
     * Updates an existing User entity in the in-memory storage.
     *
     * @param user The User entity to update.
     * @return The updated User entity.
     * @throws IllegalArgumentException If the User with the given ID is not found.
     */
    @Override
    public User update(User user) {
        Long userId = user.getId();
        if (users.containsKey(userId)) {
            users.put(userId, user);
            return users.get(userId);
        } else {
            throw new IllegalArgumentException("User with id " + userId + " not found, cannot update.");
        }
    }

    /**
     * Retrieves a list of all User entities.
     *
     * @return A list containing all User entities stored in the in-memory map.
     */
    @Override
    public List<User> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(users.values()));
    }

    /**
     * Saves a User entity to the in-memory storage.
     *
     * @param user The User entity to save.
     * @return The saved User entity with an assigned ID.
     */
    @Override
    public User save(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    /**
     * Retrieves a User entity by its login.
     *
     * @param login The login of the User entity to retrieve.
     * @return An Optional containing the found User entity, or an empty Optional if not found.
     */
    @Override
    public Optional<User> findByLogin(String login) {
        User user = null;
        List<User> list = new ArrayList<>(users.values());

        for (User us : list) {
            if (us.getLogin().equals(login)) {
                user = us;
                break;
            }
        }

        return Optional.ofNullable(user);
    }
}
