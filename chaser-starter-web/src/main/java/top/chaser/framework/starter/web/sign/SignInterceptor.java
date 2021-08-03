package top.chaser.framework.starter.web.sign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import top.chaser.framework.common.web.http.request.MultiReadHttpServletRequest;
import top.chaser.framework.starter.web.autoconfigure.SpringBootWebProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

/**
 * @program:
 * @description:
 * @create: 2019-08-26 11:27
 **/
@Slf4j
public class SignInterceptor implements HandlerInterceptor {

    private SpringBootWebProperties.Sign signProperties;
    private SignHandler signHandler;

    public SignInterceptor(SpringBootWebProperties.Sign signProperties,@NonNull SignHandler signHandler) {
        this.signProperties = signProperties;
        this.signHandler = signHandler;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(request instanceof MultiReadHttpServletRequest)){
            log.info("request not instanceof RequestWrapper >> "+handler);
            return true;
        }
        MultiReadHttpServletRequest multiReadHttpServletRequest = (MultiReadHttpServletRequest) request;
        TreeMap<String,Object> body = multiReadHttpServletRequest.getBody(TreeMap.class);
        boolean flag = this.signHandler.handle(body, request, signProperties);
        if(!flag){
            this.signHandler.onValidateFailure(body,request,response);
        }
        return flag;
    }
}
