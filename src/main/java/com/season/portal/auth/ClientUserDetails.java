package com.season.portal.auth;

import com.season.portal.client.generated.user.User;
import com.season.portal.client.users.ClientUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class ClientUserDetails implements UserDetails {
    private String userId;
    private String supportId;
    private String resellerId;
    private String userName;
    private String userEmail;
    private float resellerBalance;
    private boolean hasBalance = false;
    private ArrayList<GrantedAuthority> authorities;

    private boolean AccountNonExpired = false;
    private boolean AccountNonLocked = false;
    private boolean CredentialsNonExpired = false;
    private boolean Enabled = false;

    private String theme = "D";
    private String language = "D";
    private String status = "";

    public ClientUserDetails(User user, ArrayList<GrantedAuthority> authorities){
        userId = user.getUserId();
        userName = user.getUserName();
        userEmail = user.getUserEmail();
        theme = user.getThemePreference();
        language = user.getLanguagePreference();
        status = user.getUserStatus();
        this.authorities = authorities;
    }

    public boolean needChangPass() {
        return (status.equals(ClientUser.USER_STATUS.CHANGEPW.name()));
    }

    public boolean hasRole(String role){
        boolean has = false;
        for(GrantedAuthority authority : authorities){
            if(authority.getAuthority().equals(role)){
                has = true;
                break;
            }
        }
        return has;
    }

    public ArrayList<GrantedAuthority> getAuthoritiesList() {
        return authorities;
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

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserId() {
        return userId;
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

    public String getSupportId() {
        return supportId;
    }

    public void setSupportId(String supportId) {
        this.supportId = supportId;
    }

    public String getResellerId() {
        return resellerId;
    }

    public void setResellerId(String resellerId) {
        this.resellerId = resellerId;
    }

    public float getResellerBalance() {
        return resellerBalance;
    }

    public void setResellerBalance(float resellerBalance) {
        hasBalance = true;
        this.resellerBalance = resellerBalance;
    }

    public boolean haveBalance() {
        return hasBalance;
    }
}
