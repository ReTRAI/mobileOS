package com.season.portal.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //UserName can be anything, name, email...


        /** /
        UserDetails result = null;

        User u = WS.getUserByName(username);
        if(u == null)
            throw new UsernameNotFoundException("User not found");
        result = new MyUserDetails(u);

        /**/

        UserDetails result = new MyUserDetails(username);

        return result;
    }
}
