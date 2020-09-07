package top.chaser.framework.cloud.starter.web.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SpringCloudStarterWebExampleApplicationA1 {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStarterWebExampleApplicationA1.class, args);
    }
}