package top.chaser.framework.starter.web.core;

import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.chaser.framework.common.base.exception.SystemException;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.starter.web.autoconfigure.Config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @program:
 * @description:
 * @create: 2019-08-26 11:27
 **/
@Slf4j
public class SignInterceptor extends HandlerInterceptorAdapter {
    private Config.Sign sign;
    public SignInterceptor(Config.Sign sign){
        this.sign = sign;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestWrapper requestWrapper = (RequestWrapper) request;
        String sortBody = requestWrapper.getSortBody();
        String requestSign = request.getHeader(Config.Sign.KEY);
        Optional.ofNullable(requestSign).orElseThrow(() -> new SystemException(WebErrorType.SIGN_ERROR.clone().setMessage("无签名")));
        String sign = SecureUtil.md5(sortBody + this.sign.getSecretKey());
        if(requestSign.equals(sign)){
            return true;
        }else {
            throw new SystemException(WebErrorType.SIGN_ERROR.clone().setMessage("签名验证失败:"+requestSign));
        }
    }

}