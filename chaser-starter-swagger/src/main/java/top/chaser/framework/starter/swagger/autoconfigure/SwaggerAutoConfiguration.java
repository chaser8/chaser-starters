package top.chaser.framework.starter.swagger.autoconfigure;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description:
 * @author: chaser8
 * @create: 2019-03-05 10:56
 **/
@ConditionalOnWebApplication
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "chaser.swagger.enable", havingValue = "true")
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerAutoConfiguration {
    @Bean
    public Docket api() {
        Docket docket =  new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);//去掉多余的400，401，404，500等
        return docket;
    }

    private ApiInfo buildApiInfo(){
        return new ApiInfoBuilder()
                .title("api")
                .description("api")
                .contact(new Contact("chaser", "http://www.chaser.top", "zhengbin_yang@msn.cn"))
                .build();
    }
}
