package com.eventmaster.backend.events.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record EventCreatedEvent(
    @NotNull UUID eventId,
    @NotNull String organizerId,
    @NotBlank String title,
    String description,
    @NotNull Instant eventDate
) {}
