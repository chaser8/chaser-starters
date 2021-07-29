package top.chaser.framework.starter.gray.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: bigdata-dev1
 * @description:
 * @author: chaser8
 * @create: 2019-03-22 14:10
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class GrayService2Application {
    public static void main(String[] args) {
        SpringApplication.run(GrayService2Application.class, args);
    }
}
