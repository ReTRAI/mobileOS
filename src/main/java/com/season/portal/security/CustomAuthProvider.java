package com.season.portal.security;
/*
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
    @Resource
    UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED":authentication.getName();

        UserDetails user = null;

        try{
            user = ((MyUserDetailsService)userDetailsService).loadToLogin(username);
        }catch (UsernameNotFoundException exception){
            throw new BadCredentialsException("invalid login details");
        }

        return createSuccessfulAuthentication(authentication, user);
    }

    private Authentication createSuccessfulAuthentication(Authentication authentication, UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
                user.getPassword(), (Collection<GrantedAuthority>) user.getAuthorities());
        token.setDetails(authentication.getDetails());

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
*/