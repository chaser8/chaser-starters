package top.chaser.framework.starter.uaa.resource.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断权限服务,如果要自定义则实现PermissionService
 *
 * @program:
 * @description:
 * @author: chaser8
 * @date 2019/4/26 16:11
 **/
@Slf4j
public class NonPermissionServiceImpl extends PermissionServiceImpl {
    /**
     * 判断权限
     *
     * @param request
     * @param authentication
     * @return boolean
     * @throws
     * @Description:
     * @author yzb
     * @date 2019/4/26 16:10
     */
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return true;
    }
}
