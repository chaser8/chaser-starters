package top.chaser.framework.starter.uaa.resource.controller.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


/**
 * 发送短信验证码入参
 *
 * @author: chaser8
 * @date 2021/5/19 5:50 下午
 **/
@Getter
@Setter
public class SmsCodeSendReq {
    /**
     * 登录帐号
     */
    @NotBlank
    @Length(min = 11, max = 11)
    private String phoneNumber;
}
