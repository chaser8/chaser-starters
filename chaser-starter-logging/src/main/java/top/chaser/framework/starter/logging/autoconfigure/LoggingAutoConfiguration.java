package top.chaser.framework.starter.logging.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.chaser.framework.starter.logging.HttpLoggingFilter;
import top.chaser.framework.starter.logging.LoggingProperties;
import top.chaser.framework.starter.logging.processor.LoggingProcessor;
import top.chaser.framework.starter.logging.processor.PrintLoggingProcessor;

import java.util.List;

/**
 * @author: chaser8
 * @date 2021/5/19 10:33 上午
 **/
@Configuration
@EnableConfigurationProperties({LoggingProperties.class})
@ConditionalOnProperty(value = "chaser.logging.enable",havingValue = "true", matchIfMissing = false)
public class LoggingAutoConfiguration {

    @Autowired
    private List<LoggingProperties.ProcessorProperties> processorPropertiesList;

    @Bean
    @ConditionalOnWebApplication
    public FilterRegistrationBean filterRegistrationBean(ApplicationContext applicationContext, HttpLoggingFilter httpLoggingFilter) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(httpLoggingFilter);
        bean.addUrlPatterns("/*");
        bean.setOrder(-999998);
        return bean;
    }

    @Bean
    @ConditionalOnWebApplication
    public HttpLoggingFilter requestLoggingFilter(ApplicationContext applicationContext) {
        return new HttpLoggingFilter(processorPropertiesList, applicationContext);
    }


    @Bean("printLoggingProcessor")
    public LoggingProcessor printLoggingProcessor() {
        return new PrintLoggingProcessor();
    }
}
