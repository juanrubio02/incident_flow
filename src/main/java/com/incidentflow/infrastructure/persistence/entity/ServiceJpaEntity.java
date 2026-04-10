package com.incidentflow.infrastructure.persistence.entity;

import com.incidentflow.domain.model.ServiceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "services")
public class ServiceJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ServiceStatus status;

    protected ServiceJpaEntity() {
    }

    public ServiceJpaEntity(UUID id, String name, ServiceStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
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
}
