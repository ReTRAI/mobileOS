package com.season.portal.reseller;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.generated.dashboard.GetDashboardByResellerIdResponse;
import com.season.portal.client.generated.reseller.*;
import com.season.portal.client.generated.support.GetCountSupportFilteredResponse;
import com.season.portal.client.generated.support.GetSupportByUserIdResponse;
import com.season.portal.client.generated.support.GetSupportParentByChildIdResponse;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.generated.user.*;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.users.UserRoleModel;
import com.season.portal.users.UsersListPageModel;
import com.season.portal.users.hierarchy.HierarchyModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidModel;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.model.StringPageModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class ResellerController extends ModelViewBaseController {
    @Autowired
    ClientReseller clientReseller;

    private String SESSION_RESELLER_CONTROLLER_LIST_MODEL = "SESSION_RESELLER_CONTROLLER_LIST_MODEL";
    private String SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY = "SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY";


    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers/listBack")
    public ModelAndView resellersBySession(){
        HttpSession session = request.getSession(true);
        ResellerListPageModel model = (ResellerListPageModel)session.getAttribute(SESSION_RESELLER_CONTROLLER_LIST_MODEL);
        if(model == null){
            model = new ResellerListPageModel();
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        return resellersView(model);
    }
    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers")
    public ModelAndView resellers(ResellerListPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_LIST_MODEL, model);
        }

        return resellersView(model);
    }

    private ModelAndView resellersView(ResellerListPageModel model){
        ModelAndView mv = new ModelAndView("reseller/resellers");
        ArrayList<Reseller> elements = new ArrayList<Reseller>();
        long totalElements = 0;

        Reseller r = getPrincipalReseller(clientReseller);
        if(r != null) {
            boolean onlyChildren = true;
            String resellerId = r.getResellerId();
            if(request.isUserInRole("ROLE_ADMIN")){
                HttpSession session = request.getSession(true);
                if(AdminController.getAdminView(session)){
                    resellerId = "";
                }
            }

            model.setResellerId(resellerId);
            model.setOnlyChildren(onlyChildren);
            GetCountResellerFilteredResponse responseCount = clientReseller.countResellerFiltered(model);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetResellerFilteredResponse response = clientReseller.getResellerFiltered(model);
                    if(response != null){
                        elements = new ArrayList(response.getReseller());
                    }
                }
            }

        }
        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);


        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("resellerListPageModel", model);
        mv.addObject("guidModel_viewReseller", new GuidRequiredModel());

        return dispatchView(mv);
    }



    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/reseller")
    public ModelAndView reseller(@Valid GuidModel model, BindingResult result) {
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY, new ArrayList<Reseller>());
            return resellerViewById(model.getValue());

        }
        return resellerView(null);
    }

    public ModelAndView resellerViewById(String resellerId){

        GetResellerByIdResponse response = clientReseller.getResellerById(resellerId);
        if(response != null){
            return resellerView(response.getReseller());
        }
        return resellerView(null);
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers/resellerHistory")
    public ModelAndView resellerParentViewBySession(){
        HttpSession session = request.getSession(true);
        ArrayList<Reseller> viewedResellers = (ArrayList<Reseller>)session.getAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY);
        Reseller reseller = null;
        int size = viewedResellers.size();
        if (viewedResellers == null){
            viewedResellers = new ArrayList<Reseller>();
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY, viewedResellers);
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        else if(size > 1){//The Parent
            viewedResellers.remove(size-1);//The Last sawed
            reseller = viewedResellers.remove(size-2);//The Parent
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY, viewedResellers);
        }
        else if(size == 1){//The Last sawed
            reseller = viewedResellers.remove(size-1);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY, viewedResellers);
            PortalApplication.addErrorKey("api_reseller_noParentSessionReseller");
        }
        else{
            PortalApplication.addErrorKey("api_reseller_noSessionReseller");
        }

        return resellerView(reseller);
    }


    public ModelAndView resellerViewBySession(){
        HttpSession session = request.getSession(true);
        ArrayList<Reseller> viewedResellers = (ArrayList<Reseller>)session.getAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY);
        Reseller lastReseller = null;
        if (viewedResellers == null){
            viewedResellers = new ArrayList<Reseller>();
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY, viewedResellers);
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        else if(viewedResellers.size()>0){
            lastReseller = viewedResellers.remove(viewedResellers.size()-1);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY, viewedResellers);
        }
        else{
            PortalApplication.addErrorKey("api_reseller_noSessionReseller");
        }

        return resellerView(lastReseller);
    }

    private ModelAndView resellerView(Reseller reseller){
        ModelAndView mv = new ModelAndView("reseller/view");
        mv.addObject("Reseller", reseller);

        HttpSession session = request.getSession(true);
        ArrayList<Reseller> viewedResellers = (ArrayList<Reseller>)session.getAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY);
        if(viewedResellers == null){
            viewedResellers = new ArrayList<Reseller>();
        }
        mv.addObject("resellerHistory", (viewedResellers.size() > 0));


        if(reseller != null){
            viewedResellers.add(reseller);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLERS_HISTORY, viewedResellers);
        }

        return dispatchView(mv);
    }
}
