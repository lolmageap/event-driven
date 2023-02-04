package com.example.OrderService;

import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    private final StringRedisTemplate redisTemplate;

    public OrderController(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/order")
    public String order(
            @RequestParam String userId,
            @RequestParam String productId,
            @RequestParam String price
    ) {

        Map fieldMap = new HashMap<String, String>();
        fieldMap.put("userId", userId);
        fieldMap.put("productId", productId);
        fieldMap.put("price", price);

        redisTemplate.opsForStream().add("order-events", fieldMap);

        System.out.println("Order created");
        return "ok";
    }
}
