package com.season.portal.dashboard;

import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.dashboard.ClientDashboard;
import com.season.portal.client.generated.dashboard.GetDashboardByResellerIdResponse;
import com.season.portal.client.generated.reseller.GetResellerByUserIdResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class DashboardController extends ModelViewBaseController {
    @Autowired
    ClientDashboard clientDashboard;

    @Autowired
    ClientReseller clientReseller;

    @PreAuthorize(ALLOW_ROLES_ALL)
    @RequestMapping(value={"/dashboard"})
    public ModelAndView dashboard(){
        return dashboardView();
    }

    private ModelAndView dashboardView(){
        ModelAndView mv = new ModelAndView("dashboard");
        mv.addObject("dashboard", null);

        ClientUserDetails user = Utils.getPrincipalDetails(true);
        if(user != null){
            if(request.isUserInRole("ROLE_RESELLER")){
                GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(user.getUserId());
                if(resellerResponse != null) {
                    Reseller r = resellerResponse.getReseller();
                    boolean recursiveAdmin = false;

                    if(request.isUserInRole("ROLE_ADMIN")){
                        HttpSession session = request.getSession(true);
                        recursiveAdmin = AdminController.getAdminView(session);
                    }
                    GetDashboardByResellerIdResponse dashboardResponse = clientDashboard.getDashboardByResellerId(r.getResellerId(), recursiveAdmin);

                    if(dashboardResponse != null){
                        mv.addObject("dashboard", true);
                        mv.addObject("overview", dashboardResponse.getGlobal());
                        mv.addObject("inactive", dashboardResponse.getInactive());
                        mv.addObject("expiring", dashboardResponse.getExpiring());
                        mv.addObject("activations", dashboardResponse.getActive());
                    }
                }
            }
        }

        return dispatchView(mv);
    }
}
