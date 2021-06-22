package top.chaser.framework.starter.uaa.resource.controller;

import top.chaser.framework.starter.uaa.resource.ResourceServerProperties;
import top.chaser.framework.starter.uaa.resource.controller.response.SmsCodeSendRes;
import top.chaser.framework.uaa.base.code.AuthCode;
import top.chaser.framework.uaa.base.code.AuthCodeType;
import top.chaser.framework.uaa.base.code.SmsAuthCode;
import top.chaser.framework.uaa.base.store.TokenStore;
import top.chaser.framework.starter.uaa.resource.controller.request.SmsCodeSendReq;
import top.chaser.framework.starter.uaa.resource.security.code.ValidateCodeProcessor;
import top.chaser.framework.starter.uaa.resource.security.code.ValidateCodeProcessorHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.chaser.framework.common.web.annotation.PostMapping;
import top.chaser.framework.common.web.controller.BaseController;
import top.chaser.framework.common.web.response.R;


/**
 * @author: yangzb
 * @date 2021/6/1 3:06 下午
 **/
@RequestMapping("/sms")
@RestController
@ConditionalOnProperty(name = "chaser.security.resource-server.sms-code.enable", havingValue = "true")
public class SmsController extends BaseController {

    @Autowired
    protected TokenStore tokenStore;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected ValidateCodeProcessorHolder validateCodeProcessorHolder;
    @Autowired
    protected ResourceServerProperties resourceServerProperties;

    @PostMapping(value = "send")
    public R<SmsCodeSendRes> sms(@RequestBody @Validated SmsCodeSendReq smsCodeSendReq) {
        ValidateCodeProcessor processor = validateCodeProcessorHolder.getProcessor(AuthCodeType.SMS);
        SmsAuthCode smsAuthCode = new SmsAuthCode();
        smsAuthCode.setType(AuthCodeType.SMS);
        smsAuthCode.setPhoneNumber(smsCodeSendReq.getPhoneNumber());
        AuthCode authCode = processor.create(smsAuthCode);
        return R.success(new SmsCodeSendRes().setAuthCodeId(authCode.getAuthCodeId()).setIntervalSeconds(resourceServerProperties.getSmsCode().getSendIntervalSeconds()));
    }
}
