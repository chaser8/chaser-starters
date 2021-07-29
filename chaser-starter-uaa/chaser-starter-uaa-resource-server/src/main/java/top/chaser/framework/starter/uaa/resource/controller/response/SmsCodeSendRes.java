package top.chaser.framework.starter.uaa.resource.controller.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 发送短信验证码入参
 *
 * @author: chaser8
 * @date 2021/5/19 5:50 下午
 **/
@Getter
@Setter
@Accessors(chain = true)
public class SmsCodeSendRes {
    /**
     * 登录帐号
     */
    private String authCodeId;


    /**
     * 下次获取间隔时间（秒）
     */
    private long intervalSeconds;
}
