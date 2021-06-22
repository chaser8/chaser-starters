package top.chaser.framework.starter.uaa.resource;

import top.chaser.framework.uaa.base.JwtProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "chaser.security.jwt")
@Getter
@Setter
public class ResourceServerJwtProperties extends JwtProperties {

    /**
     * RSA公钥，client情况下只需要配公钥
     */
    @NonNull
    private String publicKey;
}
