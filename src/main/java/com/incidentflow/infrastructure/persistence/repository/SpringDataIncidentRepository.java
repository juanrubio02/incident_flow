package com.incidentflow.infrastructure.persistence.repository;

import com.incidentflow.domain.model.IncidentStatus;
import com.incidentflow.infrastructure.persistence.entity.IncidentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface SpringDataIncidentRepository extends JpaRepository<IncidentJpaEntity, UUID> {

    List<IncidentJpaEntity> findByServiceIdAndStatusIn(UUID serviceId, Collection<IncidentStatus> statuses);
}
