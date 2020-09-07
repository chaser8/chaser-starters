package top.chaser.framework.cloud.starter.gray.gateway;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.chaser.framework.cloud.starter.gray.ribbon.GrayParams;
import top.chaser.framework.common.base.util.JSONUtil;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class GrayLoadBalancerClientFilter implements GlobalFilter, Ordered {

    public static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10100 - 1;
    public static final String CACHED_REQUEST_BODY_ATTR = "cachedRequestBodyStr";

    private static final Log log = LogFactory.getLog(GrayLoadBalancerClientFilter.class);

    protected final RibbonLoadBalancerClient loadBalancer;

    private LoadBalancerProperties properties;
    private final List<HttpMessageReader<?>> messageReaders;


    public GrayLoadBalancerClientFilter(RibbonLoadBalancerClient loadBalancer,
                                        LoadBalancerProperties properties) {
        this.loadBalancer = loadBalancer;
        this.properties = properties;
        this.messageReaders = HandlerStrategies.withDefaults().messageReaders();

    }

    @Override
    public int getOrder() {
        return LOAD_BALANCER_CLIENT_FILTER_ORDER;
    }

    @Override
    @SuppressWarnings("Duplicates")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
        if (url == null
                || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
            return chain.filter(exchange);
        }
        // preserve the original url
        ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);

        if (log.isTraceEnabled()) {
            log.trace("LoadBalancerClientFilter url before: " + url);
        }

        return choose(exchange).doOnNext(instance -> {
            if (instance == null) {
                throw NotFoundException.create(properties.isUse404(),
                        "Unable to find instance for " + url.getHost());
            }
            ServerHttpRequest request = exchange.getRequest();
            URI uri = request.getURI();

            // if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
            // if the loadbalancer doesn't provide one.
            String overrideScheme = instance.isSecure() ? "https" : "http";
            if (schemePrefix != null) {
                overrideScheme = url.getScheme();
            }

            URI requestUrl = loadBalancer.reconstructURI(
                    new DelegatingServiceInstance(instance, overrideScheme), uri);

            if (log.isTraceEnabled()) {
                log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
            }
            exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);

        }).then(chain.filter(exchange));
    }

    protected Mono<ServiceInstance> choose(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap queryParams = request.getQueryParams();
        GrayParams grayParams = new GrayParams();
        if (!queryParams.isEmpty()) {
            grayParams.setParams(queryParams);
        }
        HttpHeaders headers = request.getHeaders();
        if (!headers.isEmpty()) {
            grayParams.setHeaders(headers.toSingleValueMap());
        }
        if (headers.getContentType().equals(MediaType.APPLICATION_JSON)) {
            ServerHttpRequest serverHttpRequest = exchange.getAttribute(ServerWebExchangeUtils.CACHED_SERVER_HTTP_REQUEST_DECORATOR_ATTR);
            ServerRequest serverRequest = ServerRequest.create(exchange.mutate().request(serverHttpRequest).build(),
                    messageReaders);

            Mono<String> requestBody = serverRequest.bodyToMono(String.class);
            return requestBody.flatMap(bodyStr -> {
                if (JSONUtil.isJson(bodyStr)) {
                    log.debug("gray params :" + JSONUtil.toPrettyString(bodyStr));
                    grayParams.getParams().putAll(JSONUtil.parseObject(bodyStr, Map.class));
                }
                return Mono.just(bodyStr);
            }).then(Mono.defer(() -> Mono.just(loadBalancer.choose(((URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR)).getHost(), grayParams))));
        } else {
            return Mono.defer(() -> Mono.just(loadBalancer.choose(((URI) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR)).getHost(), grayParams)));
        }
    }

}