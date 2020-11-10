package top.chaser.framework.starter.tkmybatis.autoconfigure;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import tk.mybatis.mapper.autoconfigure.MybatisProperties;

/****
 *
 * @description:
 * @author:
 * @date 2020/8/14 5:31 下午
 **/
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@org.springframework.context.annotation.Configuration
@Slf4j
public class TkMybatisAutoConfiguration {
    private static String BASE_MAPPER_LOCATION = "classpath*:mapper/*Mapper.xml";
    private static String TYPE_ALIASES_PACKAGE = "top.chaser.framework.**.model.**";

    @Bean("chaserMybatisBeanPostProcessor")
    public BeanPostProcessor beanPostProcessor(){
        return new BeanPostProcessor(){
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                log.info("{}-{}",bean,beanName);
                if(bean instanceof MybatisProperties){
                    MybatisProperties mybatisProperties = (MybatisProperties)bean;
                    String[] originalMapperLocations = mybatisProperties.getMapperLocations();
                    String typeAliasesPackage = mybatisProperties.getTypeAliasesPackage();
                    if (typeAliasesPackage == null || typeAliasesPackage.trim().length() == 0) {
                        typeAliasesPackage = TYPE_ALIASES_PACKAGE;
                    } else {
                        typeAliasesPackage += "," + TYPE_ALIASES_PACKAGE;
                    }
                    mybatisProperties.setTypeAliasesPackage(typeAliasesPackage);
                    mybatisProperties.setMapperLocations(ArrayUtil.append(originalMapperLocations, BASE_MAPPER_LOCATION));
                }
                return bean;
            }
        };
    }
}