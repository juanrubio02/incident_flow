package com.incidentflow.interfaces.rest.mapper;

import com.incidentflow.application.port.in.command.CreateIncidentCommand;
import com.incidentflow.application.port.in.command.TransitionIncidentCommand;
import com.incidentflow.domain.model.Incident;
import com.incidentflow.interfaces.rest.dto.CreateIncidentRequest;
import com.incidentflow.interfaces.rest.dto.IncidentResponse;
import com.incidentflow.interfaces.rest.dto.UpdateIncidentStatusRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IncidentApiMapper {

    public CreateIncidentCommand toCommand(CreateIncidentRequest request) {
        return new CreateIncidentCommand(request.serviceId());
    }

    public TransitionIncidentCommand toCommand(UUID incidentId, UpdateIncidentStatusRequest request) {
        return new TransitionIncidentCommand(incidentId, request.status());
    }

    public IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getServiceId(),
                incident.getStatus(),
                incident.getCreatedAt()
        );
    }
}
