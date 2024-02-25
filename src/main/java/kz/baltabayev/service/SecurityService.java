package kz.baltabayev.service;

import kz.baltabayev.dto.TokenResponse;
import kz.baltabayev.model.User;

import java.util.Optional;

/**
 * The service interface for security-related functionality.
 */
public interface SecurityService {

    User register(String login, String password);

    TokenResponse authorize(String login, String password);
}
