package org.thesix.gatewayserver.filter;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.thesix.gatewayserver.config.JwtValidator;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Log4j2
public class ReplyFilter extends AbstractGatewayFilterFactory<ReplyFilter.Config> {
    public ReplyFilter() {
        super(Config.class);
    }

    @Autowired
    private JwtValidator jwtValidator;

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("GlobalFilter baseMessage>>>>>>" + config.getBaseMessage());


            /**
             *
             * 고민중
             */
            if (config.isPreLogger()) {
                log.info("GlobalFilter Start>>>>>>" + exchange.getRequest());
                String accessToken = exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
                jwtValidator.validateToken(accessToken);
                List<String> roles = jwtValidator.extractAllClaims(accessToken).get("roles", List.class);
                HttpMethod method = exchange.getRequest().getMethod();
//                if(!method.equals(HttpMethod.GET) && !roles.contains("GENERAL")) {
//                    log.info("권한이 없다.");
//                    exchange.getResponse().setComplete();
//                }

            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("GlobalFilter End>>>>>>" + exchange.getResponse());
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

    /**
     * application.yml에 선언한 각 filter의 args(인자값) 사용을 위한 클래스
     */
    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
