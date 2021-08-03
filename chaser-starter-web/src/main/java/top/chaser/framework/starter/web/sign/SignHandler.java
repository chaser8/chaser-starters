package top.chaser.framework.starter.web.sign;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.common.web.http.MediaType;
import top.chaser.framework.common.web.response.R;
import top.chaser.framework.starter.web.autoconfigure.SpringBootWebProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.TreeMap;

@Slf4j
public abstract class SignHandler {
    public abstract boolean handle(TreeMap<String,Object> body, HttpServletRequest request, SpringBootWebProperties.Sign signProperties);
    @SneakyThrows
    public void onValidateFailure(TreeMap<String,Object> body, HttpServletRequest request, HttpServletResponse response){
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        R<Void> fail = R.fail(WebErrorType.SIGN_ERROR, "签名验证失败");
        String responseBody = JSONUtil.toJSONString(fail);
        log.error(request.getServletPath()+" -> request body:{},response body:{}",JSONUtil.toJSONString(body),responseBody);
        ServletUtil.write(response,responseBody,MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
