package top.chaser.framework.starter.web;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.chaser.framework.starter.web.autoconfigure.DefaultGlobalExceptionHandlerAdvice;
import top.chaser.framework.starter.web.autoconfigure.ErrorPageConfig;
import top.chaser.framework.starter.web.autoconfigure.SpringBootWebProperties;

/****
 *
 * @description:
 * @author:
 * @date 2020/8/14 5:31 下午
 **/
@ComponentScan(basePackages = {"top.chaser.framework"})
@Configuration
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class, WebMvcProperties.class,SpringBootWebProperties.class})
@ImportAutoConfiguration({DefaultGlobalExceptionHandlerAdvice.class, ErrorPageConfig.class})
@ConditionalOnWebApplication
@ServletComponentScan
public class SpringBootWebAutoConfiguration {
}