package top.chaser.framework.starter.uaa.authorization;

import top.chaser.framework.starter.uaa.authorization.security.handler.DefaultAuthenticationSuccessHandler;
import top.chaser.framework.starter.uaa.authorization.security.handler.DefaultLogoutHandler;
import top.chaser.framework.uaa.base.handler.DefaultAuthenticationFailureHandler;
import top.chaser.framework.starter.uaa.authorization.service.UaaUserDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import top.chaser.framework.common.base.exception.NotImplementedException;

/**
 * @author: chaser8
 * @date 2021/5/19 10:33 上午
 **/
@Configuration
@EnableConfigurationProperties({AuthorizationServerProperties.class, AuthorizationServerJwtProperties.class})
@ComponentScan("top.chaser.framework.starter.uaa.authorization")
public class AuthorizationServerAutoConfiguration {
    @Bean
    @Order
    @ConditionalOnMissingBean(UaaUserDetailsService.class)
    public UaaUserDetailsService userDetailsService() {
        throw new NotImplementedException("请实现 UaaUserDetailsService");
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new DefaultAuthenticationSuccessHandler();
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new DefaultAuthenticationFailureHandler();
    }

    @Bean
    @Order
    @ConditionalOnMissingBean(LogoutHandler.class)
    public LogoutHandler logoutHandler() {
        return new DefaultLogoutHandler();
    }
}
