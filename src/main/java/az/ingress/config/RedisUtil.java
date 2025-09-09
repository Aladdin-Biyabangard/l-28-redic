package az.ingress.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedissonClient redissonClient;

    public <T> T getBucketSafe(String cacheKey) {
        try {
            RBucket<T> bucket = redissonClient.getBucket(cacheKey);
            return bucket == null ? null : bucket.get();
        } catch (Exception e) {
            log.warn("Redis get failed for key {}", cacheKey, e);
            return null;
        }
    }

    public <T> void saveToCacheSafe(String key, T value, Long expireTime, TemporalUnit temporalUnit) {
        try {
            var bucket = redissonClient.getBucket(key);
            bucket.set(value);
            bucket.expire(Duration.of(expireTime, temporalUnit));
        } catch (Exception e) {
            log.warn("Redis save failed for key {}", key, e);
        }
    }

    public void deleteFromCacheSafe(String key) {
        try {
            redissonClient.getBucket(key).delete();
        } catch (Exception e) {
            log.warn("Redis delete failed for key {}", key, e);
        }
    }
}
