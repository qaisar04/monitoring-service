package kz.baltabayev.dao;

import kz.baltabayev.model.User;

import java.util.Optional;

public interface UserDAO extends MainDAO<Long, User> {

    Optional<User> findByLogin(String login);

    User update(User user);
}
