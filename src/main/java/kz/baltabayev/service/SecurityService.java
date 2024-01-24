package kz.baltabayev.service;

import kz.baltabayev.dao.UserDao;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;

import java.util.List;
import java.util.Optional;

import static kz.baltabayev.model.types.ActionType.REGISTRATION;
import static kz.baltabayev.model.types.AuditType.SUCCESS;

public class SecurityService {

    private final List<String> audits;
    private final UserDao userDao;

    public SecurityService(List<String> audits, UserDao userDao) {
        this.audits = audits;
        this.userDao = userDao;
    }

    public User register(String firstname, String phoneNumber, String password) {
        Optional<User> optionalUser = userDao.findByPhoneNumber(phoneNumber);
        if (optionalUser.isPresent()) {
            throw new RegisterException("The player with this login already exists.");
        }

        audit(firstname, REGISTRATION, SUCCESS);
        return userDao.save(create(firstname, password, phoneNumber));
    }

    public User authorization(String phoneNumber, String password) {
        Optional<User> optionalUser = userDao.findByPhoneNumber(phoneNumber);
        if (optionalUser.isEmpty()) {
            throw new AuthorizeException("There is no player with this login in the database.");
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
            throw new AuthorizeException("Incorrect password.");
        }

        return user;
    }

    private User create(String firstname, String password, String phoneNumber) {
        return User.builder()
                .firstname(firstname)
                .password(password)
                .phoneNumber(phoneNumber)
                .build();
    }

    public void audit(String username, ActionType actionType, AuditType auditType) {
        String s = "Пользователь " + username + ": " + actionType + " | " + auditType;
        audits.add(s);
    }
}
