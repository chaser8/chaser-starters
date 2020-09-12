package top.chaser.framework.starter.gray.reactive;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.loadbalancer.cache.LoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.DiscoveryClientServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplierBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
public class GrayServiceInstanceListSupplierBuilder {

    private ServiceInstanceListSupplierBuilder.Creator baseCreator;

    private ServiceInstanceListSupplierBuilder.DelegateCreator cachingCreator;

    private final List<ServiceInstanceListSupplierBuilder.DelegateCreator> creators = new ArrayList<>();

    public GrayServiceInstanceListSupplierBuilder() {
    }

    /**
     * Sets a blocking {@link DiscoveryClient}-based
     * {@link DiscoveryClientServiceInstanceListSupplier} as a base
     * {@link ServiceInstanceListSupplier} in the hierarchy.
     * @return the {@link ServiceInstanceListSupplierBuilder} object
     */
    public GrayServiceInstanceListSupplierBuilder withBlockingDiscoveryClient() {
        if (baseCreator != null && log.isWarnEnabled()) {
            log.warn(
                    "Overriding a previously set baseCreator with a blocking DiscoveryClient baseCreator.");
        }
        this.baseCreator = context -> {
            DiscoveryClient discoveryClient = context.getBean(DiscoveryClient.class);

            return new DiscoveryClientServiceInstanceListSupplier(discoveryClient,
                    context.getEnvironment());
        };
        return this;
    }

    /**
     * Sets a {@link ReactiveDiscoveryClient}-based
     * {@link DiscoveryClientServiceInstanceListSupplier} as a base
     * {@link ServiceInstanceListSupplier} in the hierarchy.
     * @return the {@link ServiceInstanceListSupplierBuilder} object
     */
    public GrayServiceInstanceListSupplierBuilder withDiscoveryClient() {
        if (baseCreator != null && log.isWarnEnabled()) {
            log.warn(
                    "Overriding a previously set baseCreator with a ReactiveDiscoveryClient baseCreator.");
        }
        this.baseCreator = context -> {
            ReactiveDiscoveryClient discoveryClient = context
                    .getBean(ReactiveDiscoveryClient.class);

            return new DiscoveryClientServiceInstanceListSupplier(discoveryClient,
                    context.getEnvironment());
        };
        return this;
    }

    /**
     * Sets a user-provided {@link ServiceInstanceListSupplier} as a base
     * {@link ServiceInstanceListSupplier} in the hierarchy.
     * @param supplier a user-provided {@link ServiceInstanceListSupplier} instance
     * @return the {@link ServiceInstanceListSupplierBuilder} object
     */
    public GrayServiceInstanceListSupplierBuilder withBase(
            ServiceInstanceListSupplier supplier) {
        this.baseCreator = context -> supplier;
        return this;
    }

    /**
     * If {@link LoadBalancerCacheManager} is available in the context, wraps created
     * {@link ServiceInstanceListSupplier} hierarchy with a
     * {@link CachingServiceInstanceListSupplier} instance to provide a caching mechanism
     * for service instances. Uses {@link ObjectProvider} to lazily resolve
     * {@link LoadBalancerCacheManager}.
     * @return the {@link ServiceInstanceListSupplierBuilder} object
     */
    public GrayServiceInstanceListSupplierBuilder withCaching() {
        if (cachingCreator != null && log.isWarnEnabled()) {
            log.warn(
                    "Overriding a previously set cachingCreator with a CachingServiceInstanceListSupplier-based cachingCreator.");
        }
        this.cachingCreator = (context, delegate) -> {
            ObjectProvider<LoadBalancerCacheManager> cacheManagerProvider = context
                    .getBeanProvider(LoadBalancerCacheManager.class);
            if (cacheManagerProvider.getIfAvailable() != null) {
                return new GrayServiceInstanceListSupplier(delegate,
                        cacheManagerProvider.getIfAvailable());
            }
            if (log.isWarnEnabled()) {
                log.warn(
                        "LoadBalancerCacheManager not available, returning delegate without caching.");
            }
            return delegate;
        };
        return this;
    }

    /**
     * Builds the {@link ServiceInstanceListSupplier} hierarchy.
     * @param context application context
     * @return a {@link ServiceInstanceListSupplier} instance on top of the delegate
     * hierarchy
     */
    public ServiceInstanceListSupplier build(ConfigurableApplicationContext context) {
        Assert.notNull(baseCreator, "A baseCreator must not be null");

        ServiceInstanceListSupplier supplier = baseCreator.apply(context);

        for (ServiceInstanceListSupplierBuilder.DelegateCreator creator : creators) {
            supplier = creator.apply(context, supplier);
        }

        if (this.cachingCreator != null) {
            supplier = this.cachingCreator.apply(context, supplier);
        }
        return supplier;
    }

    /**
     * Allows creating a {@link ServiceInstanceListSupplier} instance based on provided
     * {@link ConfigurableApplicationContext}.
     */
    public interface Creator extends
            Function<ConfigurableApplicationContext, ServiceInstanceListSupplier> {

    }

    /**
     * Allows creating a {@link ServiceInstanceListSupplier} instance based on provided
     * {@link ConfigurableApplicationContext} and another
     * {@link ServiceInstanceListSupplier} instance that will be used as a delegate.
     */
    public interface DelegateCreator extends
            BiFunction<ConfigurableApplicationContext, ServiceInstanceListSupplier, ServiceInstanceListSupplier> {

    }

}
