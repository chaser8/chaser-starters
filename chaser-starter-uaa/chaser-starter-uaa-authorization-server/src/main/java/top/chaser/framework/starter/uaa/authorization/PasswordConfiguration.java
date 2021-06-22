package top.chaser.framework.starter.uaa.authorization;

import top.chaser.framework.starter.uaa.authorization.security.password.PasswordAuthenticationManager;
import top.chaser.framework.starter.uaa.authorization.security.password.PasswordAuthenticationProvider;
import top.chaser.framework.starter.uaa.authorization.security.password.PasswordSecurityConfigurerAdapter;
import top.chaser.framework.uaa.base.store.CacheStore;
import top.chaser.framework.uaa.base.store.TokenStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: yangzb
 * @date 2021/5/19 10:33 上午
 **/
@Configuration
public class PasswordConfiguration {
    @Bean
    @Order
    @ConditionalOnMissingBean(PasswordAuthenticationProvider.class)
    public PasswordAuthenticationProvider passwordAuthenticationProvider() {
        return new PasswordAuthenticationProvider();
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(PasswordSecurityConfigurerAdapter.class)
    public PasswordSecurityConfigurerAdapter passwordSecurityConfigurerAdapter() {
        return new PasswordSecurityConfigurerAdapter();
    }


    @Bean
    @Order
    @ConditionalOnMissingBean(PasswordAuthenticationManager.class)
    public PasswordAuthenticationManager passwordAuthenticationManager() {
        return new PasswordAuthenticationManager();
    }

    @Bean
    @ConditionalOnMissingBean(TokenStore.class)
    public TokenStore tokenStore(RedisTemplate<String,String> redisTemplate) {
        return new CacheStore(redisTemplate);
    }
}
