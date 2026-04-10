package com.incidentflow.application.port.out;

import com.incidentflow.domain.event.DomainEvent;

public interface EventPublisher {

    void publish(DomainEvent event);
}
