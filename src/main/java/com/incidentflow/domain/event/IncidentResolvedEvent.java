package com.incidentflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record IncidentResolvedEvent(
        UUID incidentId,
        UUID serviceId,
        Instant occurredAt
) implements DomainEvent {
}
