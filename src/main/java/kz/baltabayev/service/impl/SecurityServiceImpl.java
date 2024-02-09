package kz.baltabayev.service.impl;

import kz.baltabayev.dao.UserDAO;
import kz.baltabayev.dto.TokenResponse;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.security.JwtTokenUtils;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.model.types.AuditType;
import kz.baltabayev.service.AuditService;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * Implementation of the {@link SecurityService} interface.
 */
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserDAO userDao;
    private final AuditService auditService;
    private final JwtTokenUtils jwtTokenUtils;

    /**
     * Registers a new user with the provided login and password.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return the registered user
     * @throws NotValidArgumentException if login or password is empty, blank, or does not meet length requirements
     * @throws RegisterException         if a user with the same login already exists
     */
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
                .build();

        auditService.audit(login, ActionType.REGISTRATION, AuditType.SUCCESS);
        return userDao.save(newUser);
    }

    /**
     * Authorizes a user with the provided login and password.
     *
     * @param login    the user's login
     * @param password the user's password
     * @return an optional containing the authorized user, or empty if authorization fails
     * @throws AuthorizeException if the user is not found or the password is incorrect
     */
    @Override
    public TokenResponse authorize(String login, String password) {
        Optional<User> optionalUser = userDao.findByLogin(login);
        if (optionalUser.isEmpty()) {
            auditService.audit(login, ActionType.AUTHORIZATION, AuditType.FAIL);
            throw new AuthorizeException("Пользователь с данным логином отсутствует в базе данных.");
        }

        if (!optionalUser.get().getPassword().equals(password)) {
            auditService.audit(login, ActionType.AUTHORIZATION, AuditType.FAIL);
            throw new AuthorizeException("Неверный пароль.");
        }

        String token = jwtTokenUtils.generateToken(login);
        auditService.audit(login, ActionType.AUTHORIZATION, AuditType.SUCCESS);
        return new TokenResponse(token);
    }
}
