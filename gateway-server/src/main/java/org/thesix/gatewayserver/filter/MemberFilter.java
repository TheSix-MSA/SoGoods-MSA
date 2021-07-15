package org.thesix.gatewayserver.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class MemberFilter extends CustomFilter {
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("MemberFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                log.info("MemberFilter Start>>>>>>" + exchange.getRequest());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    log.info("MemberFilter End>>>>>>" + exchange.getResponse());
                }
            }));
        });
    }
}
