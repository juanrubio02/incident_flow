package com.incidentflow.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class RedisEventProcessingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventProcessingService.class);
    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MILLIS = 1000L;

    private final RedisEventMessageMapper redisEventMessageMapper;
    private final FailedRedisEventStore failedRedisEventStore;
    private final Clock clock;
    private final Set<String> processedEventIds = ConcurrentHashMap.newKeySet();

    public RedisEventProcessingService(
            RedisEventMessageMapper redisEventMessageMapper,
            FailedRedisEventStore failedRedisEventStore,
            Clock clock
    ) {
        this.redisEventMessageMapper = redisEventMessageMapper;
        this.failedRedisEventStore = failedRedisEventStore;
        this.clock = clock;
    }

    public void process(String consumerName, String rawMessage, Consumer<RedisEventMessage> processor) {
        RedisEventMessage metadata = redisEventMessageMapper.extractMetadata(rawMessage);
        String processedKey = processedKey(consumerName, metadata.eventId(), rawMessage);

        if (processedEventIds.contains(processedKey)) {
            LOGGER.info(
                    "event_id={} event_type={} status={} retries={} consumer_name={} duplicate={}",
                    metadata.eventId(),
                    metadata.eventType(),
                    "processed",
                    0,
                    consumerName,
                    true
            );
            return;
        }

        long backoffMillis = INITIAL_BACKOFF_MILLIS;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                RedisEventMessage event = redisEventMessageMapper.fromJson(rawMessage);
                processor.accept(event);

                processedEventIds.add(processedKey);

                LOGGER.info(
                        "event_id={} event_type={} status={} retries={} consumer_name={} duplicate={}",
                        event.eventId(),
                        event.eventType(),
                        "processed",
                        attempt - 1,
                        consumerName,
                        false
                );
                return;
            } catch (Exception exception) {
                if (attempt < MAX_RETRIES) {
                    LOGGER.warn(
                            "event_id={} event_type={} status={} retries={} retry_count={} consumer_name={} error_message={}",
                            metadata.eventId(),
                            metadata.eventType(),
                            "retry",
                            attempt,
                            attempt,
                            consumerName,
                            safeErrorMessage(exception)
                    );
                    if (!sleep(backoffMillis)) {
                        failedRedisEventStore.add(new FailedRedisEvent(
                                metadata.eventId(),
                                metadata.eventType(),
                                metadata.serviceId(),
                                metadata.incidentId(),
                                metadata.timestamp(),
                                consumerName,
                                rawMessage,
                                "Retry backoff interrupted",
                                attempt,
                                Instant.now(clock)
                        ));
                        LOGGER.error(
                                "event_failed event_id={} event_type={} status={} retries={} retry_count={} consumer_name={} error_message={}",
                                metadata.eventId(),
                                metadata.eventType(),
                                "failed",
                                attempt,
                                attempt,
                                consumerName,
                                "Retry backoff interrupted"
                        );
                        return;
                    }
                    backoffMillis *= 2;
                    continue;
                }

                failedRedisEventStore.add(new FailedRedisEvent(
                        metadata.eventId(),
                        metadata.eventType(),
                        metadata.serviceId(),
                        metadata.incidentId(),
                        metadata.timestamp(),
                        consumerName,
                        rawMessage,
                        safeErrorMessage(exception),
                        attempt,
                        Instant.now(clock)
                ));

                LOGGER.error(
                        "event_failed event_id={} event_type={} status={} retries={} retry_count={} consumer_name={} error_message={}",
                        metadata.eventId(),
                        metadata.eventType(),
                        "failed",
                        attempt,
                        attempt,
                        consumerName,
                        safeErrorMessage(exception),
                        exception
                );
                return;
            }
        }
    }

    private boolean sleep(long backoffMillis) {
        try {
            Thread.sleep(backoffMillis);
            return true;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private String processedKey(String consumerName, UUID eventId, String rawMessage) {
        return consumerName + ":" + (eventId == null ? Integer.toHexString(rawMessage.hashCode()) : eventId);
    }

    private String safeErrorMessage(Exception exception) {
        String message = exception.getMessage();
        return message == null || message.isBlank() ? exception.getClass().getSimpleName() : message;
    }
}
