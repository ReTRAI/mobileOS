package com.season.portal.auth;

import com.season.portal.PortalApplication;
import com.season.portal.auth.model.LoginModel;
import com.season.portal.dashboard.DashboardController;
import com.season.portal.language.LanguageService;
import com.season.portal.utils.validation.PasswordValidator;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class AuthController {

    private boolean validateLogin(String email, String pass){
        return (Objects.equals("pedro.di.moura@gmail.com", email)
                && Objects.equals("Qwerty123", pass));
    }

    @RequestMapping(value={"/login"})
    public ModelAndView login(){
        return loginView(new LoginModel());
    }

    @PostMapping(value={"/login"})
    public ModelAndView login(@Valid LoginModel model, BindingResult result){

        if(!result.hasErrors()){
            if(validateLogin(model.getEmail(), model.getPassword())){
                PortalApplication.login(model);
                return new DashboardController().dashboardView();
            }
            else{
                PortalApplication.addErrorKey("api_login_invalid");
            }
        }

        return loginView(model);
    }


    private ModelAndView loginView(LoginModel model){
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("loginModel", model);

        return PortalApplication.addStatus(mv);
    }
}
