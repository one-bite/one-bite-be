package code.rice.bowl.spaghetti.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 7, TimeUnit.DAYS);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
