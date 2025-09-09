package com.example.demogateway.ratelimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.example.demogateway.ratelimiter.service.RateLimiterService;
import com.example.demogateway.ratelimiter.service.TokenBucketRateLimiter;

@Component
public class CustomRateLimiterGatewayFilterFactory extends AbstractGatewayFilterFactory<CustomRateLimiterGatewayFilterFactory.Config>{	
	public static class Config {
        private String routeId;
        public String getRouteId() { return routeId; }
        public void setRouteId(String routeId) { this.routeId = routeId; }
    }

	private final Map<String, RateLimiterService> rateLimiters = new ConcurrentHashMap<>();

    public CustomRateLimiterGatewayFilterFactory() {
        super(Config.class);
        rateLimiters.put("ACCOUNT-SERVICE", new TokenBucketRateLimiter(5, 5, 60)); // 5 tokens i.e requests per 60 seconds allowed
        //rateLimiters.put("AUTH_SERVICE_GLOBAL", new TokenBucketRateLimiter(5, 5, 60));
        //rateLimiters.put("BACKEND_SERVICE", new TokenBucketRateLimiter(5, 5, 60));
        //rateLimiters.put("route2", new LeakyBucketRateLimiter(10, 1));
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String routeId = config.getRouteId();
            
            System.out.println("routeId>>>>>>>>>>"+routeId);
            RateLimiterService rateLimiter = rateLimiters.get(routeId);
            if (rateLimiter == null || rateLimiter.isAllowed(getClientKey(exchange))) {
                return chain.filter(exchange);
            } else {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }
        };
    }

    private String getClientKey(ServerWebExchange exchange) {
        return exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
    }
}
