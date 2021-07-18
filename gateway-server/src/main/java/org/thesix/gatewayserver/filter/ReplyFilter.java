package org.thesix.gatewayserver.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@Log4j2
public class ReplyFilter extends CustomFilter {

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            log.info("ReplyFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                //전처리
                ServerHttpRequest req = exchange.getRequest();
                log.info("ReplyFilter Start>>>>>>" + req);
                if(!req.getMethod().equals(HttpMethod.GET)) {
                    checkAuthorization(req);
                }
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    //후처리
//                    log.info("ReplyFilter End>>>>>>" + exchange.getResponse());
                    log.info("ReplyFilter End Status>>>>>>" + exchange.getResponse().getStatusCode());
                }
            }));
        });
    }
    

}
