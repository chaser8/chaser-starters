package top.chaser.framework.starter.uaa.resource.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录认证服务
 *
 * @author: chaser8
 * @date 2021/5/20 9:40 上午
 **/
public interface PermissionService {
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
