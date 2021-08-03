package top.chaser.framework.uaa.base.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.common.web.http.MediaType;
import top.chaser.framework.common.web.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 认证失败的默认处理器
 * @author: chaser8
 * @date 2021/5/20 3:45 下午
 **/
public class DefaultAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter printWriter = response.getWriter();
        String body = JSONUtil.toJSONString(R.fail(WebErrorType.AUTH_ERROR, exception.getMessage()));
        printWriter.write(body);
        printWriter.flush();
    }
}
