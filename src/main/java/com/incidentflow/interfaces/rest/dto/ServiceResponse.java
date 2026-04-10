package com.incidentflow.interfaces.rest.dto;

import com.incidentflow.domain.model.ServiceStatus;

import java.util.UUID;

public record ServiceResponse(
        UUID id,
        String name,
        ServiceStatus status
) {
}
