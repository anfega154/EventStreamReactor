package com.anfega.EventStreamReactor.service;

import com.anfega.EventStreamReactor.domain.Event;
import com.anfega.EventStreamReactor.repositoy.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    StreamService streamService;

    public Flux<Event> findAll() {
        return eventRepository.findAll();
    }

    public Mono<Event> findById(String id) {
        return eventRepository.findById(id);
    }

    public Mono<Event> create(Event event) {
        event.setCreatedAt(Instant.now());
        event.setUpdatedAt(Instant.now());
        return eventRepository.save(event)
                .doOnSuccess(savedEvent ->
                        streamService.publish("Nuevo evento creado: " + savedEvent.getName())
                );
    }

    public Mono<Event> update(String id, Event event) {
        return eventRepository.findById(id)
                .flatMap(existingEvent -> {
                    existingEvent.setName(event.getName());
                    existingEvent.setDescription(event.getDescription());
                    existingEvent.setDateTime(event.getDateTime());
                    existingEvent.setLocation(event.getLocation());
                    existingEvent.setCategory(event.getCategory());
                    existingEvent.setUpdatedAt(Instant.now());
                    return eventRepository.save(existingEvent)
                            .doOnSuccess(updatedEvent ->
                                    streamService.publish("Evento actualizado: " + updatedEvent.getName())
                            );
                });
    }

    public Mono<Void> delete(String id) {
        return eventRepository.deleteById(id)
                .doOnSuccess(aVoid ->
                        streamService.publish("Evento eliminado con ID: " + id)
                );
    }
}
