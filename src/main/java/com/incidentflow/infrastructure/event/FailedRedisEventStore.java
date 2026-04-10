package com.incidentflow.infrastructure.event;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class FailedRedisEventStore {

    private final List<FailedRedisEvent> failedEvents = new CopyOnWriteArrayList<>();

    public void add(FailedRedisEvent failedEvent) {
        failedEvents.add(failedEvent);
    }

    public List<FailedRedisEvent> getFailedEvents() {
        return List.copyOf(failedEvents);
    }
}
