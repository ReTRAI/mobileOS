package com.season.portal.auth;

import com.season.portal.auth.model.LoginModel;
import com.season.portal.language.LanguageService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AuthController {

    @RequestMapping(value={"/login"})
    public ModelAndView login(){
        return loginView(new LoginModel());
    }

    @PostMapping(value={"/login"})
    public ModelAndView login(@Valid LoginModel model, BindingResult result){

        if(result.hasErrors()){
            System.out.println("***********Errors****************************\n");
        }
        else{
            if(model.getEmail() == "batman" && model.getPassword() == "Qwerty123"){
                //Fazer o login
                System.out.println("***********Logou****************************\n");
            }
            else{


                System.out.println("***********email or password are invalid********\n");
            }
        }

        return loginView(model);
    }


    private ModelAndView loginView(LoginModel model){
        ModelAndView mv = new ModelAndView("login");
        mv.addObject("loginModel", model);
        mv.addObject("langCodes", LanguageService.LANGUAGE_CODES);
        return mv;
    }
}
