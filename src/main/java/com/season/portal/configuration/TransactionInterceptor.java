package com.season.portal.configuration;

import com.season.portal.PortalApplication;
import com.season.portal.utils.Utils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.WebRequestHandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.cert.X509Certificate;

public class TransactionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        response.addHeader("Cache-Control", "private, no-cache, no-store, must-revalidate");

        // Your business logic goes here
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return true;
        }

        // return true or false depending on whether you want the controller to handle the request or terminate request processing.
        X509Certificate[] certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        String commonName = Utils.parseCertificate(certificates[0].getSubjectX500Principal(), "CN");
        String emailuserName = auth.getName();

        if(commonName.equals(emailuserName)){
            return true;
        }

        PortalApplication.logout(request, response);
        PortalApplication.addErrorKey("api_error_sessionExpired");

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "https://localhost:8443/login");

        return false;
    }
}
