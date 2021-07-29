package top.chaser.framework.starter.gateway.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;
import top.chaser.framework.starter.gateway.filter.CachedBodyGlobalFilter;
import top.chaser.framework.common.web.flux.decorator.DefaultServerWebExchangeDecorator;

/**
 * @description:
 * @author: chaser8
 * @create: 2019-05-08 14:28
 **/

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayProperties.class)
@ComponentScan(basePackages={"top.chaser.framework"})
public class GatewayAutoConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE) //过滤器顺序
    public WebFilter webFilter() {
        return (exchange, chain) -> chain.filter(new DefaultServerWebExchangeDecorator(exchange));
    }

    @Bean
    @ConditionalOnMissingBean({CachedBodyGlobalFilter.class})
    public CachedBodyGlobalFilter cachedBodyGlobalFilter() {
        return new CachedBodyGlobalFilter();
    }
}
