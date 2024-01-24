package kz.baltabayev.dao;

import kz.baltabayev.model.User;

import java.util.Optional;

public interface UserDao extends BaseDao<Long, User> {

    Optional<User> findByPhoneNumber(String phoneNumber);


}
