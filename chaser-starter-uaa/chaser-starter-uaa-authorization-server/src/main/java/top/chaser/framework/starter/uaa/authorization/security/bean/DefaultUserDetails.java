package top.chaser.framework.starter.uaa.authorization.security.bean;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.chaser.framework.common.web.session.Role;
import top.chaser.framework.common.web.session.User;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author: chaser8
 * @date 2021/6/22 10:18 上午
 **/
public class DefaultUserDetails implements UserDetails {
    /**
     * 当前登录用户
     */
    @Getter
    private transient User currentUser;

    public DefaultUserDetails(User user) {
        if (user != null) {
            this.currentUser = user;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (currentUser == null && currentUser.getRoles() != null) {
            return null;
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : currentUser.getRoles()) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getCode());
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        if (currentUser == null) {
            return null;
        }
        return currentUser.getPassword();
    }

    @Override
    public String getUsername() {
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUserCode();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !(User.Status.LOCKED == currentUser.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return User.Status.NORMAL == currentUser.getStatus();
    }
}
