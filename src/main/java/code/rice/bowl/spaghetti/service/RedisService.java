package code.rice.bowl.spaghetti.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * redis 에서 값 추가. (기본 만료 시간 7일)
     * @param key   추가 할 키
     * @param value 추가 할 값.
     */
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 7, TimeUnit.DAYS);
    }

    /**
     * redis 에 값 추가. (밀리초 단위로 만료 시간 설정)
     * @param key       추가 할 키.
     * @param value     추가 할 값.
     * @param exp       해당 데이터 만료 시간.
     */
    public void setValue(String key, String value, Long exp) {
        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.MILLISECONDS);
    }

    /**
     * key 해당하는 값을 반환.
     * @param key   검색할 키
     * @return      키에 해당하는 값. 키가 없는 경우 NULL.
     */
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 해당 키 삭제
     * @param key 삭제할 키
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 키 존재 여부 확인.
     * @param key   확인할 키 값.
     * @return      T : 존재 O, F : 존재 X
     */
    public boolean hasKey(String key) {
        try {
            Boolean hasKey = redisTemplate.hasKey(key);

            return Boolean.TRUE.equals(hasKey);
        } catch (Exception e) {
            // Redis 연결 문제 발생.
            return false;
        }
    }
}
