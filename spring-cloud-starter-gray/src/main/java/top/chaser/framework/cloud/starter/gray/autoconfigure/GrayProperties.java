package top.chaser.framework.cloud.starter.gray.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ConfigurationProperties(prefix = "chaser.gray")
@Getter
@Setter
public class GrayProperties {
    private List<Strategy> strategys = new ArrayList<>();
    private boolean enable = true;

    /***
     * 路由策略
     * @description:
     * @author:
     * @date 2020/9/2 2:28 下午
     **/
    @Getter
    @Setter
    @SuppressWarnings("all")
    public static class Strategy {
        /**
         * 服务名，对应注册中心的服务名
         */
        private String serviceName;
        /**
         * 百分比 0-100
         */
        private int percentage = 100;
        /**
         * 版本
         */
        private String version;
        /**
         * 条件
         */
        private Set<Condition> conditions = new HashSet<>();
    }

    @Getter
    @Setter
    @SuppressWarnings("all")
    public static class Condition {
        /**
         * 类型，header or body
         */
        private Type type = Type.PARAM;
        /**
         * 属性名<br/>
         * json多级，user.name
         * {"user":{"name":"zhangsan"}}
         */
        @NonNull
        private String name;
        /**
         * 值
         */
        @NonNull
        private String value;
    }

    public static enum Type {
        HEADER, PARAM;
    }
}



