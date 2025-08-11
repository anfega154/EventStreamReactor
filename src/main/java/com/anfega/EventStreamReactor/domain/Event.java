package com.anfega.EventStreamReactor.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;

@Data
@Document(collection = "events")
public class Event implements Serializable {
    @Id
    private String id;
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    private String description;
    private Instant dateTime;
    @NotBlank(message = "La ubicación es obligatoria")
    private String location;
    @NotBlank(message = "La categoría es obligatoria")
    private String category;
    private Instant createdAt;
    private Instant updatedAt;
}
