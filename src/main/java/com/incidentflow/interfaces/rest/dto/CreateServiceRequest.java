package com.incidentflow.interfaces.rest.dto;

import com.incidentflow.domain.model.ServiceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateServiceRequest(
        @NotBlank String name,
        @NotNull ServiceStatus status
) {
}
