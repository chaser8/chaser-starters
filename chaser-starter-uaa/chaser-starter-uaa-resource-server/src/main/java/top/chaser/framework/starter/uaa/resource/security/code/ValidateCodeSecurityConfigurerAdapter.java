package top.chaser.framework.starter.uaa.resource.security.code;

import top.chaser.framework.starter.uaa.resource.ResourceServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 装配验证码过滤器
 *
 * @author: chaser8
 * @date 2021/6/22 10:18 上午
 **/
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
public class ValidateCodeSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    /**
     * 验证码校验失败处理器
     */
    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 系统中的校验码处理器
     */
    @Autowired
    protected ValidateCodeProcessorHolder validateCodeProcessorHolder;

    @Autowired
    protected ResourceServerProperties properties;

    @Override
    public void configure(HttpSecurity http) {
        // 验证码过滤器加在认证处理器之前，以支持登录认证请求也可以校验验证码
        http.addFilterAfter(new ValidateCodeFilter(authenticationFailureHandler, validateCodeProcessorHolder, properties), LogoutFilter.class);
    }
}
