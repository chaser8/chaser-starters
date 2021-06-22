package top.chaser.framework.starter.uaa.authorization;

import top.chaser.framework.uaa.base.JwtProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "chaser.security.jwt")
@Getter
@Setter
public class AuthorizationServerJwtProperties extends JwtProperties {

    /**
     * RSA私钥，client情况下只需要配公钥
     */
    private String privateKey;
}
