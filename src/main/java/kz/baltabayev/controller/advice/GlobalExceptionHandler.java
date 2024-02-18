package kz.baltabayev.controller.advice;

import kz.baltabayev.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizeException.class)
    ResponseEntity<ApplicationError> handleAuthorizeException(AuthorizeException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(DuplicateRecordException.class)
    ResponseEntity<ApplicationError> handleDuplicateRecordException(DuplicateRecordException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    ResponseEntity<ApplicationError> handleInvalidCredentialsException(InvalidCredentialsException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NotValidArgumentException.class)
    ResponseEntity<ApplicationError> handleNotValidArgumentException(NotValidArgumentException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(RegisterException.class)
    ResponseEntity<ApplicationError> handleRegisterException(RegisterException exception) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(ValidationParametersException.class)
    ResponseEntity<ApplicationError> handleGradeNotFoundException(ValidationParametersException exception) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private ResponseEntity<ApplicationError> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApplicationError.builder()
                .status(status.value())
                .message(message)
                .build());
    }
}