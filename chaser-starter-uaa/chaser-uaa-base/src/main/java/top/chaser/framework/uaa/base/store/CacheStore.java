package top.chaser.framework.uaa.base.store;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import top.chaser.framework.common.base.exception.AuthenticationException;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.session.User;

import java.time.Duration;
import java.util.Optional;

/**
 * token缓存默认支持mq和redis
 *
 * @author: chaser8
 * @date 2021/6/22 10:19 上午
 **/
public class CacheStore implements TokenStore {
    protected RedisTemplate<String,String> redisTemplate;

    public CacheStore(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 存储token，key usrcode,value token
     *
     * @param user
     * @param token
     * @return void
     * @author yangzb
     * @date 2021/5/24 10:03 上午
     */
    @Override
    @SneakyThrows
    public void storeToken(@NonNull User user, @NonNull String token, int expireSeconds) {
        String tokenKey = StrUtil.format(TOKEN_KEY, user.getUserCode());
        String userKey = StrUtil.format(USER_KEY, user.getUserCode());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(tokenKey, token);
        valueOperations.set(userKey, JSONUtil.toJSONString(user));
        expire(user.getUserCode(),expireSeconds);
    }

    /**
     * 通过userCode获取用户信息，包含机构，角色，权限
     *
     * @param userCode
     * @return User
     * @author yangzb
     * @date 2021/5/24 9:37 上午
     */
    @Override
    @SneakyThrows
    public User getUserByUserCode(String userCode) throws AuthenticationException {
        String userKey = StrUtil.format(USER_KEY, userCode);
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String userJsonStr = Optional.ofNullable(valueOperations.get(userKey)).orElseThrow(() -> new AuthenticationException("certification invalidation"));
        return JSONUtil.parseObject(userJsonStr, User.class);
    }

    @Override
    @SneakyThrows
    public void expire(String userCode, int expireSeconds) {
        String tokenKey = StrUtil.format(TOKEN_KEY, userCode);
        String userKey = StrUtil.format(USER_KEY, userCode);
        redisTemplate.expire(userKey, Duration.ofSeconds(expireSeconds));
        redisTemplate.expire(tokenKey, Duration.ofSeconds(expireSeconds));
    }

    @Override
    @SneakyThrows
    public boolean validate(@NonNull String userCode, @NonNull String token) {
        String tokenKey = StrUtil.format(TOKEN_KEY, userCode);
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();

        String cacheToken = valueOperations.get(tokenKey);
        return token.equals(cacheToken);
    }

    @Override
    @SneakyThrows
    public void remove(String userCode) {
        String tokenKey = StrUtil.format(TOKEN_KEY, userCode);
        String userKey = StrUtil.format(USER_KEY, userCode);
        redisTemplate.delete(CollUtil.newArrayList(tokenKey,userKey));
    }
}
