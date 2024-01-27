package kz.baltabayev.service.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.service.AuditService;
import kz.baltabayev.service.SecurityService;
import kz.baltabayev.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserDAO userDao;
    private final AuditService auditService;

    @Override
    public User register(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty() || login.isBlank() || password.isBlank()) {
            auditService.audit(login, ActionType.REGISTRATION, AuditType.FAIL);
            throw new NotValidArgumentException("Пароль или логин не могут быть пустыми или состоять только из пробелов.");
        }

        if (password.length() < 5 || password.length() > 30) {
            auditService.audit(login, ActionType.REGISTRATION, AuditType.FAIL);
            throw new NotValidArgumentException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> optionalUser = userDao.findByLogin(login);
        if (optionalUser.isPresent()) {
            auditService.audit(login, ActionType.REGISTRATION, AuditType.FAIL);
            throw new RegisterException("Пользователь с таким логином уже существует.");
        }

        User newUser = User.builder()
                .login(login)
                .password(password)
                .registrationDate(DateTimeUtils.parseDateTime(LocalDateTime.now()))
                .role(Role.USER)
                .build();

        auditService.audit(login, ActionType.REGISTRATION, AuditType.SUCCESS);
        return userDao.save(newUser);
    }

    @Override
    public User authorize(String login, String password) {
        Optional<User> optionalUser = userDao.findByLogin(login);
        if (optionalUser.isEmpty()) {
            auditService.audit(login, ActionType.AUTHORIZATION, AuditType.FAIL);
            throw new AuthorizeException("Пользователь с данным логином отсутствует в базе данных.");
        }

        if (!optionalUser.get().getPassword().equals(password)) {
            auditService.audit(login, ActionType.AUTHORIZATION, AuditType.FAIL);
            throw new AuthorizeException("Неверный пароль.");
        }

        auditService.audit(login, ActionType.AUTHORIZATION, AuditType.SUCCESS);
        return optionalUser.get();
    }
}
