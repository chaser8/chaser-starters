package top.chaser.framework.starter.uaa.authorization.controller.request;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录入参
 *
 * @author: chaser8
 * @date 2021/5/19 5:50 下午
 **/
@Getter
@Setter
public class PasswordLoginReq {
    /**
     * 登录帐号
     */
    @NotBlank
    @Length(min = 6, max = 50)
    private String userCode;
    /**
     * 密码
     */
    @NotBlank
    @Length(min = 6, max = 50)
    private String password;
}
