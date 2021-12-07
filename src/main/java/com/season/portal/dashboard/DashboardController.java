package com.season.portal.dashboard;

import com.season.portal.PortalApplication;
import com.season.portal.auth.model.LoginModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardController {

    @RequestMapping(value={"/dashboard"})
    public ModelAndView dashboard(){
        return dashboardView();
    }

    public ModelAndView dashboardView(){
        ModelAndView mv = new ModelAndView("dashboard");

        return PortalApplication.addStatus(mv);
    }
}
