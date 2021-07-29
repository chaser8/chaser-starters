package top.chaser.framework.starter.uaa.authorization.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * 密码错误异常
 * @author: chaser8
 * @date 2021/6/22 10:17 上午
 **/
public class PasswordErrorException extends BadCredentialsException {
    public PasswordErrorException(String msg) {
        super(msg);
    }

    public PasswordErrorException(String msg, Throwable t) {
        super(msg, t);
    }
}
