package com.season.portal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PortalController {

    @RequestMapping(value={"/"})
    public String index(Model model){
        model.addAttribute("teste", "ola");
        return "index";
    }

}
