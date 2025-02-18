package com.example.olive.Olive.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveToRedis(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
    }

    public String getFromRedis(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteFromRedis(String key) {
        redisTemplate.delete(key);
    }
}
