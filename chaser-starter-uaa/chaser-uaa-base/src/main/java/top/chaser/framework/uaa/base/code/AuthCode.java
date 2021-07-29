package top.chaser.framework.uaa.base.code;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @author: chaser8
 * @date 2021/6/22 10:20 上午
 **/
@Getter
@Setter
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type", defaultImpl = AuthCode.class)
@JsonSubTypes({@JsonSubTypes.Type(value = AuthCode.class, name = "CAPTCHA"),
        @JsonSubTypes.Type(value = SmsAuthCode.class, name = "SMS")})
public class AuthCode implements Serializable {
    private String authCodeId;
    private String authCode;
    private AuthCodeType type;
}
