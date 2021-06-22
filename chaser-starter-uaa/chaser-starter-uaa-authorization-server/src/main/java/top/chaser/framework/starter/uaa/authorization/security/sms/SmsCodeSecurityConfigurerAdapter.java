package top.chaser.framework.starter.uaa.authorization.security.sms;

import top.chaser.framework.uaa.base.store.TokenStore;
import top.chaser.framework.starter.uaa.authorization.AuthorizationServerJwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 短信验证码登录相关配置
 *
 * @author: yangzb
 * @date 2021/6/2 11:20 上午
 **/
public class SmsCodeSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    protected TokenStore tokenStore;
    @Autowired
    protected AuthorizationServerJwtProperties jwtProperties;
    @Autowired
    protected SmsCodeAuthenticationProvider smsCodeAuthenticationProvider;
    @Autowired
    protected SmsCodeAuthenticationManager authenticationManager;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter(jwtProperties, tokenStore);
        smsCodeAuthenticationFilter.setAuthenticationManager(authenticationManager);
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        http.authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
