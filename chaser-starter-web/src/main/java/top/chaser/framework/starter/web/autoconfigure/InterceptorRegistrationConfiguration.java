package top.chaser.framework.starter.web.autoconfigure;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.chaser.framework.starter.web.core.LogInterceptor;
import top.chaser.framework.starter.web.core.SignInterceptor;

import java.util.Arrays;

/**
 * @program:
 * @description:

 * @create: 2019-08-26 11:30
 **/
@Configuration
@ConditionalOnBean(SpringBootWebProperties.class)
public class InterceptorRegistrationConfiguration implements WebMvcConfigurer {

    private static final String [] DEFAULT_EXCLUDE_PATTERNS = {"/errors/**",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/**"};

    @Autowired
    private SpringBootWebProperties config;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(config.getSign().isEnable()){
            InterceptorRegistration interceptorRegistration = registry.addInterceptor(new SignInterceptor(config.getSign()));
            String[] patterns = config.getSign().getPatterns();
            String[] excludePatterns = config.getSign().getExcludePatterns();
            if(null != patterns && patterns.length>0){
                interceptorRegistration.addPathPatterns(patterns);
            }
            interceptorRegistration.excludePathPatterns(ArrayUtil.addAll(excludePatterns, DEFAULT_EXCLUDE_PATTERNS));
        }
        if(config.getLog().isEnable()){
            InterceptorRegistration interceptorRegistration = registry.addInterceptor(new LogInterceptor());
            String[] patterns = config.getLog().getPatterns();
            String[] excludePatterns = config.getLog().getExcludePatterns();
            if(null != patterns && patterns.length>0){
                interceptorRegistration.addPathPatterns(patterns);
            }
            interceptorRegistration.excludePathPatterns(ArrayUtil.addAll(excludePatterns, DEFAULT_EXCLUDE_PATTERNS));
        }
    }
}
