package com.season.portal.users.hierarchy;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.reseller.*;
import com.season.portal.client.generated.support.GetCountSupportFilteredResponse;
import com.season.portal.client.generated.support.GetSupportByUserIdResponse;
import com.season.portal.client.generated.support.GetSupportFilteredResponse;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.generated.user.GetCountUserFilteredResponse;
import com.season.portal.client.generated.user.GetUserFilteredResponse;
import com.season.portal.client.generated.user.User;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.client.users.ClientUser;
import com.season.portal.reseller.ResellerListPageModel;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.users.UserRoleModel;
import com.season.portal.users.UsersController;
import com.season.portal.users.UsersListPageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.ArrayList;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_SUP_ADMIN;

@Controller
public class HierarchyController extends ModelViewBaseController {
    @Autowired
    private UsersController usersController;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    ClientUser client;

    @Autowired
    ClientReseller clientReseller;

    @Autowired
    ClientSupport clientSupport;

    private String SESSION_HIERARCHY_CONTROLLER_RESELLER = "SESSION_HIERARCHY_CONTROLLER_RESELLER";
    private String SESSION_HIERARCHY_CONTROLLER_SUPPORT = "SESSION_HIERARCHY_CONTROLLER_SUPPORT";

    //<editor-fold desc="Add parent reseller">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/addParentResellerList")
    public ModelAndView addParentResellerList(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            String userId = model.getValue();

            GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(userId);
            if(resellerResponse != null) {
                Reseller r = resellerResponse.getReseller();
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_HIERARCHY_CONTROLLER_RESELLER, r);
                return parentResellerListView(
                        new HierarchyViewParentPageModel(r.getResellerId()));
            }
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();
    }
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/hierarchy/parentResellerList")
    public ModelAndView parentResellerList(@Valid HierarchyViewParentPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            return parentResellerListView(model);
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();

    }

    private ModelAndView parentResellerListView(HierarchyViewParentPageModel model){
        ModelAndView mv = new ModelAndView("support/hierarchy/reseller/hierarchyParentList");

        HttpSession session = request.getSession(true);
        Reseller r = (Reseller)session.getAttribute(SESSION_HIERARCHY_CONTROLLER_RESELLER);

        if(r != null) {
            mv.addObject("UserRole", new UserRoleModel(r));

            ArrayList<UserRoleModel> elements = new ArrayList<UserRoleModel>();

            long totalElements = 0;
            String parentName = (model.getParentName() == null)?"":model.getParentName();
            GetCountAvailableResellerParentResponse responseCount = clientReseller.countAvailableResellerParent(model.getChildId());
            //var responseCount = clientReseller.countResellerFiltered("", "", false);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetAvailableResellerParentResponse response = clientReseller.getAvailableResellerParent(model.getChildId(),
                    //var response = clientReseller.getResellerFiltered("", "", false,
                            model.getValidOffset(),
                            model.getValidNumPerPage(),
                            model.getValidSort(),
                            model.getValidOrder());

                    if(response != null){
                        ArrayList<Reseller> resellers = new ArrayList(response.getReseller());
                        elements = Utils.resellerToUserRole(resellers);
                    }
                }
            }

            Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

            mv.addObject("guidModel_back", new GuidRequiredModel(r.getUserId()));
            mv.addObject("elements", elements);
            mv.addObject("Pagination", pagination);
            mv.addObject("hierarchyViewParentPageModel", model);
            mv.addObject("hierarchyModel_addParent", new HierarchyModel("",r.getResellerId()));
            return dispatchView(mv);

        }
        return usersController.userViewBySession();
    }
    //</editor-fold>

    //<editor-fold desc="Add child reseller">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/viewResellerChildList")
    public ModelAndView viewResellerChildList(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            String userId = model.getValue();

            GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(userId);
            if(resellerResponse != null) {
                Reseller r = resellerResponse.getReseller();
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_HIERARCHY_CONTROLLER_RESELLER, r);
                return resellerChildListView(
                        new HierarchyViewChildPageModel(r.getResellerId()));
            }
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/hierarchy/resellerChildList")
    public ModelAndView resellerChildList(@Valid HierarchyViewChildPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            return resellerChildListView(model);
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();

    }

    private ModelAndView resellerChildListView(HierarchyViewChildPageModel model){
        ModelAndView mv = new ModelAndView("support/hierarchy/reseller/hierarchyChildList");

        HttpSession session = request.getSession(true);
        Reseller r = (Reseller)session.getAttribute(SESSION_HIERARCHY_CONTROLLER_RESELLER);

        if(r != null) {
            mv.addObject("UserRole", new UserRoleModel(r));

            ArrayList<UserRoleModel> elements = new ArrayList<UserRoleModel>();

            long totalElements = 0;

            GetCountResellerFilteredResponse responseCount = clientReseller.countResellerFiltered(
                    new ResellerListPageModel(r.getResellerId(), true));

            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetResellerFilteredResponse response = clientReseller.getResellerFiltered(
                            model.getParentId(),
                            model.getValidChildName(),
                            true,
                            model.getValidOffset(),
                            model.getValidNumPerPage(),
                            model.getValidSort(),
                            model.getValidOrder());

                    if(response != null){
                        ArrayList<Reseller> resellers = new ArrayList(response.getReseller());
                        elements = Utils.resellerToUserRole(resellers);
                    }
                }
            }

            Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

            mv.addObject("guidModel_back", new GuidRequiredModel(r.getUserId()));
            mv.addObject("elements", elements);
            mv.addObject("Pagination", pagination);
            mv.addObject("hierarchyViewChildPageModel", model);
            mv.addObject("hierarchyModel_removeChild", new HierarchyModel(r.getResellerId(),""));
            return dispatchView(mv);
        }
        return usersController.userViewBySession();
    }
    //</editor-fold>

    //<editor-fold desc="Add parent support">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/addParentSupportList")
    public ModelAndView addParentSupportList(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            String userId = model.getValue();

            GetSupportByUserIdResponse response = clientSupport.getSupportByUserId(userId);
            if(response != null) {
                Support s = response.getSupport();
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_HIERARCHY_CONTROLLER_SUPPORT, s);
                return parentSupportListView(
                        new HierarchyViewParentPageModel(s.getSupportId()));
            }
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/hierarchy/parentSupportList")
    public ModelAndView parentSupportList(@Valid HierarchyViewParentPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            return parentSupportListView(model);
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();

    }

    private ModelAndView parentSupportListView(HierarchyViewParentPageModel model){
        ModelAndView mv = new ModelAndView("support/hierarchy/support/hierarchyParentList");

        HttpSession session = request.getSession(true);
        Support s = (Support)session.getAttribute(SESSION_HIERARCHY_CONTROLLER_SUPPORT);

        if(s != null) {
            mv.addObject("UserRole", new UserRoleModel(s));

            ArrayList<UserRoleModel> elements = new ArrayList<UserRoleModel>();

            long totalElements = 0;
            //GetCountAvailableResellerParentResponse responseCount = clientSupport.countAvailableSupportParent(model.getChildId());
            var responseCount = clientSupport.countSupportFiltered("", "", false);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    //GetAvailableResellerParentResponse response = clientSupport.getAvailableSupportParent(model.getChildId(),
                    var response = clientSupport.getSupportFiltered("", "", false,
                            model.getValidOffset(),
                            model.getValidNumPerPage(),
                            model.getValidSort(),
                            model.getValidOrder());

                    if(response != null){
                        ArrayList<Support> supports = new ArrayList(response.getSupport());
                        elements = Utils.supportToUserRole(supports);
                    }
                }
            }

            Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

            mv.addObject("guidModel_back", new GuidRequiredModel(s.getUserId()));
            mv.addObject("elements", elements);
            mv.addObject("Pagination", pagination);
            mv.addObject("hierarchyViewParentPageModel", model);
            mv.addObject("hierarchyModel_addParent", new HierarchyModel("",s.getSupportId()));
            return dispatchView(mv);

        }
        return usersController.userViewBySession();
    }
    //</editor-fold>

    //<editor-fold desc="Add child support">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/viewSupportChildList")
    public ModelAndView viewSupportChildList(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            String userId = model.getValue();

            GetSupportByUserIdResponse response = clientSupport.getSupportByUserId(userId);
            if(response != null) {
                Support s = response.getSupport();
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_HIERARCHY_CONTROLLER_SUPPORT, s);
                return supportChildListView(
                        new HierarchyViewChildPageModel(s.getSupportId()));
            }
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/hierarchy/supportChildList")
    public ModelAndView supportChildList(@Valid HierarchyViewChildPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            return supportChildListView(model);
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return usersController.userViewBySession();

    }

    private ModelAndView supportChildListView(HierarchyViewChildPageModel model){
        ModelAndView mv = new ModelAndView("support/hierarchy/support/hierarchyChildList");

        HttpSession session = request.getSession(true);
        Support s = (Support)session.getAttribute(SESSION_HIERARCHY_CONTROLLER_SUPPORT);

        if(s != null) {
            mv.addObject("UserRole", new UserRoleModel(s));

            ArrayList<UserRoleModel> elements = new ArrayList<UserRoleModel>();

            long totalElements = 0;

            GetCountSupportFilteredResponse responseCount = clientSupport.countSupportFiltered(
                    new SupportListPageModel(s.getSupportId(), true));

            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetSupportFilteredResponse response = clientSupport.getSupportFiltered(
                            model.getParentId(),
                            model.getValidChildName(),
                            true,
                            model.getValidOffset(),
                            model.getValidNumPerPage(),
                            model.getValidSort(),
                            model.getValidOrder());

                    if(response != null){
                        ArrayList<Support> supports = new ArrayList(response.getSupport());
                        elements = Utils.supportToUserRole(supports);
                    }
                }
            }

            Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

            mv.addObject("guidModel_back", new GuidRequiredModel(s.getUserId()));
            mv.addObject("elements", elements);
            mv.addObject("Pagination", pagination);
            mv.addObject("hierarchyViewChildPageModel", model);
            mv.addObject("hierarchyModel_removeChild", new HierarchyModel(s.getSupportId(),""));
            return dispatchView(mv);
        }
        return usersController.userViewBySession();
    }
    //</editor-fold>




    //<editor-fold desc="User roles and hierarchy actions">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/removeChildReseller")
    public ModelAndView removeChildReseller(@Valid HierarchyModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientReseller.removeResellerAssociation(model.getParentId(), model.getChildId(), user.getUserId());
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return resellerChildListView(
                new HierarchyViewChildPageModel(model.getParentId()));
    }


    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/removeParentReseller")
    public ModelAndView removeParentReseller(@Valid HierarchyModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientReseller.removeResellerAssociation(model.getParentId(), model.getChildId(), user.getUserId());
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return usersController.userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/addParentReseller")
    public ModelAndView addParentReseller(@Valid HierarchyModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                if(clientReseller.validateSetResellerAssociation(
                        clientReseller.setResellerAssociation(model.getParentId(), model.getChildId(), user.getUserId()), true)){
                    return usersController.userViewBySession();
                }
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return parentResellerListView(new HierarchyViewParentPageModel(model.getChildId()));
    }



    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/removeChildSupport")
    public ModelAndView removeChildSupport(@Valid HierarchyModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientSupport.removeSupportAssociation(model.getParentId(), model.getChildId(), user.getUserId());
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return resellerChildListView(
                new HierarchyViewChildPageModel(model.getParentId()));
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/removeParentSupport")
    public ModelAndView removeParentSupport(@Valid HierarchyModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientSupport.removeSupportAssociation(model.getParentId(), model.getChildId(), user.getUserId());
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return usersController.userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/hierarchy/addParentSupport")
    public ModelAndView addParentSupport(@Valid HierarchyModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                if(clientSupport.validateSetSupportAssociation(
                        clientSupport.setSupportAssociation(model.getParentId(), model.getChildId(), user.getUserId()), true)){
                    return usersController.userViewBySession();
                }
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return parentSupportListView(new HierarchyViewParentPageModel(model.getChildId()));
    }
    //</editor-fold>

}
