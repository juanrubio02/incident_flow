package com.incidentflow.domain.model;

import com.incidentflow.domain.exception.InvalidIncidentStatusTransitionException;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Incident {

    private final UUID id;
    private final UUID serviceId;
    private final IncidentStatus status;
    private final Instant createdAt;

    public Incident(UUID id, UUID serviceId, IncidentStatus status, Instant createdAt) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.serviceId = Objects.requireNonNull(serviceId, "serviceId must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public UUID getId() {
        return id;
    }

    public UUID getServiceId() {
        return serviceId;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static Incident open(UUID id, UUID serviceId, Instant createdAt) {
        return new Incident(id, serviceId, IncidentStatus.OPEN, createdAt);
    }

    public Incident startInvestigation() {
        if (status != IncidentStatus.OPEN) {
            throw new InvalidIncidentStatusTransitionException(id, status, IncidentStatus.INVESTIGATING);
        }
        return new Incident(id, serviceId, IncidentStatus.INVESTIGATING, createdAt);
    }

    public Incident resolve() {
        if (status != IncidentStatus.INVESTIGATING) {
            throw new InvalidIncidentStatusTransitionException(id, status, IncidentStatus.RESOLVED);
        }
        return new Incident(id, serviceId, IncidentStatus.RESOLVED, createdAt);
    }

    public Incident transitionTo(IncidentStatus targetStatus) {
        Objects.requireNonNull(targetStatus, "targetStatus must not be null");

        if (status == targetStatus) {
            return this;
        }

        return switch (targetStatus) {
            case OPEN -> throw new InvalidIncidentStatusTransitionException(id, status, targetStatus);
            case INVESTIGATING -> startInvestigation();
            case RESOLVED -> resolve();
        };
    }

    public boolean isOpen() {
        return status == IncidentStatus.OPEN;
    }

    public boolean isInvestigating() {
        return status == IncidentStatus.INVESTIGATING;
    }

    public boolean isActive() {
        return isOpen() || isInvestigating();
    }
}
