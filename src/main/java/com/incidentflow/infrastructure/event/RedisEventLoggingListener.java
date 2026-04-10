package com.incidentflow.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisEventLoggingListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventLoggingListener.class);
    private static final String CONSUMER_NAME = "redis_event_logging_listener";

    private final RedisEventProcessingService redisEventProcessingService;

    public RedisEventLoggingListener(RedisEventProcessingService redisEventProcessingService) {
        this.redisEventProcessingService = redisEventProcessingService;
    }

    public void handleMessage(String message) {
        redisEventProcessingService.process(CONSUMER_NAME, message, this::logEvent);
    }

    private void logEvent(RedisEventMessage event) {
        LOGGER.info(
                "event_id={} event_type={} service_id={} incident_id={} timestamp={}",
                event.eventId(),
                event.eventType(),
                event.serviceId(),
                event.incidentId(),
                event.timestamp()
        );
    }
}
