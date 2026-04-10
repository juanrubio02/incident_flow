package com.incidentflow.application.port.in;

import com.incidentflow.application.port.in.command.CreateServiceCommand;
import com.incidentflow.domain.model.Service;

public interface CreateServiceUseCase {

    Service create(CreateServiceCommand command);
}
