package com.season.portal.users;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.user.*;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class UsersController extends ModelViewBaseController {
    @Autowired
    ClientUser client;

    private String SESSION_USERS_CONTROLLER_LIST_MODEL = "SESSION_USERS_CONTROLLER_LIST_MODEL";

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
        mv.addObject("guidModel", new GuidModel());
        return dispatchView(mv);
    }

    @GetMapping("/users/new")
    public ModelAndView usersNew() {

        return usersNewView(new NewUserModel());
    }

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
                List<UserRole> roles = rolesResponse.getUserRole();
                mv.addObject("roles", roles);
            }

            mv.addObject("guidModel_addReseller", new GuidModel(user.getUserId()));
        }

        return dispatchView(mv);
    }

    @PostMapping("/users/addReseller")
    public ModelAndView addResseler(@Valid GuidModel model, BindingResult result) {
        if(!result.hasErrors()){

            return userViewById(model.getValue());
        }
        return userView(null);

    }

}
