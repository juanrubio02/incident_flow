package com.incidentflow.infrastructure.persistence.mapper;

import com.incidentflow.domain.model.Incident;
import com.incidentflow.infrastructure.persistence.entity.IncidentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class IncidentPersistenceMapper {

    public IncidentJpaEntity toEntity(Incident incident) {
        return new IncidentJpaEntity(
                incident.getId(),
                incident.getServiceId(),
                incident.getStatus(),
                incident.getCreatedAt()
        );
    }

    public Incident toDomain(IncidentJpaEntity entity) {
        return new Incident(
                entity.getId(),
                entity.getServiceId(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
