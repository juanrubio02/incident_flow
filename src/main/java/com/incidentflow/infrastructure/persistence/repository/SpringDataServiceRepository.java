package com.incidentflow.infrastructure.persistence.repository;

import com.incidentflow.infrastructure.persistence.entity.ServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataServiceRepository extends JpaRepository<ServiceJpaEntity, UUID> {
}
