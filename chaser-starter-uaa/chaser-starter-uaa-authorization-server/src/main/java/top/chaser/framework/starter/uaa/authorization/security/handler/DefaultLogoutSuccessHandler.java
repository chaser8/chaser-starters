package top.chaser.framework.starter.uaa.authorization.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.http.MediaType;
import top.chaser.framework.common.web.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的退出成功处理器
 *
 * @author: chaser8
 * @date 2021/6/21 3:24 下午
 **/
public class DefaultLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(JSONUtil.toJSONString(R.success()));
    }
}
