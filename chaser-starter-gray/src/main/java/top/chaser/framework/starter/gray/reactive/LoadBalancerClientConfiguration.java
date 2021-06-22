package top.chaser.framework.starter.gray.reactive;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

//@Configuration(proxyBeanMethods = false)
//@ConditionalOnDiscoveryEnabled
public class LoadBalancerClientConfiguration {

    private static final int REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER = 193827465;

    @Bean
    @ConditionalOnMissingBean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
            Environment environment,
            org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new RoundRobinLoadBalancer(loadBalancerClientFactory.getLazyProvider(name,
                ServiceInstanceListSupplier.class), name);
    }

//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnReactiveDiscoveryEnabled
    @Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER)
    public static class ReactiveSupportConfiguration {

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations",
                havingValue = "default", matchIfMissing = true)
        public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return new GrayServiceInstanceListSupplierBuilder().withDiscoveryClient()
                    .withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations",
                havingValue = "zone-preference")
        public ServiceInstanceListSupplier zonePreferenceDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient()
                    .withZonePreference().withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations",
                havingValue = "health-check")
        public ServiceInstanceListSupplier healthCheckDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withDiscoveryClient()
                    .withHealthChecks().withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        @ConditionalOnMissingBean
        public ServiceInstanceListSupplier discoveryClientServiceInstanceSupplier(
                ReactiveDiscoveryClient discoveryClient, Environment env,
                ApplicationContext context) {
            DiscoveryClientServiceInstanceListSupplier delegate = new DiscoveryClientServiceInstanceListSupplier(
                    discoveryClient, env);
            ObjectProvider<LoadBalancerCacheManager> cacheManagerProvider = context
                    .getBeanProvider(LoadBalancerCacheManager.class);
            if (cacheManagerProvider.getIfAvailable() != null) {
                return new CachingServiceInstanceListSupplier(delegate,
                        cacheManagerProvider.getIfAvailable());
            }
            return delegate;
        }

    }

//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnBlockingDiscoveryEnabled
    @Order(REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER + 1)
    public static class BlockingSupportConfiguration {

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations",
                havingValue = "default", matchIfMissing = true)
        public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return new GrayServiceInstanceListSupplierBuilder().withBlockingDiscoveryClient()
                    .withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations",
                havingValue = "zone-preference")
        public ServiceInstanceListSupplier zonePreferenceDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient()
                    .withZonePreference().withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        @ConditionalOnProperty(value = "spring.cloud.loadbalancer.configurations",
                havingValue = "health-check")
        public ServiceInstanceListSupplier healthCheckDiscoveryClientServiceInstanceListSupplier(
                ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder().withBlockingDiscoveryClient()
                    .withHealthChecks().withCaching().build(context);
        }

        @Bean
        @ConditionalOnBean(DiscoveryClient.class)
        @ConditionalOnMissingBean
        public ServiceInstanceListSupplier discoveryClientServiceInstanceSupplier(
                DiscoveryClient discoveryClient, Environment env,
                ApplicationContext context) {
            DiscoveryClientServiceInstanceListSupplier delegate = new DiscoveryClientServiceInstanceListSupplier(
                    discoveryClient, env);
            ObjectProvider<LoadBalancerCacheManager> cacheManagerProvider = context
                    .getBeanProvider(LoadBalancerCacheManager.class);
            if (cacheManagerProvider.getIfAvailable() != null) {
                return new CachingServiceInstanceListSupplier(delegate,
                        cacheManagerProvider.getIfAvailable());
            }
            return delegate;
        }

    }

}
