package com.backend.pdf_wizard.exception;

import com.backend.pdf_wizard.model.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({ResourceAlreadyExistsException.class})
    public ResponseEntity<ErrorResponseDto> handleResourceAlreadyExistsException(RuntimeException exception) {
        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Data Conflict",
                exception.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            ResourceNotFountException.class,
            UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleResourceNotFountException(RuntimeException exception) {
        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Data not found",
                exception.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String field = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    errors.put(field, message);
                });

        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                errors
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException exception) {
        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Credentials",
                exception.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({StorageException.class})
    public ResponseEntity<ErrorResponseDto> handleStorageException(RuntimeException exception) {
        ErrorResponseDto response = new ErrorResponseDto(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Storage exception",
                exception.getMessage()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
