package top.chaser.framework.starter.uaa.resource.security.code;

import top.chaser.framework.uaa.base.code.AuthCode;
import top.chaser.framework.uaa.base.code.AuthCodeType;
import lombok.Getter;

/**
 * 验证码处理器
 *
 * @author: chaser8
 * @date 2021/6/3 11:34 上午
 **/
public abstract class ValidateCodeProcessor<T extends AuthCode> {
    public ValidateCodeProcessor(ValidateCodeStore validateCodeStore, AuthCodeType type) {
        this.validateCodeStore = validateCodeStore;
        this.type = type;
    }

    /**
     * 创建
     */
    public abstract T create(T validateCode);

    /**
     * 校验
     */
    public abstract boolean validate(T validateCode);

    protected ValidateCodeStore validateCodeStore;

    @Getter
    protected AuthCodeType type;
}
