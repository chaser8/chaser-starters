package top.chaser.framework.cloud.starter.gray.autoconfigure;

import com.netflix.loadbalancer.IRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;
import top.chaser.framework.cloud.starter.gray.gateway.GrayLoadBalancerClientFilter;
import top.chaser.framework.cloud.starter.gray.ribbon.GrayRule;

/**
 * @description:
 * @author: yzb
 * @create: 2019-05-08 14:28
 **/

@Configuration
@EnableConfigurationProperties(GrayProperties.class)
@ConditionalOnProperty(name = "chaser.gray.enable", havingValue = "true")
@Slf4j
public class GrayAutoConfiguration{

    @Bean
    @ConditionalOnClass({ LoadBalancerClient.class, RibbonAutoConfiguration.class,
            DispatcherHandler.class })
    @ConditionalOnMissingBean({GrayLoadBalancerClientFilter.class})
    @SuppressWarnings("all")
    public GrayLoadBalancerClientFilter grayReactiveLoadBalancerClientFilter(RibbonLoadBalancerClient loadBalancerClient, LoadBalancerProperties properties) {
        return new GrayLoadBalancerClientFilter(loadBalancerClient, properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule(GrayProperties grayProperties) {
        GrayRule rule = new GrayRule();
        return rule;
    }
}