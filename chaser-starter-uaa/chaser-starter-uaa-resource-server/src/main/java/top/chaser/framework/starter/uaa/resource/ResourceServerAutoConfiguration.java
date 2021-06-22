package top.chaser.framework.starter.uaa.resource;

import top.chaser.framework.starter.uaa.resource.security.code.*;
import top.chaser.framework.starter.uaa.resource.security.code.sms.SmsCodeProcessor;
import top.chaser.framework.starter.uaa.resource.service.PermissionService;
import top.chaser.framework.starter.uaa.resource.service.impl.NonPermissionServiceImpl;
import top.chaser.framework.uaa.base.code.DefaultSmsCodeSender;
import top.chaser.framework.uaa.base.code.SmsCodeSender;
import top.chaser.framework.uaa.base.handler.DefaultAccessDeniedHandler;
import top.chaser.framework.uaa.base.handler.DefaultAuthenticationEntryPoint;
import top.chaser.framework.uaa.base.handler.DefaultAuthenticationFailureHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.util.List;

/**
 * @author: yangzb
 * @date 2021/5/19 10:33 上午
 **/
@Configuration
@EnableConfigurationProperties({ResourceServerProperties.class, ResourceServerJwtProperties.class})
@ComponentScan("top.chaser.framework.starter.uaa.resource")
public class ResourceServerAutoConfiguration {
    @Bean
    @Order
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint defaultAuthenticationEntryPoint() {
        return new DefaultAuthenticationEntryPoint();
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new DefaultAccessDeniedHandler();
    }

    @Bean("permissionService")
    @Order
    @ConditionalOnMissingBean(PermissionService.class)
    public PermissionService permissionService() {
        return new NonPermissionServiceImpl();
    }


    //***********************************************************ValidateCode
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @Order
    @ConditionalOnMissingBean(ValidateCodeStore.class)
    public ValidateCodeStore cacheValidateCodeStore(@NonNull RedisTemplate redisTemplate) {
        return new CacheValidateCodeStore(redisTemplate);
    }

    @Bean
    public ValidateCodeProcessorHolder validateCodeProcessorHolder(List<ValidateCodeProcessor> validateCodeProcessors) {
        ValidateCodeProcessorHolder validateCodeProcessorHolder = new ValidateCodeProcessorHolder(validateCodeProcessors);
        return validateCodeProcessorHolder;
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeProcessor.class)
    @Order
    @ConditionalOnProperty(name = "chaser.security.resource-server.sms-code.enable", havingValue = "true")
    public ValidateCodeProcessor smsCodeProcessor(@NonNull ValidateCodeStore validateCodeStore) {
        return new SmsCodeProcessor(validateCodeStore);
    }
    @Bean
    @ConditionalOnMissingBean(SmsCodeProcessor.class)
    @Order
    @ConditionalOnProperty(name = "chaser.security.resource-server.sms-code.enable", havingValue = "true")
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }


    @Bean
    public ValidateCodeSecurityConfigurerAdapter validateCodeSecurityConfig() {
        return new ValidateCodeSecurityConfigurerAdapter();
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new DefaultAuthenticationFailureHandler();
    }
}
