package top.chaser.framework.starter.gray.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.K;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.function.BiFunction;

@ConfigurationProperties(prefix = "gray")
@Getter
@Setter
@Validated
public class GrayProperties {
    public static final String DEFAULT_RULE_CLASS =  "top.chaser.framework.cloud.starter.gray.ribbon.GrayRule";
    @Valid
    private Strategys<String,Strategy> strategys = new Strategys<>();
    private String defaultRuleClass = DEFAULT_RULE_CLASS;
    private boolean enable = true;

    /***
     * 解决刷新配置后map合并不删除的问题
     * @description:
     * @author: 
     * @date 2020/9/9 8:54 下午
     **/
    @Validated
    public static class Strategys<K,V> extends HashMap<K,V>{
        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            this.clear();
            super.putAll(m);
        }
    }

    /***
     * 路由策略
     * @description:
     * @author:
     * @date 2020/9/2 2:28 下午
     **/
    @Getter
    @Setter
    @Validated
    @SuppressWarnings("all")
    public static class Strategy {
        private String defaultRuleClass;
        /**
         * 百分比 0-100
         */
        @NotNull
        private int percentage = 100;
        /**
         * 版本
         */
        @NotNull
        private String version;
        /**
         * 条件
         */
        @Valid
        private Set<Condition> conditions = new HashSet<>();
    }

    @Getter
    @Setter
    @Validated
    @SuppressWarnings("all")
    public static class Condition {
        /**
         * 类型，header or body
         */
        @NotNull
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

    public enum Type {
        HEADER, PARAM;
    }
}



