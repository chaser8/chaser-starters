package top.chaser.framework.starter.upload.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author yzb
 * @Description:
 * @return
 * @date 2019/2/25 16:52
 */
@ConditionalOnWebApplication
@ConfigurationProperties(prefix = "chaser.upload")
@Getter
@Setter
public class UploadProperties {
    private Boolean enable = true;
    private Storage storageType = Storage.LOCAL;
    private LocalStorage localStorage = new LocalStorage();
}
