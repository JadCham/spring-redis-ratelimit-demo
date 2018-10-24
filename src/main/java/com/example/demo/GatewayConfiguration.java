package com.example.demo;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GatewayConfiguration {


    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes()
            .route("ratelimited_hello", route -> route
            .path("/hi")
                .filters(i -> i.requestRateLimiter(j -> j.setRateLimiter(rateLimiter())))
            .uri("http://localhost:8080/hello")
            ).build();
    }

    @Bean
    public MapReactiveUserDetailsService reactiveUserDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder()
            .username("test")
            .password(encoder.encode("pass"))
            .roles("USER")
            .build();
        return new MapReactiveUserDetailsService(user);
    }

    @Bean
    public RedisRateLimiter rateLimiter(){
        return new RedisRateLimiter(1,1);
    }


}
