package top.chaser.framework.boot.starter.tkmybatis.service;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;
import top.chaser.framework.boot.starter.tkmybatis.model.MallInfo;
import top.chaser.framework.common.base.util.JSONUtil;

@SpringBootTest
@Slf4j
class TkServiceImplTest {

    @Autowired
    MallInfoTkService mallInfoService;

    @BeforeAll
    public static void beforeAll() {
//        log.info(sessionTemplate+"");
    }

    @Test
    void testPage() {
        log.info("testPage");
        for (int i = 0; i < 105; i++) {
            MallInfo mallInfo = new MallInfo();
            mallInfo.setAgentId("1");
            mallInfo.setMallName(i + "");
            mallInfo.setMallNbr("123");
            mallInfo.setMallType("31");
            mallInfoService.insertSelective(mallInfo);
        }
        Example example = new Example(MallInfo.class);
        example.and().andGreaterThan("mallId",80);
        PageInfo<MallInfo> page = mallInfoService.page(example, 1, 10);
        log.info(JSONUtil.toPrettyString(page));

        PageInfo<MallInfo> page1 = mallInfoService.page(new MallInfo(), 1, 10);
        log.info(JSONUtil.toPrettyString(page1));

    }
}