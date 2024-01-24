package kz.baltabayev.dao;

import kz.baltabayev.model.User;

import java.util.Optional;

public interface UserDAO extends BaseDAO<Long, User> {

    Optional<User> findByLogin(String login);

}
