package com.incidentflow.application.port.in;

import com.incidentflow.application.port.in.command.CreateIncidentCommand;
import com.incidentflow.domain.model.Incident;

public interface CreateIncidentUseCase {

    Incident create(CreateIncidentCommand command);
}
