package org.thesix.gatewayserver.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.thesix.gatewayserver.config.JwtValidator;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class ReplyFilter extends CustomFilter {

    private final JwtValidator jwtValidator;

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("ReplyFilter baseMessage>>>>>>" + config.getBaseMessage());
            /**
             * 고민중
             */
            if (config.isPreLogger()) {
                log.info("ReplyFilter Start>>>>>>" + exchange.getRequest());
                HttpMethod method = exchange.getRequest().getMethod();
                if(!method.equals(HttpMethod.GET)) {
                  String accessToken = exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
                  List<String> roles = jwtValidator.extractAllClaims(accessToken).get("roles", List.class);
                  jwtValidator.validateToken(accessToken);
                  log.info("권한이 없다.");
                  throw new RuntimeException("hi");
                }

            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("ReplyFilter End>>>>>>" + exchange.getResponse());
                }
            }));
        });
    }
//    @Override
//    public GatewayFilter apply(Config config) {
//        return ((exchange, chain) -> {
//            log.info("GlobalFilter baseMessage>>>>>>" + config.getBaseMessage());
//            if (config.isPreLogger()) {
//                log.info("GlobalFilter Start>>>>>>" + exchange.getRequest());
//                ServerHttpRequest request = exchange.getRequest().mutate().header("roles", "").build();
//                return chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable(()->{
//                    if (config.isPostLogger()) {
//                        log.info("GlobalFilter End>>>>>>" + exchange.getResponse());
//                    }
//                }));
//            }
//            return chain.filter(exchange);
//        });
//    }
}
