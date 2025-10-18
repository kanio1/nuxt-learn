package com.eventmaster.backend.events.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    private UUID id;
    private String title;
    private String description;
    private Instant eventDate;
    private String organizerId;
}
