package top.chaser.framework.starter.swagger.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description:
 *
 * @return
 * @author yzb
 * @date 2019/2/25 16:52
 */
@ConditionalOnWebApplication
@ConfigurationProperties(prefix="chaser.swagger")
@Getter
@Setter
public class SwaggerProperties {
    private Boolean enable =false;
}
