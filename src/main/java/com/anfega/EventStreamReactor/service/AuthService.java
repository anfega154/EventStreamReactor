package com.anfega.EventStreamReactor.service;

import com.anfega.EventStreamReactor.domain.User;
import com.anfega.EventStreamReactor.repositoy.UserRepository;
import com.anfega.EventStreamReactor.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Mono<User> signup(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new RuntimeException("Email ya registrado")))
                .switchIfEmpty(Mono.defer(() -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setRoles(List.of("ROLE_USER"));
                    user.setCreatedAt(Instant.now());
                    user.setUpdatedAt(Instant.now());
                    return userRepository.save(user);
                }));
    }

    public Mono<Map<String, String>> login(Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    if (passwordEncoder.matches(password, user.getPassword())) {
                        String token = jwtUtil.generateToken(user.getEmail(), user.getRoles());
                        return Mono.just(Map.of("token", token));
                    }
                    return Mono.error(new RuntimeException("Credenciales inválidas"));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Usuario no encontrado")));
    }
}

