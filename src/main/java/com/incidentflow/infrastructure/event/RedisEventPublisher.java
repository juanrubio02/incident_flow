package com.incidentflow.infrastructure.event;

import com.incidentflow.application.port.out.EventPublisher;
import com.incidentflow.domain.event.DomainEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisEventPublisher implements EventPublisher {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisEventMessageMapper redisEventMessageMapper;
    private final String channel;

    public RedisEventPublisher(
            StringRedisTemplate stringRedisTemplate,
            RedisEventMessageMapper redisEventMessageMapper,
            org.springframework.data.redis.listener.ChannelTopic incidentFlowEventsTopic
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisEventMessageMapper = redisEventMessageMapper;
        this.channel = incidentFlowEventsTopic.getTopic();
    }

    @Override
    public void publish(DomainEvent event) {
        String message = redisEventMessageMapper.toJson(event);
        stringRedisTemplate.convertAndSend(channel, message);
    }
}
