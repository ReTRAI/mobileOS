package com.season.portal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class PortalController {

    //@RequestMapping(value="**")
    //@RequestMapping("/{variable:(?!static).*}/**")
    public ModelAndView index(HttpSession httpSession){
        /** /
        if(httpSession.getAttribute("IS_LOGGED") != null){
            if((boolean)httpSession.getAttribute("IS_LOGGED")){
                return new ModelAndView("redirect:/dashboard");
            }
        }
         return new ModelAndView("redirect:/login");
        /**/

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth instanceof AnonymousAuthenticationToken){
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("redirect:/dashboard");
    }




}
