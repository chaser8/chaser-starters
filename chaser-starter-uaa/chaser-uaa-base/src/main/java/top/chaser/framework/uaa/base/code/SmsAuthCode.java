package top.chaser.framework.uaa.base.code;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SmsAuthCode extends AuthCode {
    /**
     * 手机号码
     */
    private String phoneNumber;
}
