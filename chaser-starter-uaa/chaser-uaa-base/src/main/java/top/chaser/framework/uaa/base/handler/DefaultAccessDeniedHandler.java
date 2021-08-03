package top.chaser.framework.uaa.base.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import top.chaser.framework.common.base.exception.SystemErrorType;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.http.MediaType;
import top.chaser.framework.common.web.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author: chaser8
 * @date 2021/5/19 3:22 下午
 **/
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        //登陆状态下，权限不足执行该方法
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter = response.getWriter();
        String body = JSONUtil.toJSONString(R.fail(SystemErrorType.FORBIDDEN_ERROR, "Access denied"));
        printWriter.write(body);
        printWriter.flush();
    }
}
