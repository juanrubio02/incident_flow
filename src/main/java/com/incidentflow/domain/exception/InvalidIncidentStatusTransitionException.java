package com.incidentflow.domain.exception;

import com.incidentflow.domain.model.IncidentStatus;

import java.util.UUID;

public class InvalidIncidentStatusTransitionException extends DomainException {

    public InvalidIncidentStatusTransitionException(
            UUID incidentId,
            IncidentStatus currentStatus,
            IncidentStatus targetStatus
    ) {
        super("Incident " + incidentId + " cannot transition from " + currentStatus + " to " + targetStatus);
    }
}
