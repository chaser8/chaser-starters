package top.chaser.framework.starter.uaa.resource.security.code;

import cn.hutool.core.util.StrUtil;
import top.chaser.framework.uaa.base.code.AuthCodeType;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 集团组件缓存
 *
 * @author: yangzb
 * @date 2021/6/2 3:25 下午
 **/
public class CacheValidateCodeStore implements ValidateCodeStore {
    protected RedisTemplate<String, String> redisTemplate;

    public static final String CACHE_KEY = "UAA:CODE:{}:{}";


    public CacheValidateCodeStore(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @SneakyThrows
    public void store(AuthCodeType type, String key, String value, int expireSeconds) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String cacheKey = StrUtil.format(CacheValidateCodeStore.CACHE_KEY, type.name(), key);
        valueOperations.set(cacheKey, value);
        redisTemplate.expire(cacheKey, Duration.ofSeconds(expireSeconds));
    }

    @Override
    public long expireSeconds(AuthCodeType type, String key) {
        String cacheKey = StrUtil.format(CacheValidateCodeStore.CACHE_KEY, type.name(), key);
        return redisTemplate.getExpire(cacheKey, TimeUnit.SECONDS);
    }

    @Override
    @SneakyThrows
    public String get(AuthCodeType type, String key) {
        String cacheKey = StrUtil.format(CacheValidateCodeStore.CACHE_KEY, type.name(), key);
        return redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    @SneakyThrows
    public void del(AuthCodeType type, String key) {
        String cacheKey = StrUtil.format(CacheValidateCodeStore.CACHE_KEY, type.name(), key);
        redisTemplate.delete(cacheKey);
    }
}
