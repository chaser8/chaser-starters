package top.chaser.framework.starter.uaa.resource.security.code.sms;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import top.chaser.framework.uaa.base.code.AuthCodeType;
import top.chaser.framework.uaa.base.code.SmsAuthCode;
import top.chaser.framework.uaa.base.code.SmsCodeSender;
import top.chaser.framework.starter.uaa.resource.ResourceServerProperties;
import top.chaser.framework.starter.uaa.resource.security.code.ValidateCodeProcessor;
import top.chaser.framework.starter.uaa.resource.security.code.ValidateCodeStore;
import top.chaser.framework.starter.uaa.resource.security.code.exception.NoSuchValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import top.chaser.framework.common.base.exception.BusiException;
import top.chaser.framework.common.base.exception.SystemErrorType;
import top.chaser.framework.common.web.exception.WebErrorType;

import java.util.Optional;

/**
 * 短信处理
 * @author: yangzb
 * @date 2021/6/22 10:18 上午
 **/
public class SmsCodeProcessor extends ValidateCodeProcessor<SmsAuthCode> {
    @Autowired
    protected ResourceServerProperties resourceServerProperties;
    @Autowired(required = false)
    protected SmsCodeSender smsCodeSender;

    public static final AuthCodeType VALIDATE_CODE_TYPE = AuthCodeType.SMS;

    public SmsCodeProcessor(ValidateCodeStore validateCodeStore) {
        super(validateCodeStore, VALIDATE_CODE_TYPE);
    }

    @Override
    public SmsAuthCode create(SmsAuthCode authCode) {
        String phoneNumber = authCode.getPhoneNumber();
        if (!Validator.isMobile(phoneNumber)) {
            throw new BusiException(WebErrorType.PARAM_ERROR,"手机号码有误");
        }

        ResourceServerProperties.SmsCodeProperties smsCodeProperties = resourceServerProperties.getSmsCode();
        long ttl = validateCodeStore.expireSeconds(VALIDATE_CODE_TYPE, phoneNumber);
        long elapsedSeconds = smsCodeProperties.getExpireSeconds() - ttl;
        if (elapsedSeconds < smsCodeProperties.getSendIntervalSeconds()) {
            throw new BusiException(SystemErrorType.SYSTEM_ERROR, StrUtil.format("频繁获取验证码，请{}秒后再试", smsCodeProperties.getSendIntervalSeconds() - elapsedSeconds));
        }

        String randomNumbers = RandomUtil.randomNumbers(smsCodeProperties.getLength());
        smsCodeSender.send(phoneNumber, randomNumbers);
        String id = UUID.randomUUID().toString(true);
        authCode.setAuthCodeId(id);
        authCode.setAuthCode(randomNumbers);
        validateCodeStore.store(VALIDATE_CODE_TYPE, phoneNumber, authCode.getAuthCodeId() + "-" + randomNumbers, Convert.toInt(smsCodeProperties.getExpireSeconds()));
        return authCode;
    }

    @Override
    public boolean validate(SmsAuthCode authCode){
        String phoneNumber = authCode.getPhoneNumber();
        if (!Validator.isMobile(phoneNumber)) {
            throw new BusiException(WebErrorType.PARAM_ERROR,"手机号码有误");
        }
        String storeCode = Optional.ofNullable(validateCodeStore.get(VALIDATE_CODE_TYPE, phoneNumber)).orElseThrow(() -> new NoSuchValidateCodeException("验证码已过期，请重新获取"));
        String[] split = storeCode.split("-");
        if (split.length != 2 || !split[0].equals(authCode.getAuthCodeId())) {
            throw new NoSuchValidateCodeException("验证码已过期，请重新获取");
        }
        boolean flag = split[1].equals(authCode.getAuthCode().trim());
        if(flag){
            validateCodeStore.del(VALIDATE_CODE_TYPE, phoneNumber);
        }
        return flag;
    }
}
