package top.chaser.framework.starter.uaa.authorization;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: chaser8
 * @date 2021/5/20 11:43 上午
 **/
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "chaser.security.server")
@Getter
@Setter
public class AuthorizationServerProperties {
    /**
     * 是否启用短信登录
     */
    private boolean smsLoginEnable = false;
    /**
     * 密码连续错误最大次数，超过次数锁定帐号
     */
    private int maxPasswordErrorTimes = 3;
}
