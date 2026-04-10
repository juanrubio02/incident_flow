package com.incidentflow.application.service;

import com.incidentflow.application.exception.IncidentNotFoundException;
import com.incidentflow.application.exception.ServiceNotFoundException;
import com.incidentflow.application.port.in.CreateIncidentUseCase;
import com.incidentflow.application.port.in.ListIncidentsUseCase;
import com.incidentflow.application.port.in.TransitionIncidentUseCase;
import com.incidentflow.application.port.in.command.CreateIncidentCommand;
import com.incidentflow.application.port.in.command.TransitionIncidentCommand;
import com.incidentflow.application.port.out.EventPublisher;
import com.incidentflow.domain.event.IncidentCreatedEvent;
import com.incidentflow.domain.event.IncidentResolvedEvent;
import com.incidentflow.domain.model.IncidentStatus;
import com.incidentflow.domain.model.Incident;
import com.incidentflow.domain.repository.IncidentRepository;
import com.incidentflow.domain.repository.ServiceRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Transactional(readOnly = true)
public class IncidentApplicationService implements CreateIncidentUseCase, ListIncidentsUseCase, TransitionIncidentUseCase {

    private final IncidentRepository incidentRepository;
    private final ServiceRepository serviceRepository;
    private final EventPublisher eventPublisher;
    private final Clock clock;

    public IncidentApplicationService(
            IncidentRepository incidentRepository,
            ServiceRepository serviceRepository,
            EventPublisher eventPublisher,
            Clock clock
    ) {
        this.incidentRepository = incidentRepository;
        this.serviceRepository = serviceRepository;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

    @Override
    @Transactional
    public Incident create(CreateIncidentCommand command) {
        serviceRepository.findById(command.serviceId())
                .orElseThrow(() -> new ServiceNotFoundException(command.serviceId()));

        Instant occurredAt = Instant.now(clock);
        Incident incident = Incident.open(UUID.randomUUID(), command.serviceId(), occurredAt);

        Incident persistedIncident = incidentRepository.save(incident);
        eventPublisher.publish(new IncidentCreatedEvent(
                persistedIncident.getId(),
                persistedIncident.getServiceId(),
                occurredAt
        ));
        return persistedIncident;
    }

    @Override
    public List<Incident> list() {
        return incidentRepository.findAll();
    }

    @Override
    @Transactional
    public Incident transition(TransitionIncidentCommand command) {
        Incident currentIncident = incidentRepository.findById(command.incidentId())
                .orElseThrow(() -> new IncidentNotFoundException(command.incidentId()));

        Incident transitionedIncident = currentIncident.transitionTo(command.status());

        if (currentIncident.getStatus() == transitionedIncident.getStatus()) {
            return currentIncident;
        }

        Incident persistedIncident = incidentRepository.save(transitionedIncident);

        if (persistedIncident.getStatus() == IncidentStatus.RESOLVED) {
            eventPublisher.publish(new IncidentResolvedEvent(
                    persistedIncident.getId(),
                    persistedIncident.getServiceId(),
                    Instant.now(clock)
            ));
        }

        return persistedIncident;
    }
}
