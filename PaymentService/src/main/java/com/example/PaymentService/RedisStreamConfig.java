package com.example.PaymentService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

@Configuration
public class RedisStreamConfig {


    private final OrderEventStreamListener orderEventStreamListener;

    public RedisStreamConfig(OrderEventStreamListener orderEventStreamListener) {
        this.orderEventStreamListener = orderEventStreamListener;
    }

    @Bean
    public Subscription subscription(RedisConnectionFactory factory) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions options = StreamMessageListenerContainer
                .StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();

        StreamMessageListenerContainer listenerContainer = StreamMessageListenerContainer.create(factory, options);

        // Consumer의 Group을 미리 생성해야함(payment-service-group)
        // XGROUP Create order-events payment-service-group $ MKSTREAM
        // MKSTREAM = 스트림이 없으면 만든다 , 없는 스트림에 그룹을 만들면 에러가 나기 때문
        Subscription subscription = listenerContainer.receiveAutoAck(Consumer.from("payment-service-group", "instance-1"),
                StreamOffset.create("order-events", ReadOffset.lastConsumed()), orderEventStreamListener);

        listenerContainer.start();
        return subscription;
    }
}
