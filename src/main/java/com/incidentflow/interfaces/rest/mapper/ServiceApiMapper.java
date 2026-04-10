package com.incidentflow.interfaces.rest.mapper;

import com.incidentflow.application.port.in.command.CreateServiceCommand;
import com.incidentflow.application.port.in.command.UpdateServiceStatusCommand;
import com.incidentflow.domain.model.Service;
import com.incidentflow.interfaces.rest.dto.CreateServiceRequest;
import com.incidentflow.interfaces.rest.dto.ServiceResponse;
import com.incidentflow.interfaces.rest.dto.UpdateServiceStatusRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ServiceApiMapper {

    public CreateServiceCommand toCommand(CreateServiceRequest request) {
        return new CreateServiceCommand(request.name(), request.status());
    }

    public UpdateServiceStatusCommand toCommand(UUID serviceId, UpdateServiceStatusRequest request) {
        return new UpdateServiceStatusCommand(serviceId, request.status());
    }

    public ServiceResponse toResponse(Service service) {
        return new ServiceResponse(service.getId(), service.getName(), service.getStatus());
    }
}
