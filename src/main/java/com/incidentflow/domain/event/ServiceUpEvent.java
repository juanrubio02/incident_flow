package com.incidentflow.domain.event;

import java.time.Instant;
import java.util.UUID;

public record ServiceUpEvent(
        UUID serviceId,
        Instant occurredAt
) implements DomainEvent {
}
