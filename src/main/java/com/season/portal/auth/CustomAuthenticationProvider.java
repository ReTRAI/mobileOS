package com.season.portal.auth;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.user.GetUserRolesByUserIdResponse;
import com.season.portal.client.generated.user.User;
import com.season.portal.client.generated.user.UserLoginResponse;
import com.season.portal.client.generated.user.UserRole;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    ClientUser client;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String email = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
        final String pass = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getCredentials().toString();

        ClientUserDetails userDetails = null;
        UserLoginResponse userResponse = client.tryLogin(email, pass);

        if(validateUserLoginResponse(userResponse)){
            User user = userResponse.getUser();
            GetUserRolesByUserIdResponse rolesResponse = client.getRolesById(user.getUserId());
            if(rolesResponse != null){
                List<UserRole> roles = rolesResponse.getUserRole();
                if(roles.size() > 0){
                    userDetails = new ClientUserDetails(user, Utils.rolesToGrantedAuthorities(roles));
                    return createSuccessfulAuthentication(authentication, userDetails);
                }
                else{
                    PortalApplication.addErrorKey("api_CustomAuthenticationProvider_authenticate_noRoles");
                }
            }
        }

        return createUnsuccessfulAuthentication();
    }

    private boolean validateUserLoginResponse(UserLoginResponse userResponse) {
        boolean valid = false;
        if(userResponse != null){
            /*User user = userResponse.getUser();

            if(!user.getBlocked()){

            }*/

            valid = true;

        }
        return valid;
    }

    @Override
    public boolean supports(Class < ? > authentication) {
        //return false;
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private Authentication createSuccessfulAuthentication(final Authentication authentication, final UserDetails user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
        token.setDetails(authentication.getDetails());
        return token;
    }

    private Authentication createUnsuccessfulAuthentication() {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(null, null, null );
        token.setAuthenticated(false);
        return token;
    }
}