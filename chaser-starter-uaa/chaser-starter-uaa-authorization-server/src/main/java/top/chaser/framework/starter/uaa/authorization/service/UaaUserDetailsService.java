package top.chaser.framework.starter.uaa.authorization.service;

import com.google.common.collect.Sets;
import top.chaser.framework.starter.uaa.authorization.AuthorizationServerProperties;
import top.chaser.framework.starter.uaa.authorization.security.bean.DefaultUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import top.chaser.framework.common.web.session.Organization;
import top.chaser.framework.common.web.session.Privilege;
import top.chaser.framework.common.web.session.Role;
import top.chaser.framework.common.web.session.User;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

public abstract class UaaUserDetailsService implements UserDetailsService {

    @Autowired
    protected AuthorizationServerProperties serverProperties;

    public UserDetails loadUserByPhone(String phone) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.ofNullable(getUser(username)).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return new DefaultUserDetails(user);
    }

    public User loadUserExtra(User user) {
        Set<Role> userRoles = getUserRoles(user.getUserId());
        Organization userOrganization = getUserOrganization(user.getUserId());
        Set<Privilege> userPrivileges = getUserPrivileges(userRoles, user.getUserId());

        user.setOrg(userOrganization);
        user.setRoles(userRoles);
        user.setPrivileges(userPrivileges);
        return user;
    }


    /**
     * 更新错误次数，如果达到最大错误次数则锁定帐号
     *
     * @param username
     * @return void
     * @author yangzb
     * @date 2021/6/10 2:25 下午
     */
    public abstract void incrementPasswordErrorTimesAndLock(String username);

    /**
     * clearPasswordErrorTimes
     *
     * @param username
     * @return void
     * @author yangzb
     * @date 2021/6/10 2:25 下午
     */
    public abstract void clearPasswordErrorTimes(String username);

    /**
     * 锁定帐号
     *
     * @param username
     * @return void
     * @author yangzb
     * @date 2021/6/10 2:25 下午
     */
    public abstract void lock(String username);

    /**
     * 查询用户信息
     *
     * @param userCode
     * @return top.chaser.framework.common.web.session.User
     * @author yangzb
     * @date 2021/5/27 9:54 上午
     */
    public abstract User getUser(Serializable userCode);

    /**
     * 查询用户角色信息
     *
     * @param userId
     * @return java.util.Set<top.chaser.framework.common.web.session.Role>
     * @author yangzb
     * @date 2021/5/27 9:55 上午
     */
    public Set<Role> getUserRoles(Serializable userId) {
        return Sets.newHashSet();
    }

    /**
     * 查询用户机构信息
     *
     * @param userId
     * @return top.chaser.framework.common.web.session.Organization
     * @author yangzb
     * @date 2021/5/27 9:55 上午
     */
    public Organization getUserOrganization(Serializable userId) {
        return null;
    }

    /**
     * 查询用户权限
     *
     * @param roles
     * @param userId
     * @return java.util.Set<top.chaser.framework.common.web.session.Privilege>
     * @author yangzb
     * @date 2021/5/27 9:55 上午
     */
    public Set<Privilege> getUserPrivileges(Set<Role> roles, Serializable userId) {
        return Sets.newHashSet();
    }
}
