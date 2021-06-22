package top.chaser.framework.starter.web.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program:
 * @description:
 * @author:
 * @create: 2019-05-30 15:58
 **/
@ControllerAdvice
@Slf4j
public class ChaserResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice {
    public ChaserResponseBodyAdvice(){
        log.error("123");
    }
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            log.error("ChaserResponseBodyAdvice >> {},selectedConverterType >> {},body >> {}",selectedContentType,selectedConverterType,body);
            if(request instanceof ServletServerHttpRequest){
                ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest)request;
                log.info("ChaserResponseBodyAdvice");
//                servletServerHttpRequest.getServletRequest().setAttribute(Consts.RESPONSE_BODY,body);
            }
        } catch (Exception e) {
            //不做异常处理
        }
        return body;
    }
}
