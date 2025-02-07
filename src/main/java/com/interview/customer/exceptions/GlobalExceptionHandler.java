package com.interview.customer.exceptions;

import com.interview.customer.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.interview.customer.dto.Error;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
            log.error("MethodArgumentNotValidException occurred: {}", ex.getMessage());
            ErrorResponse errorResponse = new ErrorResponse();
            if(!ex.getBindingResult().getAllErrors().isEmpty()) {
                errorResponse.setMessage("Validation failed");
                errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
                errorResponse.setTimestamp(LocalDate.now());
                List<Error> errors = new ArrayList<>();
                for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
                    Error error = new Error();
                    error.setField(fieldError.getField());
                    error.setLocation("body");
                    error.setReason(fieldError.getDefaultMessage());
                    errors.add(error);
                }
                errorResponse.setErrors(errors);
            }
            return ResponseEntity.badRequest().body(errorResponse);
        }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(CustomerNotFoundException ex){
        log.error("CustomerNotFoundException occurred : {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setTimestamp(LocalDate.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
        log.error("MethodArgumentTypeMismatchException occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getLocalizedMessage());
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(LocalDate.now());
        return errorResponse;
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex){
        log.error("Exception occurred: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setTimestamp(LocalDate.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}