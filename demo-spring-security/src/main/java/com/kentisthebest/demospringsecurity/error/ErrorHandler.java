package com.kentisthebest.demospringsecurity.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    public static final String USER_NOT_FOUND = "User not found in the database.";

    public static final String CANNOT_CREATE_TRANSACTION = "Error encountered when connecting to db. Please check the logs for more info.";

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest req) {
        return handleExceptionInternal(ex, USER_NOT_FOUND, new HttpHeaders(), HttpStatus.NOT_FOUND, req);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ResponseEntity<Object> handleInternalServerError(CannotCreateTransactionException ex, WebRequest req) {
        return handleExceptionInternal(ex, CANNOT_CREATE_TRANSACTION, new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, req);
    }
}
