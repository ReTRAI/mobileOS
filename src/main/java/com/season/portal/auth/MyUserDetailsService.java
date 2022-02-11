package com.season.portal.auth;

import com.season.portal.client.users.ClientUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




@Service
public class MyUserDetailsService implements UserDetailsService {
    /*@Autowired
    ClientUser client;*/

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails result = new MyUserDetails(username);
        return result;
    }

    public UserDetails validateUser(String username, String password) throws BadCredentialsException {
        UserDetails result = null;

        try {

            /*GetUserByIdResponse userResponse = client.getUserById(1);
            result = new MyUserDetails(userResponse.getUser());*/

        } catch (UsernameNotFoundException exception) {
            throw new BadCredentialsException("invalid login details");
        }


        return result;
    }


}
