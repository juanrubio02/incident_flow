package com.incidentflow.application.port.in.command;

import com.incidentflow.domain.model.ServiceStatus;

public record CreateServiceCommand(
        String name,
        ServiceStatus status
) {
}
