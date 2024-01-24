package kz.baltabayev.service.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.model.User;
import kz.baltabayev.service.SecurityService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class SecurityServiceImpl implements SecurityService {

    private final UserDAO userDao;

    public SecurityServiceImpl(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public User register(String login, String password) {
        Optional<User> optionalUser = userDao.findByLogin(login);
        if (optionalUser.isPresent()) {
            throw new RegisterException("The user with this login already exists.");
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setRegistrationDate(LocalDate.now());
        return userDao.save(newUser);
    }

    @Override
    public Optional<User> authorization(String login, String password) {
        Optional<User> optionalUser = userDao.findByLogin(login);
        if (optionalUser.isEmpty()) {
            throw new AuthorizeException("There is no user with this login in the database.");
        }

        if (!optionalUser.get().getPassword().equals(password)) {
            throw new AuthorizeException("Incorrect password.");
        }

        return optionalUser;
    }
}
