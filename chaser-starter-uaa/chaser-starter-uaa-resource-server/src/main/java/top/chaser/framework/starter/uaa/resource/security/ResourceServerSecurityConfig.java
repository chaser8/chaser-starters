package top.chaser.framework.starter.uaa.resource.security;

import top.chaser.framework.starter.uaa.resource.ResourceServerJwtProperties;
import top.chaser.framework.starter.uaa.resource.ResourceServerProperties;
import top.chaser.framework.uaa.base.store.TokenStore;
import top.chaser.framework.starter.uaa.resource.security.code.ValidateCodeSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import javax.annotation.Resource;

/**
 * resource.server 设置
 * @author: chaser8
 * @date 2021/6/22 10:15 上午
 **/
@EnableWebSecurity
@ConditionalOnMissingClass("top.chaser.framework.starter.uaa.authorization.security.AuthorizationServerSecurityConfig")
public class ResourceServerSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private AccessDeniedHandler accessDeniedHandler;
    @Resource
    private ResourceServerProperties resourceServerProperties;
    @Resource
    private ResourceServerJwtProperties jwtProperties;
    @Resource
    private TokenStore tokenStore;

    @Autowired(required = false)
    private ValidateCodeSecurityConfigurerAdapter validateCodeSecurityConfigurerAdapter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManager authenticationManager = authenticationManagerBean();
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManager, tokenStore, resourceServerProperties,jwtProperties);

        if (validateCodeSecurityConfigurerAdapter != null) {
            httpSecurity.apply(validateCodeSecurityConfigurerAdapter);
        }

        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
                .authorizeRequests()
                .antMatchers(resourceServerProperties.getWhiteListPattern().toArray(new String[]{}))
                .permitAll()
                .and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf()
                .disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest()
                .access("@permissionService.hasPermission(request,authentication)")
                .and()
                .headers()
                .cacheControl();


    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(resourceServerProperties.getStaticPathPattern().toArray(new String[]{}));
    }
}
