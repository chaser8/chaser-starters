package top.chaser.framework.starter.tkmybatis.example;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import top.chaser.framework.starter.tkmybatis.example.model.MallInfo;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.starter.tkmybatis.example.service.MallInfoService;

import java.util.Arrays;

@Component
@Slf4j
public class Runner implements ApplicationRunner {
    @Autowired
    ApplicationContext ctx;

    @Autowired
    MallInfoService mallInfoRepository;
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            MallInfo mallInfo = new MallInfo();
            mallInfo.setAgentId("1");
            mallInfo.setMallName(beanName);
            mallInfo.setMallNbr("123");
            mallInfo.setMallType("31");
            mallInfoRepository.insertSelective(mallInfo);
        }
        PageInfo<MallInfo> page = mallInfoRepository.page(new MallInfo(), 3, 1);

        log.info(JSONUtil.toPrettyString(page));
    }
}
