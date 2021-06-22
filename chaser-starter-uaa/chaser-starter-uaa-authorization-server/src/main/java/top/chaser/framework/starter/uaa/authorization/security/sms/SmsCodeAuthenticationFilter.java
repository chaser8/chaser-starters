package top.chaser.framework.starter.uaa.authorization.security.sms;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import top.chaser.framework.starter.uaa.authorization.AuthorizationServerJwtProperties;
import top.chaser.framework.starter.uaa.authorization.security.password.PasswordAuthenticationProcessingFilter;
import top.chaser.framework.uaa.base.code.SmsAuthCode;
import top.chaser.framework.uaa.base.store.TokenStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.chaser.framework.common.web.request.MultiReadHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 手机号码认证方式
 * @author: yangzb
 * @date 2021/6/2 11:03 上午
 **/
@Slf4j
public class SmsCodeAuthenticationFilter extends PasswordAuthenticationProcessingFilter {
    public SmsCodeAuthenticationFilter(AuthorizationServerJwtProperties properties, TokenStore tokenStore) {
        super(properties, tokenStore);
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/sms/login", "POST"));
    }

    /** 是否只支持 post 请求 */
    private boolean postOnly = true;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        UsernamePasswordAuthenticationToken authRequest;
        try {
            MultiReadHttpServletRequest wrappedRequest = MultiReadHttpServletRequest.newMultiReadHttpServletRequest(request);
            // 将前端传递的数据转换成jsonBean数据格式
            Optional<SmsAuthCode> smsValidateCodeOptional = Optional.ofNullable(JSONUtil.toBean(wrappedRequest.getBody(), SmsAuthCode.class));
            SmsAuthCode smsAuthCode = smsValidateCodeOptional.orElseThrow(() -> new AuthenticationServiceException("ValidateCode parameter error"));
            authRequest = new UsernamePasswordAuthenticationToken(smsAuthCode.getPhoneNumber().trim(), null, null);
            if (StrUtil.isBlank(smsAuthCode.getAuthCodeId())) {
                throw new AuthenticationServiceException("validateCode id cannot be null");
            }
            authRequest.setDetails(authenticationDetailsSource.buildDetails(wrappedRequest));
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (AuthenticationException authenticationException) {
            throw authenticationException;
        } catch (Exception e) {
            log.error("", e);
            throw new AuthenticationServiceException("短信认证出错");
        }
    }
}
