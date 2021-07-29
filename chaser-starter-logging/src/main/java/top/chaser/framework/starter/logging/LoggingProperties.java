package top.chaser.framework.starter.logging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: chaser8
 * @date 2021/5/20 11:43 上午
 **/
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "chaser.logging")
@Getter
@Setter
public class LoggingProperties {
    /**
     * 是否启用
     */
    @NonNull
    private Boolean enable = false;

    /**
     * 所有日志处理器
     */
    private List<ProcessorProperties> processors = new ArrayList<ProcessorProperties>();

    @Bean
    public List<ProcessorProperties> processors() {
        return this.processors;
    }

    @Getter
    @Setter
    public static class ProcessorProperties {
        /**
         * spring容器中处理器名
         */
        private String processorName = "printLoggingProcessor";

        /**
         * 需要排除的URLS，excludeUrls和includeUrls 只能配置1个
         */
        private Set<String> excludeUrls = new HashSet<>();

        /**
         * 包含的URLS，excludeUrls和includeUrls 只能配置1个
         */
        private Set<String> includeUrls = new HashSet<>();
    }
}
