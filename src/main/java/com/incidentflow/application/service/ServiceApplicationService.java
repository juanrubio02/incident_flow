package com.incidentflow.application.service;

import com.incidentflow.application.port.in.CreateServiceUseCase;
import com.incidentflow.application.port.in.ListServicesUseCase;
import com.incidentflow.application.port.in.UpdateServiceStatusUseCase;
import com.incidentflow.application.port.in.command.CreateServiceCommand;
import com.incidentflow.application.port.in.command.UpdateServiceStatusCommand;
import com.incidentflow.application.port.out.EventPublisher;
import com.incidentflow.application.exception.ServiceNotFoundException;
import com.incidentflow.domain.event.IncidentCreatedEvent;
import com.incidentflow.domain.event.IncidentResolvedEvent;
import com.incidentflow.domain.event.ServiceDownEvent;
import com.incidentflow.domain.event.ServiceUpEvent;
import com.incidentflow.domain.model.Incident;
import com.incidentflow.domain.model.Service;
import com.incidentflow.domain.repository.IncidentRepository;
import com.incidentflow.domain.repository.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
public class ServiceApplicationService implements CreateServiceUseCase, ListServicesUseCase, UpdateServiceStatusUseCase {

    private final ServiceRepository serviceRepository;
    private final IncidentRepository incidentRepository;
    private final EventPublisher eventPublisher;
    private final Clock clock;

    public ServiceApplicationService(
            ServiceRepository serviceRepository,
            IncidentRepository incidentRepository,
            EventPublisher eventPublisher,
            Clock clock
    ) {
        this.serviceRepository = serviceRepository;
        this.incidentRepository = incidentRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Service create(CreateServiceCommand command) {
        Service service = new Service(UUID.randomUUID(), command.name(), command.status());
        return serviceRepository.save(service);
    }

    @Override
    public List<Service> list() {
        return serviceRepository.findAll();
    }

    @Override
    @Transactional
    public Service updateStatus(UpdateServiceStatusCommand command) {
        Service currentService = serviceRepository.findById(command.serviceId())
                .orElseThrow(() -> new ServiceNotFoundException(command.serviceId()));

        Service updatedService = switch (command.status()) {
            case UP -> currentService.markUp();
            case DOWN -> currentService.markDown();
        };

        if (currentService.getStatus() == updatedService.getStatus()) {
            return currentService;
        }

        Service persistedService = serviceRepository.save(updatedService);

        if (currentService.isUp() && persistedService.isDown()) {
            Instant occurredAt = Instant.now(clock);
            eventPublisher.publish(new ServiceDownEvent(persistedService.getId(), occurredAt));

            Incident createdIncident = incidentRepository.save(Incident.open(
                    UUID.randomUUID(),
                    persistedService.getId(),
                    occurredAt
            ));
            eventPublisher.publish(new IncidentCreatedEvent(
                    createdIncident.getId(),
                    createdIncident.getServiceId(),
                    occurredAt
            ));
        }

        if (currentService.isDown() && persistedService.isUp()) {
            Instant occurredAt = Instant.now(clock);
            eventPublisher.publish(new ServiceUpEvent(persistedService.getId(), occurredAt));

            incidentRepository.findActiveByServiceId(persistedService.getId())
                    .stream()
                    .map(this::resolveForServiceRecovery)
                    .map(incidentRepository::save)
                    .forEach(incident -> eventPublisher.publish(new IncidentResolvedEvent(
                            incident.getId(),
                            incident.getServiceId(),
                            occurredAt
                    )));
        }

        return persistedService;
    }

    private Incident resolveForServiceRecovery(Incident incident) {
        if (incident.isOpen()) {
            return incident.startInvestigation().resolve();
        }
        return incident.resolve();
    }
}
