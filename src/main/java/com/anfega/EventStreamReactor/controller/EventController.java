package com.anfega.EventStreamReactor.controller;

import com.anfega.EventStreamReactor.domain.Event;
import com.anfega.EventStreamReactor.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public Flux<Event> getAll() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Event> getById(@PathVariable String id) {
        return eventService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Event> create(@Valid @RequestBody Event event) {
        return eventService.create(event);
    }

    @PutMapping("/{id}")
    public Mono<Event> update(@PathVariable String id, @Valid @RequestBody Event event) {
        return eventService.update(id, event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return eventService.delete(id);
    }
}
