package com.anfega.EventStreamReactor.controller;

import com.anfega.EventStreamReactor.service.StreamService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/stream")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping(value = "/changes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_USER')")
    public Flux<ServerSentEvent<String>> streamChanges() {
        return streamService.streamChanges()
                .map(data -> ServerSentEvent.builder(data).event("update").build());
    }
}
