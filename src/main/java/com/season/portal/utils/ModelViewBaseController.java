package com.season.portal.utils;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.reseller.GetResellerByUserIdResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

public class ModelViewBaseController {
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected PortalConfiguration portalConfig;

    public ModelAndView dispatchView(ModelAndView mv ){
        mv.addObject("portalURL", portalConfig.getPortalURL());

        return PortalApplication.addStatus(mv, request);
    }

    public Reseller getPrincipalReseller(ClientReseller clientReseller){
        ClientUserDetails user = Utils.getPrincipalDetails(true);
        return getPrincipalReseller(clientReseller, user);
    }

    public Reseller getPrincipalReseller(ClientReseller clientReseller, ClientUserDetails user){
        Reseller r = null;

        if(user != null) {
            if (user.hasRole("ROLE_RESELLER") ) {
                GetResellerByUserIdResponse resellerResponse = clientReseller.getResellerByUserId(user.getUserId());
                if (resellerResponse != null) {
                    r = resellerResponse.getReseller();
                }
            }
        }
        return r;
    }


}
