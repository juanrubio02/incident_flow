package com.incidentflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ServiceDownEvent(
        UUID serviceId,
        Instant occurredAt
) implements DomainEvent {
}
