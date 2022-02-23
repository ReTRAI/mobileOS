package com.season.portal.users;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.reseller.GetResellerByUserIdResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.reseller.SetResellerResponse;
import com.season.portal.client.generated.support.GetSupportByUserIdResponse;
import com.season.portal.client.generated.support.SetSupportResponse;
import com.season.portal.client.generated.user.*;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidModel;
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
import java.util.Date;
import java.util.List;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_ADMIN;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_SUP_ADMIN;

@Controller
public class UsersController extends ModelViewBaseController {
    @Autowired
    ClientUser client;

    @Autowired
    ClientReseller clientReseller;

    @Autowired
    ClientSupport clientSupport;

    private String SESSION_USERS_CONTROLLER_LIST_MODEL = "SESSION_USERS_CONTROLLER_LIST_MODEL";

    //<editor-fold desc="UserList">
    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/users/list")
    public ModelAndView usersList(@Valid UsersListPageModel model, BindingResult result) {
        /*model.setNumPerPage(10);
        if(!result.hasErrors()){

            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_USERS_CONTROLLER_LIST_MODEL, model);
        }*/

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

    private ModelAndView userViewById(String userId){

        GetUserByIdResponse response = client.getUserById(userId);
        if(response != null){
            return userView(response.getUser());
        }
        return userView(null);
    }

    private ModelAndView userView(User user){
        ModelAndView mv = new ModelAndView("support/users/view");
        mv.addObject("User", user);

        if(user != null){
            GetUserRolesByUserIdResponse rolesResponse = client.getRolesById(user.getUserId());

            if(rolesResponse != null){
                ArrayList<UserRole> roles = (ArrayList<UserRole>) rolesResponse.getUserRole();
                mv.addObject("roles", roles);

                for(UserRole role:roles){
                    switch(role.getUserRoleName()){
                        case "RESELLER":
                            GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(user.getUserId());
                            if(resellerResponse != null){
                                //Reseller r = resellerResponse.getReseller();
                                mv.addObject("reseller", resellerResponse.getReseller());
                            }
                            break;
                        case "SUPPORT":
                            GetSupportByUserIdResponse supportResponse = clientSupport.getSupportByUserId(user.getUserId());
                            if(supportResponse != null){
                                mv.addObject("support", supportResponse.getSupport());
                            }
                            break;
                        case "ADMIN":

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

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/addReseller")
    public ModelAndView addReseller(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientReseller.setReseller(model.getValue(), user.getUserId());
            }

            return userViewById(model.getValue());
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userView(null);
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/addSupport")
    public ModelAndView addSupport(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                clientSupport.setSupport(model.getValue(), user.getUserId());
            }

            return userViewById(model.getValue());
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userView(null);
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
            }

            return userViewById(model.getValue());
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userView(null);
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
        return userView(null);
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/users/user/activate")
    public ModelAndView activate(@Valid GuidModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                UnblockUserResponse response = client.activateUser(model.getValue(), user.getUserId());
                client.validateActivateResponse(response, true);
            }

            return userViewById(model.getValue());
        }
        for (var e:result.getAllErrors()){
            PortalApplication.addErrorKey(e.getDefaultMessage());
        }
        return userView(null);
    }
    //</editor-fold>
}
