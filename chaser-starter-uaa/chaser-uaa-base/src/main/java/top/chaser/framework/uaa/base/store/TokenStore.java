package top.chaser.framework.uaa.base.store;


import top.chaser.framework.common.base.exception.AuthenticationException;
import top.chaser.framework.common.web.session.User;

/**
 * TokenStore接口
 *
 * @author: chaser8
 * @date 2021/6/22 10:19 上午
 **/
public interface TokenStore {
    public static final String TOKEN_KEY = "UAA:TOKEN:{}";
    public static final String USER_KEY = "UAA:USER:{}";

    /**
     * 存储token，key usrcode,value token
     *
     * @param user
     * @param token
     * @return void
     * @author yangzb
     * @date 2021/5/24 10:03 上午
     */
    void storeToken(User user, String token, int expireSeconds);

    /**
     * 通过userCode获取用户信息，包含机构，角色，权限
     *
     * @param userCode
     * @return top.chaser.framework.common.web.session.User
     * @author yangzb
     * @date 2021/5/24 9:37 上午
     */
    User getUserByUserCode(String userCode) throws AuthenticationException;

    /**
     * 延长有效期
     *
     * @param userCode
     * @return void
     * @author yangzb
     * @date 2021/6/1 3:25 下午
     */
    void expire(String userCode, int expireSeconds);


    /**
     * 验证token是否有效
     *
     * @param userCode
     * @param token
     */
    boolean validate(String userCode, String token);


    /**
     * 删除token
     *
     * @param userCode
     */
    void remove(String userCode);
}
