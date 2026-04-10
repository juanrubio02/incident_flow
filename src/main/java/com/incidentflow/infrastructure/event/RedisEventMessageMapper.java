package com.incidentflow.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incidentflow.domain.event.DomainEvent;
import com.incidentflow.domain.event.IncidentCreatedEvent;
import com.incidentflow.domain.event.IncidentResolvedEvent;
import com.incidentflow.domain.event.ServiceDownEvent;
import com.incidentflow.domain.event.ServiceUpEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class RedisEventMessageMapper {

    private final ObjectMapper objectMapper;

    public RedisEventMessageMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(toMessage(event));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize domain event", exception);
        }
    }

    public RedisEventMessage fromJson(String json) {
        try {
            return objectMapper.readValue(json, RedisEventMessage.class);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to deserialize redis event message", exception);
        }
    }

    public RedisEventMessage extractMetadata(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            return new RedisEventMessage(
                    asUuid(root.get("event_id")),
                    asText(root.get("event_type")),
                    asUuid(root.get("service_id")),
                    asUuid(root.get("incident_id")),
                    asInstant(root.get("timestamp"))
            );
        } catch (JsonProcessingException exception) {
            return new RedisEventMessage(null, "unknown", null, null, null);
        }
    }

    private RedisEventMessage toMessage(DomainEvent event) {
        if (event instanceof ServiceDownEvent serviceDownEvent) {
            return new RedisEventMessage(
                    UUID.randomUUID(),
                    ServiceDownEvent.class.getSimpleName(),
                    serviceDownEvent.serviceId(),
                    null,
                    serviceDownEvent.occurredAt()
            );
        }

        if (event instanceof ServiceUpEvent serviceUpEvent) {
            return new RedisEventMessage(
                    UUID.randomUUID(),
                    ServiceUpEvent.class.getSimpleName(),
                    serviceUpEvent.serviceId(),
                    null,
                    serviceUpEvent.occurredAt()
            );
        }

        if (event instanceof IncidentCreatedEvent incidentCreatedEvent) {
            return new RedisEventMessage(
                    UUID.randomUUID(),
                    IncidentCreatedEvent.class.getSimpleName(),
                    incidentCreatedEvent.serviceId(),
                    incidentCreatedEvent.incidentId(),
                    incidentCreatedEvent.occurredAt()
            );
        }

        if (event instanceof IncidentResolvedEvent incidentResolvedEvent) {
            return new RedisEventMessage(
                    UUID.randomUUID(),
                    IncidentResolvedEvent.class.getSimpleName(),
                    incidentResolvedEvent.serviceId(),
                    incidentResolvedEvent.incidentId(),
                    incidentResolvedEvent.occurredAt()
            );
        }

        throw new IllegalArgumentException("Unsupported domain event type: " + event.getClass().getName());
    }

    private String asText(JsonNode node) {
        return node == null || node.isNull() ? null : node.asText();
    }

    private UUID asUuid(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        try {
            return UUID.fromString(node.asText());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private Instant asInstant(JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }

        try {
            return Instant.parse(node.asText());
        } catch (Exception exception) {
            return null;
        }
    }
}
