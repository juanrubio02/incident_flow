package com.incidentflow.application.port.in.command;

import java.util.UUID;

public record CreateIncidentCommand(
        UUID serviceId
) {
}
