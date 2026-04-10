package com.incidentflow.interfaces.rest.dto;

import com.incidentflow.domain.model.IncidentStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateIncidentStatusRequest(
        @NotNull IncidentStatus status
) {
}
