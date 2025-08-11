package com.anfega.EventStreamReactor.service;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

@Service
public class StreamService {

    private final Sinks.Many<String> sink;

    public StreamService() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(String message) {
        sink.tryEmitNext(message);
    }

    public Flux<String> streamChanges() {
        return sink.asFlux();
    }

    public Flux<ServerSentEvent<String>> streamSimulatedChanges() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(seq -> ServerSentEvent.<String>builder()
                        .id(UUID.randomUUID().toString())
                        .event("update")
                        .data("Cambio detectado a las " + LocalTime.now())
                        .build()
                );
    }
}
