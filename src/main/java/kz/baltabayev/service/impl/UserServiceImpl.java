package kz.baltabayev.service.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.model.User;
import kz.baltabayev.service.AuditService;
import kz.baltabayev.service.UserService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public List<User> showAllUsers() {
        return userDAO.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userDAO.findById(id);
    }
}