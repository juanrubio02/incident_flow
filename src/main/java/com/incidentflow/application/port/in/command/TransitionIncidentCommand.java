package com.incidentflow.application.port.in.command;

import com.incidentflow.domain.model.IncidentStatus;

import java.util.UUID;

public record TransitionIncidentCommand(
        UUID incidentId,
        IncidentStatus status
) {
}
