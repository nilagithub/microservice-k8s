package com.example.demogateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

//@Configuration
public class CorsFilter {
	
	/*@Bean
    public CorsWebFilter  corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("*"); // Replace with your frontend origin(s)
        //corsConfig.addAllowedOrigin("https://your-frontend-domain.com");
        corsConfig.addAllowedMethod("*"); // Allow all methods
        corsConfig.addAllowedHeader("Authorization, Authorization-Client, Content-Type, Accept, Origin, x-tenantid,x-branch, X-Requested-With"); //* Allow all headers
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Apply to all paths

        return new CorsWebFilter (source);
    }*/
}
