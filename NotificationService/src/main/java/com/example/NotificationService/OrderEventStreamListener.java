package com.example.NotificationService;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderEventStreamListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        Map map = message.getValue();

        System.out.println("order = " + map);

        String userId = (String) map.get("userId");
        String productId = (String) map.get("productId");

        // 주문 건에 대한 메일 발송 처리
        System.out.println("[Order consumed] userId: " + userId + "   productId: " + productId);
    }
}
