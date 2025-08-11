package com.anfega.EventStreamReactor.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public JwtReactiveAuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = (String) authentication.getCredentials();
        if (token == null || !jwtUtil.validateToken(token)) {
            return Mono.empty();
        }
        String username = jwtUtil.getUsername(token);
        var roles = jwtUtil.getRoles(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        Authentication auth = new UsernamePasswordAuthenticationToken(username, token, roles);
        return Mono.just(auth);
    }
}
