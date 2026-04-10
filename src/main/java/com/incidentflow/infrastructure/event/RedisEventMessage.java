package com.incidentflow.infrastructure.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record RedisEventMessage(
        @JsonProperty("event_id")
        UUID eventId,
        @JsonProperty("event_type")
        String eventType,
        @JsonProperty("service_id")
        UUID serviceId,
        @JsonProperty("incident_id")
        UUID incidentId,
        @JsonProperty("timestamp")
        Instant timestamp
) {
}
