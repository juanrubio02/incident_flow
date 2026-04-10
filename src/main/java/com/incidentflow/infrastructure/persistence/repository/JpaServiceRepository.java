package com.incidentflow.infrastructure.persistence.repository;

import com.incidentflow.domain.model.Service;
import com.incidentflow.infrastructure.persistence.mapper.ServicePersistenceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaServiceRepository implements com.incidentflow.domain.repository.ServiceRepository {

    private final SpringDataServiceRepository springDataServiceRepository;
    private final ServicePersistenceMapper servicePersistenceMapper;

    public JpaServiceRepository(
            SpringDataServiceRepository springDataServiceRepository,
            ServicePersistenceMapper servicePersistenceMapper
    ) {
        this.springDataServiceRepository = springDataServiceRepository;
        this.servicePersistenceMapper = servicePersistenceMapper;
    }

    @Override
    public Service save(Service service) {
        return servicePersistenceMapper.toDomain(
                springDataServiceRepository.save(servicePersistenceMapper.toEntity(service))
        );
    }

    @Override
    public List<Service> findAll() {
        return springDataServiceRepository.findAll()
                .stream()
                .map(servicePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Service> findById(UUID id) {
        return springDataServiceRepository.findById(id)
                .map(servicePersistenceMapper::toDomain);
    }
}
