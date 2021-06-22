package top.chaser.framework.uaa.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "chaser.security.jwt")
@Getter
@Setter
public class JwtProperties {
    /**
     * token的有效期秒，默认30分钟
     */
    private long expireSeconds = 60*30;
}
