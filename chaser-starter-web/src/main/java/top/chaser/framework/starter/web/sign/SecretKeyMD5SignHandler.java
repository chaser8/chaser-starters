package top.chaser.framework.starter.web.sign;

import cn.hutool.crypto.SecureUtil;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.starter.web.autoconfigure.SpringBootWebProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.TreeMap;

public class SecretKeyMD5SignHandler extends SignHandler{
    @Override
    public boolean handle(TreeMap<String, Object> body, HttpServletRequest request, SpringBootWebProperties.Sign signProperties) {
        String requestSign = request.getHeader(signProperties.KEY);
        requestSign = Optional.ofNullable(requestSign).orElse("");
        String sign = SecureUtil.md5(JSONUtil.toJSONString(body) + signProperties.getSecretKey());
        if (requestSign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }
}
