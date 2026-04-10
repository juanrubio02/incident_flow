package com.incidentflow.infrastructure.config;

import com.incidentflow.application.port.out.EventPublisher;
import com.incidentflow.application.service.IncidentApplicationService;
import com.incidentflow.application.service.ServiceApplicationService;
import com.incidentflow.domain.repository.IncidentRepository;
import com.incidentflow.domain.repository.ServiceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(RedisEventProperties.class)
public class ApplicationConfiguration {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    public ServiceApplicationService serviceApplicationService(
            ServiceRepository serviceRepository,
            IncidentRepository incidentRepository,
            EventPublisher eventPublisher,
            Clock systemClock
    ) {
        return new ServiceApplicationService(serviceRepository, incidentRepository, eventPublisher, systemClock);
    }

    @Bean
    public IncidentApplicationService incidentApplicationService(
            IncidentRepository incidentRepository,
            ServiceRepository serviceRepository,
            EventPublisher eventPublisher,
            Clock systemClock
    ) {
        return new IncidentApplicationService(incidentRepository, serviceRepository, eventPublisher, systemClock);
    }
}
