package com.incidentflow.application.port.in.command;

import com.incidentflow.domain.model.ServiceStatus;

import java.util.UUID;

public record UpdateServiceStatusCommand(
        UUID serviceId,
        ServiceStatus status
) {
}
