package top.chaser.framework.starter.gray.reactive;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.core.env.Environment;

public class LoadBalancerClientFactory
        extends NamedContextFactory<LoadBalancerClientSpecification>
        implements ReactiveLoadBalancer.Factory<ServiceInstance> {

    /**
     * Property source name for load balancer.
     */
    public static final String NAMESPACE = "loadbalancer";

    /**
     * Property for client name within the load balancer namespace.
     */
    public static final String PROPERTY_NAME = NAMESPACE + ".client.name";

    public LoadBalancerClientFactory() {
        super(LoadBalancerClientConfiguration.class, NAMESPACE, PROPERTY_NAME);
    }

    public String getName(Environment environment) {
        return environment.getProperty(PROPERTY_NAME);
    }

    @Override
    public ReactiveLoadBalancer<ServiceInstance> getInstance(String serviceId) {
        return getInstance(serviceId, ReactorServiceInstanceLoadBalancer.class);
    }
}