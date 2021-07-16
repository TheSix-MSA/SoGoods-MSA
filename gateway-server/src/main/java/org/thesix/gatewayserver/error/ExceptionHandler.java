package org.thesix.gatewayserver.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import static org.thesix.gatewayserver.util.ApiUtil.ApiResult;
import static org.thesix.gatewayserver.util.ApiUtil.error;

@RestControllerAdvice
@Log4j2
public class ExceptionHandler {

    private ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
        return newResponse(throwable.getMessage(), status);
    }

    private ResponseEntity<ApiResult<?>> newResponse(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(error(message, status), headers, status);
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<?> handleUnauthorizedException(Exception e) {
//        return newResponse(e, HttpStatus.BAD_REQUEST);
//    }

    // 서비스에서 리턴은 optional로 들어왔다.
//    @GetMapping("/hi")
//    public ApiResult<?> hi() {
//        return success(
//                hiService.good().orElseThrow(() -> new IllegalArgumentException("매개변수 오류"))
//        );
}

