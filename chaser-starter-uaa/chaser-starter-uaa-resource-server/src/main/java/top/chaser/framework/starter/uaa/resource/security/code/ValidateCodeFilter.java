package top.chaser.framework.starter.uaa.resource.security.code;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import top.chaser.framework.starter.uaa.resource.ResourceServerProperties;
import top.chaser.framework.uaa.base.code.AuthCode;
import top.chaser.framework.uaa.base.code.AuthCodeType;
import top.chaser.framework.starter.uaa.resource.security.code.exception.ValidateCodeAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import top.chaser.framework.common.base.exception.BusiException;
import top.chaser.framework.common.base.exception.SystemException;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.request.MultiReadHttpServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author: yangzb
 * @date 2021/6/4 9:47 上午
 **/
@Slf4j
public class ValidateCodeFilter extends OncePerRequestFilter {

    /**
     * 验证码校验失败处理器
     */
    protected AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 系统中的校验码处理器
     */
    protected ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 存放所有需要校验验证码的 url 和 对应的类型
     */
    protected Map<String, Set<AuthCodeType>> processorTypes = new HashMap<>();
    protected ResourceServerProperties properties;

    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    protected AntPathMatcher pathMatcher = new AntPathMatcher();


    public ValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler, ValidateCodeProcessorHolder validateCodeProcessorHolder, ResourceServerProperties properties) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.validateCodeProcessorHolder = validateCodeProcessorHolder;
        this.properties = properties;
        afterPropertiesSet();
    }

    /**
     * 初始化要拦截的url配置信息（提交登录信息的url）
     */
    public void afterPropertiesSet() {
        Set<String> imgCodeMatchUrls = properties.getCaptcha().getMatchUrls();
        Set<String> smsCodeMatchUrls = properties.getSmsCode().getMatchUrls();
        if (properties.getSmsCode().getEnable()) {
            if (CollUtil.isEmpty(smsCodeMatchUrls)) {
                throw new InvalidConfigurationPropertyValueException("sms-code.matchUrls", "null", "不能为空");
            } else {
                for (String smsCodeMatchUrl : smsCodeMatchUrls) {
                    Set<AuthCodeType> types = Optional.ofNullable(processorTypes.get(smsCodeMatchUrl)).orElseGet(() -> Sets.newHashSet());
                    types.add(AuthCodeType.SMS);
                    processorTypes.put(smsCodeMatchUrl, types);
                }
            }
        }
        if (properties.getCaptcha().getEnable()) {
            if (CollUtil.isEmpty(imgCodeMatchUrls)) {
                throw new InvalidConfigurationPropertyValueException("captcha.matchUrls", "null", "不能为空");
            } else {
                for (String imgCodeMatchUrl : imgCodeMatchUrls) {
                    Set<AuthCodeType> types = Optional.ofNullable(processorTypes.get(imgCodeMatchUrl)).orElseGet(() -> Sets.newHashSet());
                    types.add(AuthCodeType.CAPTCHA);
                    processorTypes.put(imgCodeMatchUrl, types);
                }
            }
        }
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Set<AuthCodeType> types = getCodeType(request);
        try {
            //获取是否需要校验，以及对应的处理器类型
            if (CollUtil.isNotEmpty(types)) {
                for (AuthCodeType type : types) {
                    if (type != null) {
                        MultiReadHttpServletRequest wrappedRequest = MultiReadHttpServletRequest.newMultiReadHttpServletRequest(request);
                        HashMap hashMapBody = JSONUtil.parseObject(wrappedRequest.getBody(), HashMap.class);
                        hashMapBody.put("type", type.name());
                        Optional<AuthCode> smsValidateCodeOptional = Optional.ofNullable(JSONUtil.parseObject(JSONUtil.toJSONString(hashMapBody), AuthCode.class));
                        AuthCode authCode = smsValidateCodeOptional.orElseThrow(() -> new AuthenticationServiceException("ValidateCode parameter error"));
                        if (StrUtil.isBlank(authCode.getAuthCodeId())) {
                            throw new AuthenticationServiceException("validateCode id cannot be null");
                        }
                        if (StrUtil.isBlank(authCode.getAuthCode())) {
                            throw new AuthenticationCredentialsNotFoundException("validateCode cannot be null");
                        }
                        boolean validate = validateCodeProcessorHolder.getProcessor(type).validate(authCode);
                        if (!validate) {
                            throw new ValidateCodeAuthenticationException("validateCode error");
                        }
                        request = wrappedRequest;
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
            if (authenticationFailureHandler != null) {
                String message = "验证码认证出错";
                if (e instanceof AuthenticationException || e instanceof SystemException || e instanceof BusiException) {
                    message = e.getMessage();
                }
                authenticationFailureHandler.onAuthenticationFailure(request, response, new AuthenticationException(message, e) {
                });
            }
            return;
        }
        // 执行请求
        chain.doFilter(request, response);
    }


    /**
     * 判断请求是否需要拦截，并校验验证码
     *
     * @param request 请求
     * @return needValidate ? validateCodeType : null
     */
    protected Set<AuthCodeType> getCodeType(HttpServletRequest request) {
        // 登录请求都是 POST, 如果是 GET 则直接放行
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            Set<String> urls = processorTypes.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    return processorTypes.get(url);
                }
            }
        }
        return Sets.newHashSet();
    }
}
