package com.incidentflow.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "incidentflow.events")
public record RedisEventProperties(
        String channel
) {
}
