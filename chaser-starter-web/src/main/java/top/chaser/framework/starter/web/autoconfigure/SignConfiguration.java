package top.chaser.framework.starter.web.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.chaser.framework.starter.web.core.SignInterceptor;

/**
 * @program:
 * @description:

 * @create: 2019-08-26 11:30
 **/
@Configuration
@ConditionalOnBean(Config.class)
public class SignConfiguration implements WebMvcConfigurer {

    @Autowired
    private Config config;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(config.getSign().isEnable()){
            registry.addInterceptor(new SignInterceptor(config.getSign())).addPathPatterns(config.getSign().getPatterns());
        }
    }
}
