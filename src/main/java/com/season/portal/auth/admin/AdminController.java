package com.season.portal.auth.admin;

import com.season.portal.PortalApplication;
import com.season.portal.auth.LoginModel;
import com.season.portal.language.LanguageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.BoolModel;
import com.season.portal.utils.model.RestBoolModel;
import com.season.portal.utils.model.RestHashMapModel;
import com.season.portal.utils.model.RestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_ADMIN;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_ALL;
import static com.season.portal.utils.Utils.certificateExpireIn;


@RestController
public class AdminController extends ModelViewBaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final static String SESSION_ADMIN_VIEW = "SESSION_ADMIN_VIEW";

    @PreAuthorize(ALLOW_ROLES_ADMIN)
    @ResponseBody
    @PostMapping(value={"/toggleAdminView"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestBoolModel toggleAdminView(HttpServletRequest request){
        RestBoolModel restBoolModel = new RestBoolModel();

        if(request.isUserInRole("ROLE_ADMIN")){
            HttpSession session = request.getSession(true);
            boolean value = getAdminView(session);
            value = !value;
            session.setAttribute(SESSION_ADMIN_VIEW, value);
            restBoolModel.setResult(value, true);
        }
        return (RestBoolModel)PortalApplication.addStatus(restBoolModel);
    }


    public static void initAdminView(HttpSession session){
        if(session == null)
            session.setAttribute(SESSION_ADMIN_VIEW, false);
    }

    public static boolean getAdminView(HttpSession session){
        boolean adminView = false;
        if(session != null){
            try{
                adminView = (boolean)session.getAttribute(SESSION_ADMIN_VIEW);
            }catch(Exception e){

            }
        }
        return adminView;

    }

}
