package top.chaser.framework.starter.gateway.filter.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.chaser.framework.starter.gateway.autoconfigure.PredicateBody;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

@Slf4j
public class RequestBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<PredicateBody> {
    private List<HttpMessageReader<?>> messageReaders;

    public RequestBodyGatewayFilterFactory() {
        super(PredicateBody.class);
        this.messageReaders = HandlerStrategies.withDefaults().messageReaders();
    }


    @Override
    @SuppressWarnings("unchecked")
    public GatewayFilter apply(PredicateBody config) {
        return (exchange, chain) -> {
            ServerRequest serverRequest = ServerRequest.create(exchange,
                    this.messageReaders);
            Mono<String> body = serverRequest.bodyToMono(String.class)
                    .flatMap(originalBody -> getBody()
                            .apply(exchange, Mono.just(originalBody)));

            BodyInserter bodyInserter = BodyInserters.fromPublisher(body,
                    String.class);
            HttpHeaders headers = new HttpHeaders();
            headers.putAll(exchange.getRequest().getHeaders());
            headers.remove(HttpHeaders.CONTENT_LENGTH);

            CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange,
                    headers);
            return bodyInserter.insert(outputMessage, new BodyInserterContext())
                    .then(Mono.defer(() -> {
                        ServerHttpRequest decorator = decorate(exchange, headers,
                                outputMessage);

                        return chain.filter(exchange.mutate().request(decorator).build());
                    }));

        };
    }

    /**
     * 修改body
     *
     * @return apply 返回Mono<String>，数据是修改后的body
     */
    private BiFunction<ServerWebExchange, Mono<String>, Mono<String>> getBody() {
        return (exchange, json) -> {
            AtomicReference<String> result = new AtomicReference<>();
            json.subscribe(
                    value -> {
                        result.set(value);
                    }, throwable -> {
                        throw new RuntimeException(throwable);
                    }
            );
            String body = result.get();
            return Mono.just(body);
        };
    }

    private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers,
                                                CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("path", "value");
    }
}
