package com.season.portal.auth;

import com.season.portal.PortalApplication;

import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import org.apache.tomcat.util.net.SSLSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class AuthController extends ModelViewBaseController {
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
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(email, pass);
            Authentication auth = authManager.authenticate(authReq);

            if(auth.isAuthenticated()){
                SecurityContext sc = SecurityContextHolder.getContext();
                sc.setAuthentication(auth);

                HttpSession session = request.getSession(true);
                session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
                session.setMaxInactiveInterval(SESSION_MAX_INACTIVE_TIME_SEC);
                if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
                    AdminController.initAdminView(session);
                }

                int expireIn = certificateExpireIn(certificates[0]);
                session.setAttribute("CERTIFICATION_VALIDITY", expireIn);

                result = true;
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
                //return new ModelAndView("redirect:/dashboard");
                return new ModelAndView("redirect:/ticket/new");
            }
        }

        return loginView( model);
    }


    private ModelAndView loginView(LoginModel model){
        ModelAndView mv = new ModelAndView("login");

        mv.addObject("loginModel", model);

        return dispatchView(mv);
    }

    /**/
    @RequestMapping(value={"/logout"})
    public void logout(HttpServletRequest request, HttpServletResponse response){

        PortalApplication.logout(request, response);
        PortalApplication.addSuccessKey("api_logout_success");

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", portalConfig.getPortalURL("login"));
    }

    @RequestMapping(value={"/changePassword"})
    public ModelAndView changePassword(){
        return changePasswordView(new ChangePassModel());
    }

    @PostMapping(value={"/changePassword"})
    public ModelAndView changePassword(@Valid ChangePassModel model, BindingResult result){

        if(!result.hasErrors()){
            /*if(validateChangePass(request, model.getEmail(), model.getPassword())){
                //return new ModelAndView("redirect:/dashboard");
                return new ModelAndView("redirect:/ticket/new");
            }*/
        }

        return changePasswordView( model);
    }


    private ModelAndView changePasswordView(ChangePassModel model){
        ModelAndView mv = new ModelAndView("changePassword");
        mv.addObject("changePassModel", model);
        return dispatchView(mv);
    }
}
