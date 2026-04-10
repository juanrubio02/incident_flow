package com.incidentflow.application.port.in;

import com.incidentflow.domain.model.Incident;

import java.util.List;

public interface ListIncidentsUseCase {

    List<Incident> list();
}
