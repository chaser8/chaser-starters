package top.chaser.framework.starter.web.autoconfigure;

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
        private boolean enable = false;
        private String secretKey;
        /**
         * 不需要加context-path
         */
        private String [] patterns = {"/**"};
        /**
         * 不需要加context-path
         */
        private String [] excludePatterns = {};
    }

    @NestedConfigurationProperty
    private Log log = new Log();
    @Getter
    @Setter
    public class Log {
        private boolean enable = false;
        /**
         * 不需要加context-path
         */
        private String [] patterns = {"/**"};
        /**
         * 不需要加context-path
         */
        private String [] excludePatterns = {};
    }
}
