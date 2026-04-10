package com.incidentflow.infrastructure.persistence.mapper;

import com.incidentflow.domain.model.Service;
import com.incidentflow.infrastructure.persistence.entity.ServiceJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ServicePersistenceMapper {

    public ServiceJpaEntity toEntity(Service service) {
        return new ServiceJpaEntity(service.getId(), service.getName(), service.getStatus());
    }

    public Service toDomain(ServiceJpaEntity entity) {
        return new Service(entity.getId(), entity.getName(), entity.getStatus());
    }
}
