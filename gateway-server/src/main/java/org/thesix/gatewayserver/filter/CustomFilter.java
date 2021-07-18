package org.thesix.gatewayserver.filter;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.thesix.gatewayserver.config.JwtValidator;

import java.util.List;

@Component
@Log4j2
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    @Autowired
    private JwtValidator jwtValidator;

    public CustomFilter() {
        super(CustomFilter.Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return null;
    }


    /**
     * GET 요청이 아니면 JWT를 통해 권한 체크를 한다.
     * @param req ServerHttpRequest
     * @throws IllegalArgumentException 권한이 없다
     */
    public void checkAuthorization(ServerHttpRequest req) {
        String accessToken = req.getHeaders().get("Authorization").get(0);
        jwtValidator.validateToken(accessToken);
        List<String> roles = jwtValidator.extractAllClaims(accessToken).get("roles", List.class);
        if(!roles.contains("GENERAL")) { // 권한 체크
            throw new IllegalArgumentException("권한이 없다.");
        }
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
