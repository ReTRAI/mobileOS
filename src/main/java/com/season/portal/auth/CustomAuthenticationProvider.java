package com.season.portal.auth;

import com.season.portal.client.users.ClientUser;
import com.season.portal.client.users.generated.GetUserByIdResponse;
import com.season.portal.client.users.generated.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    ClientUser client;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        final String pass = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getCredentials().toString();
        // get user details using Spring security user details service

        return createSuccessfulAuthentication(authentication, new MyUserDetails(username));
        /*ClientUserDetails userDetails = null;
        try {
            GetUserByIdResponse userResponse = client.getUserById(1);
            if(validateUserResponseLogin(userResponse)){
                userDetails = new ClientUserDetails(userResponse.getUser());
                return createSuccessfulAuthentication(authentication, userDetails);
            }
        } catch (UsernameNotFoundException exception) {
            throw new BadCredentialsException("invalid login details");
        }

        return null;*/
    }

    private boolean validateUserResponseLogin(GetUserByIdResponse userResponse) {
        boolean valid = false;
        if(userResponse == null){
            User user = userResponse.getUser();
            if(user.getUserId() == 0) {
                /*
                if(!user.getBlocked()){

                }
                */
                valid = true;
            }
        }
        return valid;
    }

    @Override
    public boolean supports(Class < ? > authentication) {
        //return false;
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), authentication.getCredentials(), user.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }
}