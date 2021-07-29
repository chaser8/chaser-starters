package top.chaser.framework.starter.uaa.authorization.security.handler;

import top.chaser.framework.uaa.base.store.TokenStore;
import top.chaser.framework.uaa.base.util.JwtUtil;
import top.chaser.framework.starter.uaa.resource.ResourceServerJwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import top.chaser.framework.common.web.session.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 清除缓存的token信息
 * @author: chaser8
 * @date 2021/6/22 10:13 上午
 **/
@Slf4j
public class DefaultLogoutHandler  implements LogoutHandler {
    @Autowired
    private ResourceServerJwtProperties jwtProperties;
    @Autowired
    private TokenStore tokenStore;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader("Authorization");

        if (header != null || header.startsWith("Bearer ")) {
            try {
                String token = header.replace("Bearer ","");
                User user = JwtUtil.getUserDetailFromToken(token, jwtProperties.getPublicKey());
                tokenStore.remove(user.getUserCode());
            } catch (Exception e) {
                log.error("清除缓存的token信息",e);
            }
        }
    }
}
