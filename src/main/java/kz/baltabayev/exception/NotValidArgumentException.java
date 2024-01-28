package kz.baltabayev.exception;

/**
 * Exception class representing an error due to an invalid argument.
 * Thrown when an argument provided to a method or function is not valid or fails validation.
 */
public class NotValidArgumentException extends RuntimeException {

    /**
     * Constructs a new NotValidArgumentException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public NotValidArgumentException(String message) {
        super(message);
    }
}
