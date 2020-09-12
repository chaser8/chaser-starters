package top.chaser.framework.starter.gateway.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudGatewayExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudGatewayExampleApplication.class, args);
    }
}