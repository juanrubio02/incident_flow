package com.incidentflow.domain.model;

import java.util.Objects;
import java.util.UUID;

public final class Service {

    private final UUID id;
    private final String name;
    private final ServiceStatus status;

    public Service(UUID id, String name, ServiceStatus status) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = validateName(name);
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public Service markDown() {
        if (status == ServiceStatus.DOWN) {
            return this;
        }
        return new Service(id, name, ServiceStatus.DOWN);
    }

    public Service markUp() {
        if (status == ServiceStatus.UP) {
            return this;
        }
        return new Service(id, name, ServiceStatus.UP);
    }

    public boolean isUp() {
        return status == ServiceStatus.UP;
    }

    public boolean isDown() {
        return status == ServiceStatus.DOWN;
    }

    private static String validateName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("service name must not be blank");
        }
        return value;
    }
}
