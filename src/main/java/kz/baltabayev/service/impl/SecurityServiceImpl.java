package kz.baltabayev.service.impl;

import kz.baltabayev.annotations.Auditable;
import kz.baltabayev.annotations.Loggable;
import kz.baltabayev.exception.InvalidCredentialsException;
import kz.baltabayev.model.types.Role;
import kz.baltabayev.repository.UserRepository;
import kz.baltabayev.dto.TokenResponse;
import kz.baltabayev.exception.AuthorizeException;
import kz.baltabayev.exception.NotValidArgumentException;
import kz.baltabayev.exception.RegisterException;
import kz.baltabayev.security.JwtTokenUtils;
import kz.baltabayev.model.User;
import kz.baltabayev.model.types.ActionType;
import kz.baltabayev.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the {@link SecurityService} interface.
 */
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
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
    @Loggable
    @Auditable(actionType = ActionType.REGISTRATION, login = "@login")
    public User register(String login, String password) {
        if (login == null || password == null || login.isEmpty() || password.isEmpty() || login.isBlank() || password.isBlank()) {
            throw new NotValidArgumentException("Пароль или логин не могут быть пустыми или состоять только из пробелов.");
        }

        if (password.length() < 5 || password.length() > 30) {
            throw new NotValidArgumentException("Длина пароля должна составлять от 5 до 30 символов.");
        }

        Optional<User> optionalUser = userRepository.findByLogin(login);
        if (optionalUser.isPresent()) {
            throw new RegisterException("Пользователь с таким логином уже существует.");
        }

        User newUser = User.builder()
                .login(login)
                .role(Role.USER)
                .password(passwordEncoder.encode(password))
                .build();

        return userRepository.save(newUser);
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
    @Auditable(actionType = ActionType.AUTHORIZATION, login = "@login")
    public TokenResponse authorize(String login, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        String token = jwtTokenUtils.generateToken(userDetails);

        return new TokenResponse(token);
    }
}
