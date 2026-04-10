package com.incidentflow.interfaces.rest;

import com.incidentflow.application.port.in.CreateIncidentUseCase;
import com.incidentflow.application.port.in.ListIncidentsUseCase;
import com.incidentflow.application.port.in.TransitionIncidentUseCase;
import com.incidentflow.interfaces.rest.dto.CreateIncidentRequest;
import com.incidentflow.interfaces.rest.dto.IncidentResponse;
import com.incidentflow.interfaces.rest.dto.UpdateIncidentStatusRequest;
import com.incidentflow.interfaces.rest.mapper.IncidentApiMapper;
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
@RequestMapping("/incidents")
public class IncidentController {

    private final CreateIncidentUseCase createIncidentUseCase;
    private final ListIncidentsUseCase listIncidentsUseCase;
    private final TransitionIncidentUseCase transitionIncidentUseCase;
    private final IncidentApiMapper incidentApiMapper;

    public IncidentController(
            CreateIncidentUseCase createIncidentUseCase,
            ListIncidentsUseCase listIncidentsUseCase,
            TransitionIncidentUseCase transitionIncidentUseCase,
            IncidentApiMapper incidentApiMapper
    ) {
        this.createIncidentUseCase = createIncidentUseCase;
        this.listIncidentsUseCase = listIncidentsUseCase;
        this.transitionIncidentUseCase = transitionIncidentUseCase;
        this.incidentApiMapper = incidentApiMapper;
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> create(@Valid @RequestBody CreateIncidentRequest request) {
        IncidentResponse response = incidentApiMapper.toResponse(
                createIncidentUseCase.create(incidentApiMapper.toCommand(request))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<IncidentResponse> list() {
        return listIncidentsUseCase.list()
                .stream()
                .map(incidentApiMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{id}/status")
    public IncidentResponse updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateIncidentStatusRequest request
    ) {
        return incidentApiMapper.toResponse(
                transitionIncidentUseCase.transition(incidentApiMapper.toCommand(id, request))
        );
    }
}
