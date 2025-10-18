package com.eventmaster.backend.events;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class Event {
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
