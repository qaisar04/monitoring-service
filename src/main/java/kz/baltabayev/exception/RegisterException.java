package kz.baltabayev.exception;

/**
 * Exception class representing an error during user registration.
 * Thrown when an issue occurs while attempting to register a user.
 */
public class RegisterException extends RuntimeException {

    /**
     * Constructs a new RegisterException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public RegisterException(String message) {
        super(message);
    }
}
