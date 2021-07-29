package top.chaser.framework.starter.uaa.authorization.security.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;

/**
 *
 * @author: chaser8
 * @date 2021/6/22 10:18 上午
 **/
public class PasswordAuthenticationManager implements AuthenticationManager {

    @Autowired
    private PasswordAuthenticationProvider authenticationProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication result = authenticationProvider.authenticate(authentication);

        if (Objects.nonNull(result)) {
            return result;
        }
        throw new ProviderNotFoundException("Authentication failed!");
    }
}
