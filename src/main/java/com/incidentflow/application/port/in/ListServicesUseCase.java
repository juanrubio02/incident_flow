package com.incidentflow.application.port.in;

import com.incidentflow.domain.model.Service;

import java.util.List;

public interface ListServicesUseCase {

    List<Service> list();
}
