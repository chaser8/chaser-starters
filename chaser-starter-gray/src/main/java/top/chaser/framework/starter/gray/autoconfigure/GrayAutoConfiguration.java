package top.chaser.framework.starter.gray.autoconfigure;

import top.chaser.framework.starter.gray.gateway.GrayLoadBalancerClientFilter;
import top.chaser.framework.starter.gray.ribbon.RibbonSpringClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.netflix.ribbon.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.DispatcherHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: yzb
 * @create: 2019-05-08 14:28
 **/

@Configuration
@EnableConfigurationProperties(GrayProperties.class)
@ConditionalOnProperty(name = "gray.enable", havingValue = "true")
@AutoConfigureBefore(RibbonAutoConfiguration.class)
@Slf4j
public class GrayAutoConfiguration {

    @Autowired(required = false)
    private List<RibbonClientSpecification> configurations = new ArrayList<>();

    @Bean
    public SpringClientFactory springClientFactory(GrayProperties properties) {
        RibbonSpringClientFactory factory = new RibbonSpringClientFactory(properties);
        factory.setConfigurations(this.configurations);
        return factory;
    }

    @Bean
    @ConditionalOnClass({LoadBalancerClient.class, RibbonAutoConfiguration.class,
            DispatcherHandler.class})
    @ConditionalOnMissingBean({GrayLoadBalancerClientFilter.class})
    @SuppressWarnings("all")
    public GrayLoadBalancerClientFilter grayReactiveLoadBalancerClientFilter(RibbonLoadBalancerClient loadBalancerClient, LoadBalancerProperties properties) {
        return new GrayLoadBalancerClientFilter(loadBalancerClient, properties);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public IRule ribbonRule() {
//        GrayRule rule = new GrayRule();
//        return rule;
//    }
}