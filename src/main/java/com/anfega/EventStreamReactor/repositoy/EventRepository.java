package com.anfega.EventStreamReactor.repositoy;

import com.anfega.EventStreamReactor.domain.Event;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import java.time.Instant;

public interface EventRepository extends ReactiveMongoRepository<Event, String> {
    Flux<Event> findByCategory(String category);
    Flux<Event> findByDateTimeAfter(Instant dateTime);
}