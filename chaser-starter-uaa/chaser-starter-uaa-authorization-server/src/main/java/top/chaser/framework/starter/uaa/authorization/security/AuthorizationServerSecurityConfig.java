package top.chaser.framework.starter.uaa.authorization.security;

import top.chaser.framework.starter.uaa.resource.security.ResourceServerSecurityConfig;
import top.chaser.framework.starter.uaa.authorization.security.handler.DefaultLogoutSuccessHandler;
import top.chaser.framework.starter.uaa.authorization.security.password.PasswordSecurityConfigurerAdapter;
import top.chaser.framework.starter.uaa.authorization.security.sms.SmsCodeSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * ServerSecurity 设置
 * @author: yangzb
 * @date 2021/6/22 10:15 上午
 **/
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@EnableWebSecurity
public class AuthorizationServerSecurityConfig extends ResourceServerSecurityConfig {

    @Autowired
    private PasswordSecurityConfigurerAdapter passwordSecurityConfigurerAdapter;
    @Autowired(required = false)
    private SmsCodeSecurityConfigurerAdapter smsCodeSecurityConfigurerAdapter;

    @Autowired
    private LogoutHandler logoutHandler;


    /**
     * 装载BCrypt密码编码器
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        passwordSecurityConfigurerAdapter.configure(httpSecurity);
        if (smsCodeSecurityConfigurerAdapter != null) {
            httpSecurity.apply(smsCodeSecurityConfigurerAdapter);
        }
        super.configure(httpSecurity);
        //退出登录相关设置
        httpSecurity
                .logout()
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(new DefaultLogoutSuccessHandler())
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");
    }
}
