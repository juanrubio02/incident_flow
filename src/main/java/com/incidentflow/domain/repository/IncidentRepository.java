package com.incidentflow.domain.repository;

import com.incidentflow.domain.model.Incident;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncidentRepository {

    Incident save(Incident incident);

    List<Incident> findAll();

    Optional<Incident> findById(UUID id);

    List<Incident> findActiveByServiceId(UUID serviceId);
}
