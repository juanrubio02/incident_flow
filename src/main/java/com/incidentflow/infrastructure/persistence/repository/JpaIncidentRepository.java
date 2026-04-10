package com.incidentflow.infrastructure.persistence.repository;

import com.incidentflow.domain.model.IncidentStatus;
import com.incidentflow.domain.model.Incident;
import com.incidentflow.infrastructure.persistence.mapper.IncidentPersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaIncidentRepository implements com.incidentflow.domain.repository.IncidentRepository {

    private static final List<IncidentStatus> ACTIVE_STATUSES = List.of(
            IncidentStatus.OPEN,
            IncidentStatus.INVESTIGATING
    );

    private final SpringDataIncidentRepository springDataIncidentRepository;
    private final IncidentPersistenceMapper incidentPersistenceMapper;

    public JpaIncidentRepository(
            SpringDataIncidentRepository springDataIncidentRepository,
            IncidentPersistenceMapper incidentPersistenceMapper
    ) {
        this.springDataIncidentRepository = springDataIncidentRepository;
        this.incidentPersistenceMapper = incidentPersistenceMapper;
    }

    @Override
    public Incident save(Incident incident) {
        return incidentPersistenceMapper.toDomain(
                springDataIncidentRepository.save(incidentPersistenceMapper.toEntity(incident))
        );
    }

    @Override
    public List<Incident> findAll() {
        return springDataIncidentRepository.findAll()
                .stream()
                .map(incidentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Incident> findById(UUID id) {
        return springDataIncidentRepository.findById(id)
                .map(incidentPersistenceMapper::toDomain);
    }

    @Override
    public List<Incident> findActiveByServiceId(UUID serviceId) {
        return springDataIncidentRepository.findByServiceIdAndStatusIn(serviceId, ACTIVE_STATUSES)
                .stream()
                .map(incidentPersistenceMapper::toDomain)
                .toList();
    }
}
