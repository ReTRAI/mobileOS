package com.season.portal.auth;

import com.season.portal.client.generated.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class ClientUserDetails implements UserDetails {

    private String userName;
    private String userEmail;
    private ArrayList<GrantedAuthority> authorities;
    private boolean AccountNonExpired = false;
    private boolean AccountNonLocked = false;
    private boolean CredentialsNonExpired = false;
    private boolean Enabled = false;

    public ClientUserDetails(User user){
        userName = user.getUserName();
        userEmail = user.getUserEmail();

        GrantedAuthority a = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities = new ArrayList<GrantedAuthority>();
        authorities.add(a);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {return null;}

    @Override
    public String getUsername() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return AccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return AccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return CredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return Enabled;
    }
}
