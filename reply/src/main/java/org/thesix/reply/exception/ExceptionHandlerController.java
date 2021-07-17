package org.thesix.reply.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.thesix.reply.common.util.ApiUtil.ApiResult;
import static org.thesix.reply.common.util.ApiUtil.error;

@RestControllerAdvice
public class ExceptionHandlerController {

    private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        return newResponse(throwable.getMessage(), status);
    }

    private ResponseEntity<ApiResult<?>> newResponse(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(error(message, status), headers, status);
    }

    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<?> internalError(Exception e) {
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeException(Exception e) {
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
