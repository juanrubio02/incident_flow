package com.incidentflow.domain.repository;

import com.incidentflow.domain.model.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository {

    Service save(Service service);

    List<Service> findAll();

    Optional<Service> findById(UUID id);
}
