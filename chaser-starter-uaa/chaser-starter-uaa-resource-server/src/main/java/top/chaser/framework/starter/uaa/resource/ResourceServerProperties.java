package top.chaser.framework.starter.uaa.resource;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.lang.NonNull;

import java.util.Set;

/**
 * @author: chaser8
 * @date 2021/5/20 11:43 上午
 **/
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "chaser.security.resource-server")
public class ResourceServerProperties implements InitializingBean {
    private static final Set<String> DEFAULT_WHITE_LIST = Sets.newHashSet(
            "/error",
            "/v3/api-docs",
            "/v2/api-docs",
            "/sms/send",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources");
    private static final Set<String> DEFAULT_STATIC_LIST = Sets.newHashSet(
            "/html/**",
            "/css/**",
            "/js/**",
            "/favicon.ico",
            "/images/**",
            "/img/**");
    /**
     * 白名单，不做权限验证
     */
    @Getter
    @Setter
    private Set<String> whiteListPattern = Sets.newHashSet();
    /**
     * 静态资源，不做权限验证
     */
    @Getter
    @Setter
    private Set<String> staticPathPattern = Sets.newHashSet();

    @Getter
    @Setter
    @NestedConfigurationProperty
    private SmsCodeProperties smsCode = new SmsCodeProperties();

    @Getter
    @Setter
    @NestedConfigurationProperty
    private CaptchaProperties captcha = new CaptchaProperties();

    @Override
    public void afterPropertiesSet() {
        whiteListPattern.addAll(DEFAULT_WHITE_LIST);
        staticPathPattern.addAll(DEFAULT_STATIC_LIST);
    }

    @Getter
    @Setter
    public class SmsCodeProperties {
        /**
         * 是否启用
         */
        @NonNull
        private Boolean enable = false;

        /**
         * 验证码长度
         */
        private Integer length = 6;
        /**
         * 有效期秒，默认300秒 5分钟
         */
        private long expireSeconds = 60 * 5;


        /**
         * 重新获取间隔时间,默认60秒
         */
        private long sendIntervalSeconds = 60;


        /**
         * 需要做验证的url，如：手机短信验证码登录地址，修改密码短信验证地址等
         */
        private Set<String> matchUrls = Sets.newHashSet();
    }

    @Getter
    @Setter
    public class CaptchaProperties {
        /**
         * 是否启用
         */
        @NonNull
        private Boolean enable = false;

        /**
         * 验证码长度
         */
        private Integer length = 6;

        /**
         * 有效期秒，默认300秒 5分钟
         */
        private long expireSeconds = 60 * 5;


        /**
         * 需要做验证的url，如：获取短信验证码，密码登录
         */
        private Set<String> matchUrls = Sets.newHashSet();
    }

}
