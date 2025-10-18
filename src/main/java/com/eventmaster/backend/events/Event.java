package com.eventmaster.backend.events;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "events")
public class Event {

    @Id
    private UUID id;
    private String title;
    private String description;
    private Instant eventDate;
    private String organizerId;
}
