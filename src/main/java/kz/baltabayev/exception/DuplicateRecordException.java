package kz.baltabayev.exception;

/**
 * Exception class representing an error due to a duplicate record.
 * Thrown when attempting to create a record that already exists in the system.
 */
public class DuplicateRecordException extends RuntimeException {

    /**
     * Constructs a new DuplicateRecordException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public DuplicateRecordException(String message) {
        super(message);
    }
}
