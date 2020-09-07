package top.chaser.framework.cloud.starter.web.example.controller;


import org.springframework.cloud.openfeign.FeignClient;
import top.chaser.framework.common.web.annotation.GetMapping;
import top.chaser.framework.common.web.response.R;

@FeignClient(name = "nacos-provider-b")
public interface BFeignClient {
    @GetMapping(value = "mall/feign")
    R feignServer();
}
