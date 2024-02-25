package kz.baltabayev.exception;

/**
 * Exception thrown when validation parameters are not met.
 * This exception extends {@link RuntimeException}, indicating that certain parameters fail validation.
 */
public class ValidationParametersException extends RuntimeException {
    /**
     * Constructs a {@code ValidationParametersException} with the specified detail message.
     * @param message the detail message.
     */
    public ValidationParametersException(String message) {
        super(message);
    }
}
