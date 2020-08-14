package top.chaser.framework.boot.starter.web.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/****
 * 
 * @description:
 * @author: 
 * @date 2020/8/14 5:31 下午
 **/
@ConditionalOnWebApplication
@Configuration
@EnableConfigurationProperties(SpringBootWebProperties.class)
public class SpringBootWebAutoConfiguration {
}