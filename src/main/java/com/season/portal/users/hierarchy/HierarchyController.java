package com.season.portal.users.hierarchy;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.reseller.*;
import com.season.portal.client.generated.user.GetCountUserFilteredResponse;
import com.season.portal.client.generated.user.GetUserFilteredResponse;
import com.season.portal.client.generated.user.User;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.client.users.ClientUser;
import com.season.portal.reseller.ResellerListPageModel;
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
    private HttpServletRequest request;

    @Autowired
    ClientUser client;

    @Autowired
    ClientReseller clientReseller;

    @Autowired
    ClientSupport clientSupport;

    private String SESSION_HIERARCHY_CONTROLLER_PARENT_RESELLER_USER_ID = "SESSION_HIERARCHY_CONTROLLER_PARENT_RESELLER_USER_ID";

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/hierarchy/addParentResellerList")
    public ModelAndView addParentResellerList(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            String userId = model.getValue();

            GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(userId);
            if(resellerResponse != null) {
                Reseller r = resellerResponse.getReseller();
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_HIERARCHY_CONTROLLER_PARENT_RESELLER_USER_ID, userId);
                return parentResellerListView(
                        new HierarchyViewParentPageModel(r.getResellerId()));
            }
        }
        else{
            for (var e:result.getAllErrors()){
                PortalApplication.addErrorKey(e.getDefaultMessage());
            }
        }

        return new UsersController().userViewBySession();
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

        return new UsersController().userViewBySession();

    }

    private ModelAndView parentResellerListView(HierarchyViewParentPageModel model){
        ModelAndView mv = new ModelAndView("support/hierarchy/hierarchyParentList");

        HttpSession session = request.getSession(true);
        String userId = (String)session.getAttribute(SESSION_HIERARCHY_CONTROLLER_PARENT_RESELLER_USER_ID);
        GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(userId);

        if(resellerResponse != null) {
            Reseller r = resellerResponse.getReseller();
            mv.addObject("userRole", new UserRoleModel(r));

            ArrayList<UserRoleModel> elements = new ArrayList<UserRoleModel>();

            long totalElements = 0;
            String parentName = (model.getParentName() == null)?"":model.getParentName();
            GetCountResellerFilteredResponse responseCount = clientReseller.countResellerFiltered(model.getChildId(), parentName, false);

            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetResellerFilteredResponse response = clientReseller.getResellerFiltered(model.getChildId(), parentName, false,
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

            mv.addObject("elements", elements);
            mv.addObject("Pagination", pagination);
            mv.addObject("hierarchyViewParentPageModel", model);
            mv.addObject("hierarchyModel_addParent", new HierarchyModel("",r.getResellerId()));
            return dispatchView(mv);

        }
        return new UsersController().userViewById(userId);
    }

    //<editor-fold desc="User roles and hierarchy actions">

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
        return new UsersController().userViewBySession();
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
        return new UsersController().userViewBySession();
    }
    //</editor-fold>

}
