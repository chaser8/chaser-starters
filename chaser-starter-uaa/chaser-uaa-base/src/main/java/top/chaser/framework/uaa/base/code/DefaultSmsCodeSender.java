/**
 *
 */
package top.chaser.framework.uaa.base.code;

import lombok.extern.slf4j.Slf4j;

/**
 * 短信验证码发送者
 * @author: chaser8
 * @date 2021/6/2 3:28 下午
 **/
@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {
    @Override
    public void send(String phone, String code){
        log.info("phone:{},code:{}",phone,code);
    }
}
