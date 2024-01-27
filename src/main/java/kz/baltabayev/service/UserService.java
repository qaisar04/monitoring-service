package kz.baltabayev.service;

import kz.baltabayev.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> showAllUsers();

    Optional<User> getUserById(Long id);
}
