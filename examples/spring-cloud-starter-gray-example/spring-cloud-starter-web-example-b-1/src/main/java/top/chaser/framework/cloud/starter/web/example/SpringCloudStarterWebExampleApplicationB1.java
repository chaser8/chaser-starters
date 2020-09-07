package top.chaser.framework.cloud.starter.web.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringCloudStarterWebExampleApplicationB1 {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStarterWebExampleApplicationB1.class, args);
    }
}