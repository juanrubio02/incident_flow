package com.incidentflow.application.port.in;

import com.incidentflow.application.port.in.command.TransitionIncidentCommand;
import com.incidentflow.domain.model.Incident;

public interface TransitionIncidentUseCase {

    Incident transition(TransitionIncidentCommand command);
}
