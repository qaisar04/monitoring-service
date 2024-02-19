package kz.baltabayev.exception;

/**
 * Exception thrown when a user is not found.
 * This exception extends {@link RuntimeException}, indicating that a requested user could not be found.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructs a {@code UserNotFoundException} with the specified detail message.
     * @param message the detail message.
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
