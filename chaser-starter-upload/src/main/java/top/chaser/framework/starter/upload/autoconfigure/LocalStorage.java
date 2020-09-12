package top.chaser.framework.starter.upload.autoconfigure;

import cn.hutool.core.io.FileUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalStorage {
    /**
     * 绝对存储路径
     */
    private String absolutePath = FileUtil.getAbsolutePath("./")+"/upload";
}
