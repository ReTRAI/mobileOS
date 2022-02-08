package com.season.portal;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PortalController {

    @RequestMapping("/")
    public ModelAndView index(){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/dashboard");
    }
}
