package top.chaser.framework.starter.uaa.resource.service.impl;

import top.chaser.framework.starter.uaa.resource.service.PermissionService;
import top.chaser.framework.uaa.base.store.TokenStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import top.chaser.framework.common.base.util.JSONUtil;
import top.chaser.framework.common.web.session.ApiResource;
import top.chaser.framework.common.web.session.Privilege;
import top.chaser.framework.common.web.session.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 判断权限服务,如果要自定义则实现PermissionService
 *
 * @program:
 * @description:
 * @author: chaser8
 * @date 2019/4/26 16:11
 **/
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Resource
    protected TokenStore tokenStore;

    /**
     * 可以做URLs匹配，规则如下
     * <p>
     * ？匹配一个字符
     * *匹配0个或多个字符
     * **匹配0个或多个目录
     * 用例如下
     * <p>https://www.cnblogs.com/zhangxiaoguang/p/5855113.html</p>
     */

    protected AntPathMatcher antPathMatcher = new AntPathMatcher();

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
        User principal = (User) authentication.getPrincipal();
        User user = tokenStore.getUserByUserCode(principal.getUserCode());

        String requestUrl = request.getRequestURI();
        Authentication auth = authentication;
        boolean hasPermission = false;

        Set<ApiResource> resources = user.getPrivileges().stream()
                .filter(privilege -> Privilege.Type.API == privilege.getType())
                .map(privilege -> (ApiResource) privilege)
                .collect(Collectors.toSet());

        principal:
        if (principal != null) {
            if (CollectionUtils.isEmpty(resources)) {
                break principal;
            }
            for (ApiResource resource : resources) {
                String urlPattern = resource.getUri();
                if (null != urlPattern && antPathMatcher.match(urlPattern, requestUrl)) {
                    hasPermission = true;
                    break principal;
                }
            }
        }
        //false 时打印日志
        if (!hasPermission) {
            log.debug("requestUrl:{},privileges:{}", requestUrl, JSONUtil.toJSONString(resources));
        }

        return hasPermission;
    }
}
