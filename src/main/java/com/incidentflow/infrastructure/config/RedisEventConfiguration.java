package com.incidentflow.infrastructure.config;

import com.incidentflow.infrastructure.event.RedisEventLoggingListener;
import com.incidentflow.infrastructure.event.RedisMonitoringReactionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class RedisEventConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisEventConfiguration.class);

    @Bean
    public ChannelTopic incidentFlowEventsTopic(RedisEventProperties properties) {
        return new ChannelTopic(properties.channel());
    }

    @Bean
    public ThreadPoolTaskExecutor redisEventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("redis-event-");
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }

    @Bean
    public MessageListenerAdapter redisEventLoggingListenerAdapter(RedisEventLoggingListener listener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "handleMessage");
        adapter.setSerializer(new StringRedisSerializer());
        return adapter;
    }

    @Bean
    public MessageListenerAdapter redisMonitoringReactionListenerAdapter(RedisMonitoringReactionListener listener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(listener, "handleMessage");
        adapter.setSerializer(new StringRedisSerializer());
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory connectionFactory,
            ThreadPoolTaskExecutor redisEventTaskExecutor,
            MessageListenerAdapter redisEventLoggingListenerAdapter,
            MessageListenerAdapter redisMonitoringReactionListenerAdapter,
            ChannelTopic incidentFlowEventsTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setTaskExecutor(redisEventTaskExecutor);
        container.setSubscriptionExecutor(redisEventTaskExecutor);
        container.setErrorHandler(error -> LOGGER.error("event_listener_error={}", error.getMessage(), error));
        container.addMessageListener(redisEventLoggingListenerAdapter, incidentFlowEventsTopic);
        container.addMessageListener(redisMonitoringReactionListenerAdapter, incidentFlowEventsTopic);
        return container;
    }
}
