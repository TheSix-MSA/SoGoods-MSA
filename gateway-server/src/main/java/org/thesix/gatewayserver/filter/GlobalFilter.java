package org.thesix.gatewayserver.filter;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.thesix.gatewayserver.config.GatewayExceptionHandler;
import org.thesix.gatewayserver.config.JwtValidator;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {


    @Bean
    public ErrorWebExceptionHandler exceptionHandler() {
        return new GatewayExceptionHandler();
    }

    @Autowired
    private JwtValidator jwtValidator;

    public GlobalFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("GlobalFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                log.info("GlobalFilter Start>>>>>>" + exchange.getRequest());
                String accessToken = exchange.getRequest().getHeaders().get("Authorization").get(0).substring(7);
                jwtValidator.validateToken(accessToken);
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("GlobalFilter End>>>>>>" + exchange.getResponse());
                }
            }));
        });
    }

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
