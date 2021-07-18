package org.thesix.gatewayserver.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@Log4j2
public class GlobalFilter extends CustomFilter {

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("GlobalFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                log.info("GlobalFilter Start>>>>>>" + exchange.getRequest());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("GlobalFilter End Status>>>>>>" + exchange.getResponse().getStatusCode());
//                    log.info("GlobalFilter End>>>>>>" + exchange.getResponse());
                }
            }));
        });
    }

    //헤더에 무언가 실어서 다음 요청으로 보내는법
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
