package org.thesix.gatewayserver.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class FundingFilter extends CustomFilter {
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("FundingFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                ServerHttpRequest req = exchange.getRequest();
                log.info("FundingFilter Start>>>>>>" + req);
                if(!req.getMethod().equals(HttpMethod.GET)) {
                    checkAuthorization(req);
                }
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("FundingFilter End Status>>>>>>" + exchange.getResponse().getStatusCode());
//                    log.info("FundingFilter End>>>>>>" + exchange.getResponse());
                }
            }));
        });
    }
}
