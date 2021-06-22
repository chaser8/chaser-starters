package top.chaser.framework.starter.uaa.authorization;

import top.chaser.framework.starter.uaa.authorization.security.sms.SmsCodeAuthenticationManager;
import top.chaser.framework.starter.uaa.authorization.security.sms.SmsCodeAuthenticationProvider;
import top.chaser.framework.starter.uaa.authorization.security.sms.SmsCodeSecurityConfigurerAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * 短信登录相关配置
 * @author: yangzb
 * @date 2021/6/22 10:16 上午
 **/
@Configuration
@ConditionalOnProperty(name = "chaser.security.server.sms-login-enable", havingValue = "true")
public class SmsCodeLoginConfiguration {
    @Bean
    @Order
    @ConditionalOnMissingBean(SmsCodeSecurityConfigurerAdapter.class)
    public SmsCodeSecurityConfigurerAdapter smsCodeSecurityConfigurerAdapter() {
        return new SmsCodeSecurityConfigurerAdapter();
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(SmsCodeAuthenticationProvider.class)
    public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
        return new SmsCodeAuthenticationProvider();
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(SmsCodeAuthenticationManager.class)
    public SmsCodeAuthenticationManager smsCodeAuthenticationManager() {
        return new SmsCodeAuthenticationManager();
    }
}
