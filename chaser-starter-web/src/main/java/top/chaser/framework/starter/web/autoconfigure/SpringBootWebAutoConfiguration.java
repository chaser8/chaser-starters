package top.chaser.framework.starter.web.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/****
 * 
 * @description:
 * @author: 
 * @date 2020/8/14 5:31 下午
 **/
@ConditionalOnWebApplication
@Configuration
@EnableConfigurationProperties(SpringBootWebProperties.class)
@Import({DefaultGlobalExceptionHandlerAdvice.class,ErrorPageConfig.class})
public class SpringBootWebAutoConfiguration {
}