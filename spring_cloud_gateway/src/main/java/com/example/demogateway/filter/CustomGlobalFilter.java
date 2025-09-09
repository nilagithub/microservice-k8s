package com.example.demogateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

	final Logger logger = LoggerFactory.getLogger(CustomGlobalFilter.class);
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		System.out.println("--------CustomGlobalFilter----------");
		logger.debug("****inside CustomGlobalFilter debug**********");
		logger.info("****inside CustomGlobalFilter info**********");
		
		ServerHttpRequest request = exchange.getRequest().mutate().header("Test-Header", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9").build();
		return chain.filter(exchange.mutate().request(request).build());
	}

	@Override
	public int getOrder() {
		return -1; // Set a high priority for this filter
	}
}
