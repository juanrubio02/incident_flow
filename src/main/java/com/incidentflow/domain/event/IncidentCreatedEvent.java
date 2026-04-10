package com.incidentflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record IncidentCreatedEvent(
        UUID incidentId,
        UUID serviceId,
        Instant occurredAt
) implements DomainEvent {
}
