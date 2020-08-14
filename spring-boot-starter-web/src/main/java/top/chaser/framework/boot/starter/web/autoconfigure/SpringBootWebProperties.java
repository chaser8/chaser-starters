package top.chaser.framework.boot.starter.web.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "chaser.web")
@Getter
@Setter
public class SpringBootWebProperties {
    @NestedConfigurationProperty
    private Sign sign = new Sign();
    @Getter
    @Setter
    public class Sign {
        public static final String KEY = "sign";
        private boolean enable = true;
        private String secretKey;
        private String [] patterns;
    }
}
