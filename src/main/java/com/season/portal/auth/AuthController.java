package com.season.portal.auth;

import com.season.portal.PortalApplication;
import com.season.portal.auth.model.LoginModel;
import com.season.portal.dashboard.DashboardController;
import com.season.portal.language.LanguageService;
import com.season.portal.utils.validation.PasswordValidator;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

@Controller
public class AuthController {

    private boolean validateLogin(String email, String pass){
        return (Objects.equals("pedro.di.moura@gmail.com", email)
                && Objects.equals("Qwerty123", pass));
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
    public ModelAndView login(@Valid LoginModel model, BindingResult result, HttpSession httpSession){

        if(!result.hasErrors()){
            if(validateLogin(model.getEmail(), model.getPassword())){
                if(PortalApplication.login(httpSession, model)){

                    return new ModelAndView("redirect:/dashboard");
                }
                else{
                    PortalApplication.addSuccessKey("api_login_invalid_session");
                }
            }
            else{
                PortalApplication.addErrorKey("api_login_invalid");
            }
        }

        return loginView(httpSession, model);
    }


    private ModelAndView loginView(HttpSession httpSession, LoginModel model){
        ModelAndView mv = new ModelAndView("login");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            mv.addObject("isLogged", "notLogged");
        }else{
            mv.addObject("isLogged", "Super Logged");
        }


        mv.addObject("loginModel", model);

        mv.addObject("userRole", httpSession.getAttribute("USER_ROLE"));

        return PortalApplication.addStatus(mv);
    }
}
