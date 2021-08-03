package top.chaser.framework.uaa.base.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import top.chaser.framework.common.base.exception.SystemErrorType;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.http.MediaType;
import top.chaser.framework.common.web.response.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author: chaser8
 * @date 2021/5/19 2:47 下午
 **/
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        //验证为未登陆状态会进入此方法，认证错误
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter printWriter = response.getWriter();
        String body = JSONUtil.toJSONString(R.fail(SystemErrorType.AUTH_ERROR, "please login"));
        printWriter.write(body);
        printWriter.flush();
    }
}
