package com.anfega.EventStreamReactor.controller;

import com.anfega.EventStreamReactor.domain.User;
import com.anfega.EventStreamReactor.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> signup(@RequestBody User user) {
        return authService.signup(user)
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @PostMapping("/login")
    public Mono<Map<String, String>> login(@RequestBody Map<String, String> body) {
        return authService.login(body)
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }
}
