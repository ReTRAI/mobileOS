package com.season.portal.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import com.season.portal.client.users.generated.User;

public class MyUserDetails implements UserDetails {

    private String userName;
    private String password;
    private ArrayList<GrantedAuthority> authorities;

    public MyUserDetails(String userName){
        this.userName = userName;
        password = "Qwerty123dd";
        GrantedAuthority a = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities = new ArrayList<GrantedAuthority>();
        authorities.add(a);
    }

    public MyUserDetails(String userName, String sufixRole){
        this(userName);
        for (int i = authorities.toArray().length-1; i >=0; i--) {
            String authorotyName = authorities.get(i).toString();
            GrantedAuthority a = new SimpleGrantedAuthority(authorotyName+sufixRole);
            authorities.add(a);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
