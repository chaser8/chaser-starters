package top.chaser.framework.starter.uaa.resource.security.code;

import cn.hutool.core.util.StrUtil;
import top.chaser.framework.uaa.base.code.AuthCodeType;

/**
 * 存储短信验证码
 *
 * @author: chaser8
 * @date 2021/6/2 3:25 下午
 **/
public interface ValidateCodeStore {
    /**
     * 存储验证码
     *
     * @param type
     * @param key
     * @param value
     * @param expireSeconds
     * @return void
     * @author yangzb
     * @date 2021/6/8 3:22 下午
     */
    default void store(AuthCodeType type, String key, String value, int expireSeconds) {
        System.out.println(StrUtil.format("key:{},value:{}", key, value));
    }

    /**
     * 获取验证码
     *
     * @param type
     * @param key
     * @return java.lang.String
     * @author yangzb
     * @date 2021/6/8 3:22 下午
     */
    String get(AuthCodeType type, String key);

    /**
     * 获取验证码过期时间
     *
     * @param type
     * @param key
     * @return long
     * @author yangzb
     * @date 2021/6/8 3:22 下午
     */
    long expireSeconds(AuthCodeType type, String key);

    /**
     * 删除验证码
     *
     * @param type
     * @param key
     * @return void
     * @author yangzb
     * @date 2021/6/8 3:22 下午
     */
    void del(AuthCodeType type, String key);
}
