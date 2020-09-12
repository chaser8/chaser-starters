package top.chaser.framework.starter.gray.reactive;

import org.springframework.cache.CacheManager;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

public class GrayServiceInstanceListSupplier extends CachingServiceInstanceListSupplier {
    public GrayServiceInstanceListSupplier(ServiceInstanceListSupplier delegate, CacheManager cacheManager) {
        super(delegate, cacheManager);
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return super.get();
    }
}
