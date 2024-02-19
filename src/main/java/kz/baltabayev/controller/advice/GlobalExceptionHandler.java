package kz.baltabayev.controller.advice;

import kz.baltabayev.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controller advice class to handle global exceptions and return appropriate error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles AuthorizeException and returns a ResponseEntity with an ApplicationError body.
     * @param exception The AuthorizeException instance.
     * @return ResponseEntity with HTTP status UNAUTHORIZED and ApplicationError body.
     */
    @ExceptionHandler(AuthorizeException.class)
    ResponseEntity<ApplicationError> handleAuthorizeException(AuthorizeException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    /**
     * Handles DuplicateRecordException and returns a ResponseEntity with an ApplicationError body.
     * @param exception The DuplicateRecordException instance.
     * @return ResponseEntity with HTTP status BAD_REQUEST and ApplicationError body.
     */
    @ExceptionHandler(DuplicateRecordException.class)
    ResponseEntity<ApplicationError> handleDuplicateRecordException(DuplicateRecordException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles InvalidCredentialsException and returns a ResponseEntity with an ApplicationError body.
     * @param exception The InvalidCredentialsException instance.
     * @return ResponseEntity with HTTP status BAD_REQUEST and ApplicationError body.
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    ResponseEntity<ApplicationError> handleInvalidCredentialsException(InvalidCredentialsException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles NotValidArgumentException and returns a ResponseEntity with an ApplicationError body.
     * @param exception The NotValidArgumentException instance.
     * @return ResponseEntity with HTTP status BAD_REQUEST and ApplicationError body.
     */
    @ExceptionHandler(NotValidArgumentException.class)
    ResponseEntity<ApplicationError> handleNotValidArgumentException(NotValidArgumentException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles RegisterException and returns a ResponseEntity with an ApplicationError body.
     * @param exception The RegisterException instance.
     * @return ResponseEntity with HTTP status UNAUTHORIZED and ApplicationError body.
     */
    @ExceptionHandler(RegisterException.class)
    ResponseEntity<ApplicationError> handleRegisterException(RegisterException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    /**
     * Handles ValidationParametersException and returns a ResponseEntity with an ApplicationError body.
     * @param exception The ValidationParametersException instance.
     * @return ResponseEntity with HTTP status BAD_REQUEST and ApplicationError body.
     */
    @ExceptionHandler(ValidationParametersException.class)
    ResponseEntity<ApplicationError> handleGradeNotFoundException(ValidationParametersException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Exception handler method for handling {@link UserNotFoundException}.
     * This method returns a {@link ResponseEntity} with an {@link ApplicationError} body,
     * containing the HTTP status code NOT_FOUND and the message from the exception.
     *
     * @param exception The {@link UserNotFoundException} instance to handle.
     * @return A {@link ResponseEntity} with the appropriate error response.
     */
    @ExceptionHandler(UserNotFoundException.class)
    ResponseEntity<ApplicationError> handleUserNotFoundException(UserNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }


    /**
     * Builds a ResponseEntity with the specified HTTP status and error message.
     * @param status The HTTP status.
     * @param message The error message.
     * @return ResponseEntity with the specified status and ApplicationError body.
     */
    private ResponseEntity<ApplicationError> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApplicationError.builder()
                .status(status.value())
                .message(message)
                .build());
    }
}
