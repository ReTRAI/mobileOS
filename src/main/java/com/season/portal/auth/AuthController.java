package com.season.portal.auth;

import com.season.portal.PortalApplication;

import com.season.portal.utils.Utils;
import org.apache.tomcat.util.net.SSLSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.season.portal.utils.Utils.certificateExpireIn;


@Controller
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final int SESSION_MAX_INACTIVE_TIME_SEC = 300;

    private boolean validateLogin(HttpServletRequest request, String email, String pass){

        boolean result = false;
        String certificateEmail = "";

        X509Certificate[] certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
        if (certificates != null && certificates.length > 0) {
            certificateEmail = Utils.parseCertificate(certificates[0].getSubjectX500Principal(), "CN");
        }

        if(certificateEmail.equals(email)){
            try{
                UsernamePasswordAuthenticationToken authReq
                        = new UsernamePasswordAuthenticationToken(email, pass);
                Authentication auth = authManager.authenticate(authReq);

                if(auth.isAuthenticated()){
                    SecurityContext sc = SecurityContextHolder.getContext();
                    sc.setAuthentication(auth);

                    HttpSession session = request.getSession(true);
                    session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
                    session.setMaxInactiveInterval(SESSION_MAX_INACTIVE_TIME_SEC);

                    int expireIn = certificateExpireIn(certificates[0]);
                    session.setAttribute("CERTIFICATION_VALIDITY", expireIn);

                    result = true;
                }
                else{
                    PortalApplication.addErrorKey("api_login_invalid");
                }
            }
            catch (Exception ex){
                LOGGER.error(ex.getMessage());
                PortalApplication.addErrorKey("api_login_invalid");
            }
        }
        else{
            PortalApplication.addErrorKey("api_login_invalid_mismatch");
        }

        return result;
    }

    @RequestMapping(value={"/login"})
    public ModelAndView login(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return loginView(new LoginModel());

        }
        return new ModelAndView("redirect:/dashboard");

        //return loginView(new LoginModel());
    }

    @PostMapping(value={"/login"})
    public ModelAndView login(@Valid LoginModel model, BindingResult result, HttpServletRequest request){

        if(!result.hasErrors()){
            if(validateLogin(request, model.getEmail(), model.getPassword())){
                return new ModelAndView("redirect:/dashboard");
            }
        }

        return loginView( model);
    }


    private ModelAndView loginView(LoginModel model){
        ModelAndView mv = new ModelAndView("login");

        mv.addObject("loginModel", model);

        return PortalApplication.addStatus(mv);
    }

    /**/
    @RequestMapping(value={"/logout"})
    public void logout(HttpServletRequest request, HttpServletResponse response){

        PortalApplication.logout(request, response);
        PortalApplication.addSuccessKey("api_logout_success");


        //response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "https://localhost:8443/login");



        /** /
        ModelAndView mv = new ModelAndView("errorHandler");
        mv.addObject("errorTitle","api_logout_success");
        return PortalApplication.addStatus(mv);
        /**/
        //return new ModelAndView("redirect:/login");
        //return loginView(new LoginModel());
    }
}
