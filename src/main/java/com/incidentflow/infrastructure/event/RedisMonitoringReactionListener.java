package com.incidentflow.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisMonitoringReactionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisMonitoringReactionListener.class);
    private static final String CONSUMER_NAME = "redis_monitoring_reaction_listener";

    private final RedisEventProcessingService redisEventProcessingService;

    public RedisMonitoringReactionListener(RedisEventProcessingService redisEventProcessingService) {
        this.redisEventProcessingService = redisEventProcessingService;
    }

    public void handleMessage(String message) {
        redisEventProcessingService.process(CONSUMER_NAME, message, this::logReaction);
    }

    private void logReaction(RedisEventMessage event) {
        LOGGER.info(
                "event_id={} reaction_type={} event_type={} service_id={} incident_id={} timestamp={}",
                event.eventId(),
                reactionType(event.eventType()),
                event.eventType(),
                event.serviceId(),
                event.incidentId(),
                event.timestamp()
        );
    }

    private String reactionType(String eventType) {
        return switch (eventType) {
            case "ServiceDownEvent" -> "monitoring_alert_triggered";
            case "ServiceUpEvent" -> "monitoring_recovery_recorded";
            case "IncidentCreatedEvent" -> "incident_response_started";
            case "IncidentResolvedEvent" -> "incident_response_closed";
            default -> "event_observed";
        };
    }
}
