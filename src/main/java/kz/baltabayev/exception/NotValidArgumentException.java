package kz.baltabayev.exception;

public class NotValidArgumentException extends RuntimeException {
    public NotValidArgumentException(String message) {
        super(message);
    }
}
