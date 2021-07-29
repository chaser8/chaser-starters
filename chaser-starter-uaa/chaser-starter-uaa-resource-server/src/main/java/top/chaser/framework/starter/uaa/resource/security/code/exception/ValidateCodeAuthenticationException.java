package top.chaser.framework.starter.uaa.resource.security.code.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * 验证码校验不通过
 * @author: chaser8
 * @date 2021/6/3 3:04 下午
 **/
public class ValidateCodeAuthenticationException extends AuthenticationException {

    public ValidateCodeAuthenticationException(String msg) {
        super(msg);
    }

    public ValidateCodeAuthenticationException(String msg, Throwable e) {
        super(msg, e);
    }

}
