package top.chaser.framework.starter.logging.example;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class Runner implements ApplicationRunner {
    @Autowired
    ApplicationContext ctx;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        MDC.put("requestId","12345");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        Arrays.stream(beanNames).forEach(s -> log.error(s));
    }
}
