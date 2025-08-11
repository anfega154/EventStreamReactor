package com.anfega.EventStreamReactor.repositoy;



import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import com.anfega.EventStreamReactor.domain.User;


public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByEmail(String email);
}