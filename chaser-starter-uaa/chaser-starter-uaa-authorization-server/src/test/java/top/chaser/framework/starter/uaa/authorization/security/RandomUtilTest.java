package top.chaser.framework.starter.uaa.authorization.security;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;


public class RandomUtilTest {
    @Test
    public void test(){
        for (int i = 0; i < 1000; i++) {
            String i1 = RandomUtil.randomNumbers(4);
            System.out.println(i1);
        }
        System.out.println(RandomUtil.randomInt(0,9999));
    }
}
