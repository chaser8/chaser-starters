package top.chaser.framework.starter.uaa.resource.security;

import cn.hutool.core.convert.Convert;
import top.chaser.framework.starter.uaa.resource.ResourceServerJwtProperties;
import top.chaser.framework.starter.uaa.resource.ResourceServerProperties;
import top.chaser.framework.uaa.base.store.TokenStore;
import top.chaser.framework.uaa.base.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import top.chaser.framework.common.base.exception.AuthenticationException;
import top.chaser.framework.common.base.exception.SystemException;
import top.chaser.framework.common.web.exception.WebErrorType;
import top.chaser.framework.common.web.session.User;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 *
 * @author yangzb 20210525
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    protected TokenStore tokenStore;
    protected ResourceServerProperties resourceServerProperties;
    protected ResourceServerJwtProperties jwtProperties;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, TokenStore tokenStore, ResourceServerProperties resourceServerProperties, ResourceServerJwtProperties jwtProperties) {
        super(authenticationManager);
        this.tokenStore = tokenStore;
        this.resourceServerProperties = resourceServerProperties;
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || bearerToken.isEmpty()) {
            throw new AuthenticationException("Token为空");
        }
        User user = null;
        try {
            String token = bearerToken.replace("Bearer ", "");
            user = JwtUtil.getUserDetailFromToken(token, jwtProperties.getPublicKey());
            if (tokenStore.validate(user.getUserCode(), token)) {
                tokenStore.expire(user.getUserCode(), Convert.toInt(jwtProperties.getExpireSeconds()));
                return new UsernamePasswordAuthenticationToken(user, null, user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getCode())).collect(Collectors.toList()));
            }else {
                throw new SystemException(WebErrorType.AUTH_ERROR,"token已过期或无效");
            }
        } catch (ExpiredJwtException e) {
            log.error("Token已过期 ", e);
            throw new CredentialsExpiredException("token已过期");
        } catch (JwtException e) {
            log.error("Token 解析错误 ", e);
            throw new AccessDeniedException("token格式错误");
        } catch (IllegalArgumentException e) {
            log.error("非法参数异常" + e);
            throw new AccessDeniedException("非法参数异常");
        }catch (SystemException e) {
            throw new AccessDeniedException(e.getMessage());
        } catch (Exception e) {
            log.error("非法参数异常 ", e);
            throw new AccessDeniedException("非法参数异常");
        }
    }

}
