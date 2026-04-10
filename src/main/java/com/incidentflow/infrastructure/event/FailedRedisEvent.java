package com.incidentflow.infrastructure.event;

import java.time.Instant;
import java.util.UUID;

public record FailedRedisEvent(
        UUID eventId,
        String eventType,
        UUID serviceId,
        UUID incidentId,
        Instant eventTimestamp,
        String consumerName,
        String rawMessage,
        String errorMessage,
        int retries,
        Instant failedAt
) {
}
