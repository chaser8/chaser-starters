package top.chaser.framework.starter.uaa.authorization.security.password;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import top.chaser.framework.uaa.base.store.TokenStore;
import top.chaser.framework.uaa.base.util.JwtUtil;
import top.chaser.framework.starter.uaa.authorization.AuthorizationServerJwtProperties;
import top.chaser.framework.starter.uaa.authorization.controller.request.PasswordLoginReq;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.chaser.framework.common.web.request.MultiReadHttpServletRequest;
import top.chaser.framework.common.web.session.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * 自定义JWT登录过滤器
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 *
 * @author: yangzb
 * @date 2021/5/20 3:14 下午
 **/
@Slf4j
public class PasswordAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    protected AuthorizationServerJwtProperties jwtProperties;
    protected TokenStore tokenStore;

    public PasswordAuthenticationProcessingFilter(@NonNull AuthorizationServerJwtProperties jwtProperties, @NonNull TokenStore tokenStore) {
        super(new AntPathRequestMatcher("/login", "POST"));
        this.tokenStore = tokenStore;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType() == null || !request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        UsernamePasswordAuthenticationToken authRequest;
        try {
            MultiReadHttpServletRequest wrappedRequest = MultiReadHttpServletRequest.newMultiReadHttpServletRequest(request);
            // 将前端传递的数据转换成jsonBean数据格式
            Optional<PasswordLoginReq> loginReqOpt = Optional.ofNullable(JSONUtil.toBean(wrappedRequest.getBody(), PasswordLoginReq.class));
            PasswordLoginReq passwordLoginReq = loginReqOpt.orElseThrow(() -> new AuthenticationServiceException("请输入正确的用户名和密码"));
            if(StrUtil.isBlank(passwordLoginReq.getUserCode())){
                throw new UsernameNotFoundException("请输入正确的用户名");
            }
            if(StrUtil.isBlank(passwordLoginReq.getPassword())){
                throw new AuthenticationCredentialsNotFoundException("请输入正确的登录密码");
            }
            authRequest = new UsernamePasswordAuthenticationToken(passwordLoginReq.getUserCode(), passwordLoginReq.getPassword(), null);
            authRequest.setDetails(authenticationDetailsSource.buildDetails(wrappedRequest));
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (AuthenticationException authenticationException) {
            throw authenticationException;
        } catch (Exception e) {
            log.error("", e);
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    // 认证成功(用户成功登录后，这个方法会被调用，我们在这个方法里生成token)
    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) {
        // builder the token
        String token = null;

        User principal = (User) auth.getPrincipal();
        token = JwtUtil.generateToken(principal,jwtProperties.getExpireSeconds(),jwtProperties.getPrivateKey());
        principal.setPassword("");
        tokenStore.storeToken(principal,token, Convert.toInt(jwtProperties.getExpireSeconds()));

        // 登录成功后，返回token到header里面
        response.addHeader("Authorization", "Bearer " + token);
        super.successfulAuthentication(request, response, chain, auth);
    }

}
