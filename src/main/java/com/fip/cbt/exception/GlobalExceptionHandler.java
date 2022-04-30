package com.fip.cbt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValid(MethodArgumentNotValidException e){
        ExceptionResponse response = new ExceptionResponse()
                .setErrorCode("BAD_REQUEST")
                .setErrorMessage(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                .setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ExceptionResponse> dateTimeParse(DateTimeParseException e){
        ExceptionResponse response = new ExceptionResponse()
                .setErrorCode("BAD_REQUEST")
                .setErrorMessage(e.getMessage())
                .setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> resourceAlreadyExists(ResourceAlreadyExistsException e){
        ExceptionResponse response = new ExceptionResponse()
                .setErrorCode("CONFLICT")
                .setErrorMessage(e.getMessage())
                .setTimestamp(LocalDateTime.now());

        return  new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(ResourceNotFoundException e){
        ExceptionResponse response = new ExceptionResponse()
                .setErrorCode("NOT_FOUND")
                .setErrorMessage(e.getMessage())
                .setTimestamp(LocalDateTime.now());

        return  new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
