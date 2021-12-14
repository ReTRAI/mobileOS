package com.season.portal.dashboard;

import com.season.portal.PortalApplication;
import com.season.portal.auth.model.LoginModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@Controller
public class DashboardController {

    @RequestMapping(value={"/dashboard"})
    public ModelAndView dashboard(){
        return dashboardView();
    }

    public ModelAndView dashboardView(){
        ModelAndView mv = new ModelAndView("dashboard");

        HashMap<String, String> overview = new HashMap<String, String>();
        overview.put("val0", "200");
        overview.put("val1", "4");
        overview.put("val2", "21");
        overview.put("val3", "1300");

        HashMap<String, String> inactive = new HashMap<String, String>();
        inactive.put("val0", "201");
        inactive.put("val1", "5");
        inactive.put("val2", "22");
        inactive.put("val3", "1301");

        HashMap<String, String> expiring = new HashMap<String, String>();
        expiring.put("val0", "202");
        expiring.put("val1", "6");
        expiring.put("val2", "23");
        expiring.put("val3", "1302");

        HashMap<String, String> activations = new HashMap<String, String>();
        activations.put("val0", "203");
        activations.put("val1", "7");
        activations.put("val2", "24");
        activations.put("val3", "1303");

        mv.addObject("overview", overview);
        mv.addObject("inactive", inactive);
        mv.addObject("expiring", expiring);
        mv.addObject("activations", activations);
        return PortalApplication.addStatus(mv);
    }
}
