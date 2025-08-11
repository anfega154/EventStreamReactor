package com.anfega.EventStreamReactor.service;

import com.anfega.EventStreamReactor.domain.User;
import com.anfega.EventStreamReactor.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StreamService streamService;

    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    public Mono<User> getById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> create(User user) {
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user)
                .doOnSuccess(saved ->
                        streamService.publish("Nuevo usuario creado: " + saved.getName())
                );
    }

    public Mono<User> update(String id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setPassword(user.getPassword());
                    existingUser.setUpdatedAt(Instant.now());
                    return userRepository.save(existingUser)
                            .doOnSuccess(updated ->
                                    streamService.publish("Nuevo usuario actualizado: " + updated.getName())
                            );
                });
    }

    public Mono<Void> delete(String id) {
        return userRepository.deleteById(id)
                .doOnSuccess(aVoid ->
                        streamService.publish("Usuario eliminado con ID: " + id)
                );
    }

}
