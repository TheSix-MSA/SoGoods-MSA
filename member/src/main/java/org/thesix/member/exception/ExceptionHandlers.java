package org.thesix.member.exception;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.thesix.member.util.ApiUtil.*;

@RestControllerAdvice
@Log4j2
public class ExceptionHandlers {


    private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        return newResponse(throwable.getMessage(), status);
    }

    private ResponseEntity<ApiResult<?>> newResponse(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(error(message, status), headers, status);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleUnauthorizedException(Exception e) {
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({
            NullPointerException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<?> handle500Exception(Exception e) {
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

