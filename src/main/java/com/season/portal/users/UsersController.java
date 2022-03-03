package com.season.portal.users;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.reseller.GetCountResellerFilteredResponse;
import com.season.portal.client.generated.reseller.GetResellerByUserIdResponse;
import com.season.portal.client.generated.reseller.GetResellerParentByChildIdResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.GetCountSupportFilteredResponse;
import com.season.portal.client.generated.support.GetSupportByUserIdResponse;
import com.season.portal.client.generated.support.GetSupportParentByChildIdResponse;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.generated.user.*;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.client.users.ClientUser;
import com.season.portal.reseller.ResellerListPageModel;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidModel;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.users.hierarchy.HierarchyModel;
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

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_ADMIN;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_SUP_ADMIN;

@Controller
public class UsersController extends ModelViewBaseController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    ClientUser client;

    @Autowired
    ClientReseller clientReseller;

    @Autowired
    ClientSupport clientSupport;

    private String SESSION_USERS_CONTROLLER_LIST_MODEL = "SESSION_USERS_CONTROLLER_LIST_MODEL";
    private String SESSION_USERS_CONTROLLER_USER = "SESSION_USERS_CONTROLLER_USER";

    //<editor-fold desc="UserList">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/listBack")
    public ModelAndView usersListBySession(/*HttpServletRequest req*/){
        HttpSession session = request.getSession(true);
        UsersListPageModel model = (UsersListPageModel)session.getAttribute(SESSION_USERS_CONTROLLER_LIST_MODEL);
        if(model == null){
            model = new UsersListPageModel();
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        return usersListView(model);
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/list")
    public ModelAndView usersList(@Valid UsersListPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_USERS_CONTROLLER_LIST_MODEL, model);
        }
        return usersListView(model);
    }

    private ModelAndView usersListView(UsersListPageModel model){

        ModelAndView mv = new ModelAndView("support/users/usersList");
        ArrayList<User> elements = new ArrayList<User>();
        long totalElements = 0;

        GetCountUserFilteredResponse responseCount = client.countUserFiltered(model);

        if(responseCount != null){
            totalElements = responseCount.getResult();
            if(totalElements>0){
                GetUserFilteredResponse response = client.getUserFiltered(model);
                if(response != null){
                    elements = new ArrayList(response.getUser());
                }
            }
        }

        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);


        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("usersListPageModel", model);
        mv.addObject("guidModel", new GuidRequiredModel());
        return dispatchView(mv);
    }
    //</editor-fold>

    //<editor-fold desc="New User">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/new")
    public ModelAndView usersNew() {

        return usersNewView(new NewUserModel());
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping(value={"/users/new"})
    public ModelAndView usersNew(@Valid NewUserModel model, BindingResult result){

        if(!result.hasErrors()){
            if(model.getCheckPass().equals(model.getPass())) {
                ClientUserDetails user = Utils.getPrincipalDetails(true);
                if (user != null) {
                    SetUserResponse response = client.setUser(model.getEmail(), model.getNickname(), model.getPass(), user.getUserId());
                    if (response != null) {
                        return userView(response.getUser());
                    }
                }
            }
            else{
                PortalApplication.addErrorKey("api_passNotMatch");
            }
        }

        return usersNewView(model);
    }

    public ModelAndView usersNewView(NewUserModel model){
        ModelAndView mv = new ModelAndView("support/users/new");

        mv.addObject("newUserModel", model);

        return dispatchView(mv);
    }
    //</editor-fold>

    //<editor-fold desc="User">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/user")
    public ModelAndView user(@Valid GuidModel model, BindingResult result) {
        if(!result.hasErrors()){
            return userViewById(model.getValue());
        }
        return userView(null);
    }

    public ModelAndView userViewById(String userId){

        GetUserByIdResponse response = client.getUserById(userId);
        if(response != null){
            return userView(response.getUser());
        }
        return userView(null);
    }

    public ModelAndView userViewBySession(/*HttpServletRequest req*/){
        HttpSession session = request.getSession(true);
        User u = (User)session.getAttribute(SESSION_USERS_CONTROLLER_USER);
        if (u == null)
            PortalApplication.addErrorKey("api_session_invalidModel");
        return userView(u);
    }

    private ModelAndView userView(User user/*, HttpServletRequest req*/){
        ModelAndView mv = new ModelAndView("support/users/view");
        mv.addObject("User", user);

        if(user != null){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_USERS_CONTROLLER_USER, user);

            GetUserRolesByUserIdResponse rolesResponse = client.getRolesById(user.getUserId());

            if(rolesResponse != null){
                ArrayList<UserRole> roles = (ArrayList<UserRole>) rolesResponse.getUserRole();
                mv.addObject("roles", roles);

                for(UserRole role:roles){
                    switch(role.getUserRoleName()){
                        case "RESELLER":
                            GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(user.getUserId());
                            if(resellerResponse != null){
                                Reseller r = resellerResponse.getReseller();
                                mv.addObject("UserRole_Reseller", new UserRoleModel(r));
                                mv.addObject("guidModel_removeReseller", new GuidRequiredModel(r.getResellerId()));
                                GetResellerParentByChildIdResponse resellerParentResponse = clientReseller.getParent(r.getResellerId(), false);
                                mv.addObject("UserRole_ResellerParent", new UserRoleModel(resellerParentResponse));
                                if(resellerParentResponse != null){
                                    Reseller resellerParent = resellerParentResponse.getReseller();
                                    mv.addObject("hierarchyModel_removeParentReseller", new HierarchyModel(resellerParent.getResellerId(), r.getResellerId()));
                                }
                                else{
                                    mv.addObject("guidModel_addParentReseller", new GuidRequiredModel(user.getUserId()));
                                }
                                GetCountResellerFilteredResponse resellerChildsResponse = clientReseller.countResellerFiltered(
                                        new ResellerListPageModel(r.getResellerId(), true));
                                long resellerChilds = (resellerChildsResponse != null)?resellerChildsResponse.getResult():0;
                                mv.addObject("resellerChildCount", resellerChilds);
                                mv.addObject("guidModel_viewResellerChilds", new GuidRequiredModel(user.getUserId()));
                            }
                            break;
                        case "SUPPORT":
                            GetSupportByUserIdResponse supportResponse = clientSupport.getSupportByUserId(user.getUserId());
                            if(supportResponse != null){
                                Support s = supportResponse.getSupport();
                                mv.addObject("UserRole_Support", new UserRoleModel(s));
                                mv.addObject("guidModel_removeSupport", new GuidRequiredModel(s.getSupportId()));
                                GetSupportParentByChildIdResponse supportParentResponse = clientSupport.getParent(s.getSupportId(), false);
                                mv.addObject("UserRole_SupportParent", new UserRoleModel(supportParentResponse));
                                if(supportParentResponse != null){
                                    Support supportParent = supportParentResponse.getSupport();
                                    mv.addObject("hierarchyModel_removeParentSupport", new HierarchyModel(supportParent.getSupportId(), s.getSupportId()));
                                }
                                else{
                                    mv.addObject("guidModel_addParentSupport", new GuidRequiredModel(s.getUserId()));
                                }
                                GetCountSupportFilteredResponse supportChildsResponse = clientSupport.countSupportFiltered(
                                        new SupportListPageModel(s.getSupportId(), true));
                                long supportChilds = (supportChildsResponse != null)?supportChildsResponse.getResult():0;
                                mv.addObject("supportChildCount", supportChilds);
                                mv.addObject("guidModel_viewSupportChilds", new GuidRequiredModel(user.getUserId()));
                            }
                            break;
                        case "ADMIN":
                            mv.addObject("guidModel_removeAdmin", new GuidRequiredModel(user.getUserId()));
                            break;
                    }
                }
            }

            mv.addObject("guidModel_addReseller", new GuidRequiredModel(user.getUserId()));
            mv.addObject("guidModel_addSupport", new GuidRequiredModel(user.getUserId()));
            mv.addObject("guidModel_addAdmin", new GuidRequiredModel(user.getUserId()));
            mv.addObject("guidModel_unblock", new GuidRequiredModel(user.getUserId()));
            mv.addObject("guidModel_inactivate", new GuidRequiredModel(user.getUserId()));
            mv.addObject("guidModel_activate", new GuidRequiredModel(user.getUserId()));
        }

        return dispatchView(mv);
    }
    //</editor-fold>

    //<editor-fold desc="User roles">

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/addReseller")
    public ModelAndView addReseller(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientReseller.validateSetReseller(clientReseller.setReseller(model.getValue(), user.getUserId()),true);
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/removeReseller")
    public ModelAndView removeReseller(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientReseller.validateRemoveReseller(clientReseller.removeReseller(model.getValue(), user.getUserId()), true);
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }


    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/addSupport")
    public ModelAndView addSupport(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientSupport.validateSetSupport(clientSupport.setSupport(model.getValue(), user.getUserId()), true);
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/removeSupport")
    public ModelAndView removeSupport(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientSupport.validateRemoveSupport(clientSupport.removeSupport(model.getValue(), user.getUserId()), true);
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_ADMIN)
    @PostMapping("/users/addAdmin")
    public ModelAndView addAdmin(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                client.validateSetAdmin(client.setAdmin(model.getValue(), user.getUserId()), true);
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_ADMIN)
    @PostMapping("/users/removeAdmin")
    public ModelAndView removeAdmin(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                client.validateRemoveAdmin(client.removeAdmin(model.getValue(), user.getUserId()), true);
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }

    //</editor-fold>

    //<editor-fold desc="User actions">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/user/unblock")
    public ModelAndView unblock(@Valid GuidModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                UnblockUserResponse response = client.unblockUser(model.getValue(), user.getUserId());
                client.validateUnblockUserResponse(response, true);

                return userViewById(model.getValue());
            }
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/user/inactivate")
    public ModelAndView inactivate(@Valid GuidModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                InactivateUserResponse response = client.inactivateUser(model.getValue(), user.getUserId());
                client.validateInactivateUserResponse(response, true);
            }

            return userViewById(model.getValue());
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/user/activate")
    public ModelAndView activate(@Valid GuidModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                ActivateUserResponse response = client.activateUser(model.getValue(), user.getUserId());
                client.validateActivateResponse(response, true);
            }

            return userViewById(model.getValue());
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userViewBySession();
    }
    //</editor-fold>
}
