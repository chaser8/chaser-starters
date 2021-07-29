package top.chaser.framework.starter.uaa.authorization.security.password;

import cn.hutool.core.util.StrUtil;
import top.chaser.framework.starter.uaa.authorization.AuthorizationServerProperties;
import top.chaser.framework.starter.uaa.authorization.exception.PasswordErrorException;
import top.chaser.framework.starter.uaa.authorization.security.bean.DefaultUserDetails;
import top.chaser.framework.starter.uaa.authorization.service.UaaUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsChecker;

/**
 * @author: chaser8
 * @date 2021/6/10 2:31 下午
 **/
@Slf4j
public class PasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    protected UaaUserDetailsService userDetailsService;

    @Autowired
    protected AuthorizationServerProperties serverProperties;

    protected UserDetailsChecker userDetailsChecker = new AccountStatusUserDetailsChecker();


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取前端表单中输入后返回的用户名、密码
        String userName = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        DefaultUserDetails user = (DefaultUserDetails) userDetailsService.loadUserByUsername(userName);
        userDetailsChecker.check(user);
        boolean isValid = user.getPassword().equals(password);
        // 验证密码
        if (!isValid) {
            try {
                userDetailsService.incrementPasswordErrorTimesAndLock(userName);
            } catch (Exception e) {
                log.error("更新密码连续错误次数失败", e);
            }

            throw new PasswordErrorException(StrUtil.format("密码错误：连续{}次失败将冻结账户，您还可以尝试{}次！"
                    , serverProperties.getMaxPasswordErrorTimes(),
                    serverProperties.getMaxPasswordErrorTimes() - user.getCurrentUser().getPasswordErrorTimes() - 1));
        }
        try {
            userDetailsService.clearPasswordErrorTimes(userName);
        } catch (Exception e) {
            log.error("清除密码错误次数失败", e);
        }
        userDetailsService.loadUserExtra(user.getCurrentUser());
        return new UsernamePasswordAuthenticationToken(user.getCurrentUser(), password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
