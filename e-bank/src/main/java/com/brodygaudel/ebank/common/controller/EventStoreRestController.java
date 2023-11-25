package com.brodygaudel.ebank.common.controller;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Stream;

@RestController
@RequestMapping("/commons/event-store")
public class EventStoreRestController {

    private final EventStore eventStore;

    public EventStoreRestController(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @GetMapping("/get/{id}")
    public Stream eventStore(@PathVariable String id){
        return eventStore.readEvents(id).asStream();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(@NotNull Exception exception){
        return new ResponseEntity<>(
                exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
