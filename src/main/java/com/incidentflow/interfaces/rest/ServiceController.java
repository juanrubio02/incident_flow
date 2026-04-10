package com.incidentflow.interfaces.rest;

import com.incidentflow.application.port.in.CreateServiceUseCase;
import com.incidentflow.application.port.in.ListServicesUseCase;
import com.incidentflow.application.port.in.UpdateServiceStatusUseCase;
import com.incidentflow.interfaces.rest.dto.CreateServiceRequest;
import com.incidentflow.interfaces.rest.dto.ServiceResponse;
import com.incidentflow.interfaces.rest.dto.UpdateServiceStatusRequest;
import com.incidentflow.interfaces.rest.mapper.ServiceApiMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final CreateServiceUseCase createServiceUseCase;
    private final ListServicesUseCase listServicesUseCase;
    private final UpdateServiceStatusUseCase updateServiceStatusUseCase;
    private final ServiceApiMapper serviceApiMapper;

    public ServiceController(
            CreateServiceUseCase createServiceUseCase,
            ListServicesUseCase listServicesUseCase,
            UpdateServiceStatusUseCase updateServiceStatusUseCase,
            ServiceApiMapper serviceApiMapper
    ) {
        this.createServiceUseCase = createServiceUseCase;
        this.listServicesUseCase = listServicesUseCase;
        this.updateServiceStatusUseCase = updateServiceStatusUseCase;
        this.serviceApiMapper = serviceApiMapper;
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> create(@Valid @RequestBody CreateServiceRequest request) {
        ServiceResponse response = serviceApiMapper.toResponse(
                createServiceUseCase.create(serviceApiMapper.toCommand(request))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ServiceResponse> list() {
        return listServicesUseCase.list()
                .stream()
                .map(serviceApiMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{id}/status")
    public ServiceResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateServiceStatusRequest request
    ) {
        return serviceApiMapper.toResponse(
                updateServiceStatusUseCase.updateStatus(serviceApiMapper.toCommand(id, request))
        );
    }
}
