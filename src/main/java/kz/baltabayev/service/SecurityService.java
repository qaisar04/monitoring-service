package kz.baltabayev.service;

import kz.baltabayev.model.User;

import java.util.Optional;

public interface SecurityService {
    User register(String login, String password);

    Optional<User> authorization(String login, String password);
}
