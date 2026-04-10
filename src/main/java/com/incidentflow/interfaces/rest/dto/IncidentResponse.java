package com.incidentflow.interfaces.rest.dto;

import com.incidentflow.domain.model.IncidentStatus;

import java.time.Instant;
import java.util.UUID;

public record IncidentResponse(
        UUID id,
        UUID serviceId,
        IncidentStatus status,
        Instant createdAt
) {
}
