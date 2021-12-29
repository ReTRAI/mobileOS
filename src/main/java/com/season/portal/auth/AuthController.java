package com.season.portal.auth;

import com.season.portal.PortalApplication;
import com.season.portal.auth.model.LoginModel;
import com.season.portal.dashboard.DashboardController;
import com.season.portal.language.LanguageService;
import com.season.portal.utils.validation.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;

    private boolean validateLogin(HttpServletRequest request, String email, String pass){

        boolean result = false;
        try{
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(email, pass);
            Authentication auth = authManager.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", sc);

            if(auth.isAuthenticated()){
               result = true;
            }
        }
        catch (Exception ex){

        }

        if(!result){
            PortalApplication.addErrorKey("api_login_invalid");
        }

        return result;
    }

    @RequestMapping(value={"/login"})
    public ModelAndView login(HttpSession httpSession){
        return loginView(httpSession, new LoginModel());
    }
    /**/
    @RequestMapping(value={"/logout"})
    public ModelAndView logout(HttpSession httpSession){

        PortalApplication.logout(httpSession);
        PortalApplication.addSuccessKey("api_logout_success");
        return new ModelAndView("redirect:/login");
    }
    /**/



    @PostMapping(value={"/login"})
    public ModelAndView login(@Valid LoginModel model, BindingResult result, HttpSession httpSession, HttpServletRequest request){

        if(!result.hasErrors()){
            if(validateLogin(request, model.getEmail(), model.getPassword())){

                if(PortalApplication.login(httpSession, model)){
                    return new ModelAndView("redirect:/dashboard");
                }
                else{
                    PortalApplication.addSuccessKey("api_login_invalid_session");
                }
            }

        }

        return loginView(httpSession, model);
    }


    private ModelAndView loginView(HttpSession httpSession, LoginModel model){
        ModelAndView mv = new ModelAndView("login");

        mv.addObject("loginModel", model);
        mv.addObject("userRole", httpSession.getAttribute("USER_ROLE"));

        return PortalApplication.addStatus(mv);
    }
}
