package com.season.portal.security;

import com.season.portal.PortalApplication;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        /*
        PortalApplication.addErrorKey("api_error_403");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            response.sendRedirect("/login");
        }

        response.sendRedirect( "/dashboard");
        */
        PortalApplication.addMsg("errorTitle","api_error_403_title");
        PortalApplication.addMsg("errorMsg","api_error_403_message");
        response.sendRedirect( "/errorHandler");
    }
}
