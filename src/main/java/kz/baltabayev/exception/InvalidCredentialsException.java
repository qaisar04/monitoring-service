package kz.baltabayev.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * Exception thrown when the provided credentials are invalid.
 * This exception extends {@link BadCredentialsException}, indicating authentication failure due to bad credentials.
 */
public class InvalidCredentialsException extends BadCredentialsException {
    /**
     * Constructs an {@code InvalidCredentialsException} with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidCredentialsException(String msg) {
        super(msg);
    }
}
