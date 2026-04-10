package com.incidentflow.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateIncidentRequest(
        @NotNull UUID serviceId
) {
}
