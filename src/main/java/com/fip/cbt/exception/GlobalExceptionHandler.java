package com.fip.cbt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValid(MethodArgumentNotValidException e){
//        String fieldName = Objects.requireNonNull(e.getFieldError()).getField();
//        String defaultMessage = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ExceptionResponse response = new ExceptionResponse()
                .setErrorCode("BAD_REQUEST")
                .setErrorMessage(Objects.requireNonNull(e.getFieldError()).getDefaultMessage())
                //.setErrorMessage(String.format("Error on field [%s]: %s", fieldName, defaultMessage))
                .setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> responseStatus(ResponseStatusException e){
        ExceptionResponse response = new ExceptionResponse()
                .setErrorCode(e.getStatus().toString())
                .setErrorMessage(e.getLocalizedMessage())
                .setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, e.getStatus());
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
