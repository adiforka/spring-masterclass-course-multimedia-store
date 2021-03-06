package com.adison.shop.common.web;

import com.adison.shop.products.ProductNotFoundException;
import com.adison.shop.tokens.TokenNotFoundException;
import com.adison.shop.users.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.NamingException;
import java.time.Instant;
import java.util.Locale;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

// we can be more informative--than just sending back a 404--by putting an
// ExceptionDTO in the body of the response. 404 may mean the client misspelled the name of the resource
// which nonetheless exists
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @Autowired
    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> onException(Exception ex, Locale locale) {
        ex.printStackTrace();
        return createResponse(ex, INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> onUserNotFoundException(UserNotFoundException ex, Locale locale) {
        return createResponse(ex, NOT_FOUND, locale);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ExceptionDTO> onTokenNotFoundException(TokenNotFoundException ex, Locale locale) {
        return createResponse(ex, NOT_FOUND, locale);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionDTO> onProductNotFoundException(ProductNotFoundException ex, Locale locale) {

        return createResponse(ex, NOT_FOUND, locale);
    }

    @ExceptionHandler(NamingException.class)
    public ResponseEntity<ExceptionDTO> onNamingException(NamingException ex, Locale locale) {
        return createResponse(ex, NOT_FOUND, locale);
    }

    private ResponseEntity<ExceptionDTO> createResponse(Exception ex, HttpStatus status, Locale locale) {
        var exceptionName = ex.getClass().getSimpleName();
        String description;
        try {
            //message is under the key of the simple name of the class of the exception handling the sit
            description = messageSource.getMessage(ex.getClass().getSimpleName(), null, locale);
        } catch (NoSuchMessageException exception) {
            description = exceptionName;
        }
        ex.printStackTrace();
        return ResponseEntity
                .status(status)
                .body(new ExceptionDTO(description, Instant.now()));
    }
}
