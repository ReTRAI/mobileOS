package com.season.portal.resseler;

import com.season.portal.PortalApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class ResselerController {

    @GetMapping("/resselers")
    public ModelAndView resselersFilter(ResselerModel model) {



        return resselersView(model);
    }

    @RequestMapping(value={"/resselers"})
    public ModelAndView resselers(){
        return resselersView(new ResselerModel());
    }
    public ModelAndView resselersView(ResselerModel model){
        ModelAndView mv = new ModelAndView("resseler/resselers");

        HashMap<String, String> overview = new HashMap<String, String>();
        overview.put("val0", "200");
        overview.put("val1", "4");
        overview.put("val2", "21");
        overview.put("val3", "1300");

        mv.addObject("overview", overview);

        return PortalApplication.addStatus(mv);
    }

}
