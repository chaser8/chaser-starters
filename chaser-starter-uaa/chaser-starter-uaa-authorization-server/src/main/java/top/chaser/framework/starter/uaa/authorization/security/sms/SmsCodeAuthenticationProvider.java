package top.chaser.framework.starter.uaa.authorization.security.sms;

import top.chaser.framework.starter.uaa.authorization.security.bean.DefaultUserDetails;
import top.chaser.framework.starter.uaa.authorization.service.UaaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 负责手机号认证
 *
 * @author: yangzb
 * @date 2021/6/2 11:20 上午
 **/
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private UaaUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = (String) authentication.getPrincipal();
        String smsCode = (String) authentication.getCredentials();
        DefaultUserDetails user = (DefaultUserDetails) userDetailsService.loadUserByPhone(phoneNumber);
        userDetailsService.loadUserExtra(user.getCurrentUser());
        // 认证通过
        return new UsernamePasswordAuthenticationToken(user.getCurrentUser(), smsCode, user.getAuthorities());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
