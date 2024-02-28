package kz.baltabayev.dto;

/**
 * A record representing a request for security-related operations, such as user registration or authorization.
 * This record encapsulates the login and password credentials.
 */
public record SecurityRequest
        (
                String login,
                String password
        ) {
}
