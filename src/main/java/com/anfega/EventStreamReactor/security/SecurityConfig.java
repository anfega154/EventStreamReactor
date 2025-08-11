package com.anfega.EventStreamReactor.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final ReactiveUserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtUtil jwtUtil, ReactiveUserDetailsServiceImpl uds) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new JwtReactiveAuthenticationManager(jwtUtil);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        jwtFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter());
        jwtFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .authorizeExchange(authorize -> authorize
                        // Endpoints públicos
                        .pathMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/**").permitAll()
                        // Endpoints protegidos por JWT
                        .pathMatchers("/api/stream/**").authenticated()
                        .pathMatchers("/api/events/**").authenticated()
                        .pathMatchers("/api/users/**").authenticated()
                        // Solo ADMIN
                        .pathMatchers("/api/admin/**").hasRole("ADMIN")
                        // Resto
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
