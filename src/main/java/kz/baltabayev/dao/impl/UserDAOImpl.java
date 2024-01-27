package kz.baltabayev.dao.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.util.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.*;

public class UserDAOImpl implements UserDAO {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

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

    @Override
    public Optional<User> findById(Long id) {
        User user = users.get(id);
        return user == null ? Optional.empty() : Optional.of(user);
    }

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

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

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

        return user == null ? Optional.empty() : Optional.of(user);
    }
}
