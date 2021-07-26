package org.thesix.gatewayserver.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class BoardFilter extends CustomFilter {
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("BoardFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                ServerHttpRequest req = exchange.getRequest();
                log.info("BoardFilter Start>>>>>>" + req);
                if(!req.getMethod().equals(HttpMethod.GET)) {
                    checkAuthorization(req);
                }
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
//                    log.info("BoardFilter End>>>>>>" + exchange.getResponse());
                    log.info("BoardFilter End Status>>>>>>" + exchange.getResponse().getStatusCode());
                }
            }));
        });
    }
}
