package top.chaser.framework.starter.uaa.resource.security.code.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码过期异常
 * @author: chaser8
 * @date 2021/6/3 3:04 下午
 **/
public class NoSuchValidateCodeException extends AuthenticationException {

    public NoSuchValidateCodeException(String msg) {
        super(msg);
    }

    public NoSuchValidateCodeException(String msg, Throwable e) {
        super(msg, e);
    }
}
