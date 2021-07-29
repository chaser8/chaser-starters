package top.chaser.framework.starter.uaa.authorization.security.handler;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.http.MediaType;
import top.chaser.framework.common.web.response.R;
import top.chaser.framework.common.web.session.Privilege;
import top.chaser.framework.common.web.session.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 认证成功的处理器
 * @author: chaser8
 * @date 2021/5/21 10:02 上午
 **/
@Slf4j
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.debug("authentication success.");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        PrintWriter printWriter = response.getWriter();
        String token = response.getHeader("Authorization");
        User principal = (User)authentication.getPrincipal();
        Set<Privilege> privileges = principal.getPrivileges().stream().filter(privilege -> privilege.getType() != Privilege.Type.API).collect(Collectors.toSet());
        Map<String, Object> principalMap = BeanUtil.beanToMap(principal);
        principalMap.put("privileges",privileges);
        principalMap.remove("password");
        principalMap.put("accessToken",token.replace("Bearer ",""));
        String body = JSONUtil.toJSONString(R.success(principalMap));
        printWriter.write(body);
        printWriter.flush();
    }
}
