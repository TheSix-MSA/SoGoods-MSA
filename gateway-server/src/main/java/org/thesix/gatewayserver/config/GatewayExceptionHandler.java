package org.thesix.gatewayserver.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Log4j2
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {
    private String errorJSON(String msg) {
        return "{\"success\":"+false+",\"response\":"+null+",\"error\":\""+msg+"\"}";
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable throwable) {
        log.warn("GATEWAY EXCEPTION : " + throwable);
        Class<? extends Throwable> ex = throwable.getClass();
        ServerHttpResponse res = exchange.getResponse();
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
        String errorMsg = "로그인 해주세요";
        if (ex == ExpiredJwtException.class) {
            //리프레시 토큰 적용해야함
            errorMsg = "Refresh";
            res.setStatusCode(HttpStatus.UNAUTHORIZED);
        } else if (ex == IllegalArgumentException.class) {
            //권한이 없을시
            errorMsg = "권한이 없습니다";
            res.setStatusCode(HttpStatus.FORBIDDEN);
        }

//        if (ex == NullPointerException.class) {
//            res.setStatusCode(HttpStatus.UNAUTHORIZED);
//        } else if (ex == MalformedJwtException.class || ex == SignatureException.class || ex == UnsupportedJwtException.class) {
//            res.setStatusCode(HttpStatus.UNAUTHORIZED);
//        } else if (ex == IllegalArgumentException.class) {
//            res.setStatusCode(HttpStatus.UNAUTHORIZED);
//        } else if (ex == RuntimeException.class) {
//            res.setStatusCode(HttpStatus.UNAUTHORIZED);
//        } else if (ex == StringIndexOutOfBoundsException.class) {
//            res.setStatusCode(HttpStatus.UNAUTHORIZED);

        res.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        byte[] bytes = errorJSON(errorMsg).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return res.writeWith(Flux.just(buffer));
    }
}
