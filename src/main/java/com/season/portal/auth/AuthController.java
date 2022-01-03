package com.season.portal.auth;

import com.season.portal.PortalApplication;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    private AuthenticationManager authManager;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
            LOGGER.error(ex.getMessage());
        }

        if(!result){
            PortalApplication.addErrorKey("api_login_invalid");
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
    public ModelAndView logout(HttpSession httpSession){

        SecurityContextHolder.getContext().setAuthentication(null);
        httpSession.invalidate();

        PortalApplication.addSuccessKey("api_logout_success");
        return new ModelAndView("redirect:/login");
    }
    /**/
}
