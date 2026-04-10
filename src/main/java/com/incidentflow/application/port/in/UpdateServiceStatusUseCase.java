package com.incidentflow.application.port.in;

import com.incidentflow.application.port.in.command.UpdateServiceStatusCommand;
import com.incidentflow.domain.model.Service;

public interface UpdateServiceStatusUseCase {

    Service updateStatus(UpdateServiceStatusCommand command);
}
