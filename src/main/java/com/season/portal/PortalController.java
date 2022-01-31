package com.season.portal;

import com.season.portal.client.users.ClientUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class PortalController {
    @Autowired
    ClientUser client;

    @RequestMapping("/")
    public ModelAndView index(HttpSession httpSession){
        var ola = client.getUserById(1);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/dashboard");
    }
}
