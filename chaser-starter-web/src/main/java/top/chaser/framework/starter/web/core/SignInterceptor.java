package top.chaser.framework.starter.web.core;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.common.web.request.MultiReadHttpServletRequest;
import top.chaser.framework.common.web.response.R;
import top.chaser.framework.starter.web.autoconfigure.SpringBootWebProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * @program:
 * @description:
 * @create: 2019-08-26 11:27
 **/
@Slf4j
public class SignInterceptor extends HandlerInterceptorAdapter {
    private SpringBootWebProperties.Sign sign;

    public SignInterceptor(SpringBootWebProperties.Sign sign) {
        this.sign = sign;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(request instanceof MultiReadHttpServletRequest)){
            log.info("request not instanceof RequestWrapper >> "+handler);
            return true;
        }
        MultiReadHttpServletRequest multiReadHttpServletRequest = (MultiReadHttpServletRequest) request;
        String body = multiReadHttpServletRequest.getSortBody();
        String requestSign = request.getHeader(SpringBootWebProperties.Sign.KEY);
        requestSign = Optional.ofNullable(requestSign).orElse("");
        String sign = SecureUtil.md5(body + this.sign.getSecretKey());

        if (requestSign.equals(sign)) {
            return true;
        } else {
            signError(body,request,response);
            return false;
        }
    }

    public void signError(String body,HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE+";charset=UTF-8");
        PrintWriter writer = response.getWriter();
        R<Void> fail = R.fail(WebErrorType.SIGN_ERROR, "签名验证失败");
        String responseBody = JSONUtil.toJSONString(fail);
        log.error(request.getServletPath()+" -> request body:{},response body:{}",body,responseBody);
        writer.println(responseBody);
        writer.flush();
        writer.close();
    }

}
