package org.thesix.gatewayserver.filter;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class MemberFilter extends AbstractGatewayFilterFactory<MemberFilter.Config> {
    public MemberFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("GlobalFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                log.info("GlobalFilter Start>>>>>>" + exchange.getRequest());
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
