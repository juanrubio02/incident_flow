package com.incidentflow.interfaces.rest.dto;

import com.incidentflow.domain.model.ServiceStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateServiceStatusRequest(
        @NotNull ServiceStatus status
) {
}
