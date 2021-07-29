package top.chaser.framework.starter.upload.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @program: dic-framework-v3
 * @description:
 * @author: chaser8
 * @create: 2019-03-05 10:56
 **/
@ConditionalOnWebApplication
@EnableConfigurationProperties(UploadProperties.class)
@ConditionalOnProperty(name = "chaser.upload.enable", havingValue = "true")
@ComponentScan(basePackages = {
        "top.chaser.framework.starter.upload.controller",
        "top.chaser.framework.starter.upload.service"
})
@MapperScan("top.chaser.framework.starter.upload.mapper")
@Slf4j
public class UploadAutoConfiguration {
//    @Bean
//    public UploadController uploadController(){
//        return new UploadController();
//    }
}
