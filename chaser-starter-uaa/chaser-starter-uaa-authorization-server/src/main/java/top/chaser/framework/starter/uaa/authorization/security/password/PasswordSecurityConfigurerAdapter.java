/**
 *
 */
package top.chaser.framework.starter.uaa.authorization.security.password;

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
 * 用户名、密码认证
 * @author: yangzb
 * @date 2021/5/20 2:20 下午
 **/
public class PasswordSecurityConfigurerAdapter extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    protected AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    protected AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    protected PasswordAuthenticationProvider authenticationProvider;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected PasswordAuthenticationManager authenticationManager;
    @Autowired
    protected TokenStore tokenStore;
    @Autowired
    protected AuthorizationServerJwtProperties jwtProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        PasswordAuthenticationProcessingFilter authenticationProcessingFilter = new PasswordAuthenticationProcessingFilter(jwtProperties, tokenStore);
        authenticationProcessingFilter.setAuthenticationManager(authenticationManager);
        authenticationProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        authenticationProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        http.authenticationProvider(authenticationProvider)
                .addFilterAfter(authenticationProcessingFilter, UsernamePasswordAuthenticationFilter.class);

    }

}
