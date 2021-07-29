/**
 *
 */
package top.chaser.framework.uaa.base.code;

/**
 * 短信验证码发送者
 * @author: chaser8
 * @date 2021/6/2 3:28 下午
 **/
public interface SmsCodeSender {
    void send(String phone, String code);
}
