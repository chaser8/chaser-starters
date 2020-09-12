package top.chaser.framework.starter.gray.ribbon;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.loadbalancer.IRule;
import top.chaser.framework.starter.gray.autoconfigure.GrayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

@Slf4j
public class RibbonSpringClientFactory extends SpringClientFactory {

    static final String NAMESPACE = "ribbon";

    protected GrayProperties properties;

    public RibbonSpringClientFactory(GrayProperties properties) {
        super();
        this.properties = properties;
    }

    @Override
    protected AnnotationConfigApplicationContext getContext(String name) {
        AnnotationConfigApplicationContext context = super.getContext(name);
        Map<String, GrayProperties.Strategy> strategys = properties.getStrategys();
        IRule bean = context.getBean(IRule.class);
        String originalRuleClassName = bean.getClass().getName();
        String propertyName = name + "." + NAMESPACE + "." + CommonClientConfigKey.NFLoadBalancerRuleClassName.key();
        if (strategys.containsKey(name) && properties.isEnable()) {
            GrayProperties.Strategy strategy = strategys.get(name);
            String configRuleClassName = StrUtil.isBlank(strategy.getDefaultRuleClass()) ? properties.getDefaultRuleClass() : strategy.getDefaultRuleClass();

            if (!configRuleClassName.equals(originalRuleClassName)) {
                log.debug("{}:create content rule,{} to {}", name, originalRuleClassName, configRuleClassName);
                System.setProperty(propertyName, configRuleClassName);
                try {
                    context = refreshContext(name);
                    log.debug("{}:created content", name, context.getBean(IRule.class).getClass().getName());
                } catch (Exception e) {
                    throw new RuntimeException("create ribbon rule error", e);
                }
            }
        } else {
            if (bean instanceof GrayRule) {
                log.debug("{}:remove content rule {}", name, originalRuleClassName);
                System.clearProperty(propertyName);
                context = refreshContext(name);
                log.debug("{}:removed content rule {} to {}", name, originalRuleClassName, context.getBean(IRule.class).getClass().getName());
            }
        }
        return context;
    }

    protected AnnotationConfigApplicationContext refreshContext(String name) {
        Map<String, AnnotationConfigApplicationContext> contexts = (Map<String, AnnotationConfigApplicationContext>) ReflectUtil.getFieldValue(this, "contexts");
        if (contexts.containsKey(name)) {
            contexts.remove(name);
        }
        return super.getContext(name);
    }
}
