package com.season.portal.utils;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.reseller.GetResellerByUserIdResponse;
import com.season.portal.client.generated.reseller.IsHierarchyValidResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.GetSupportByUserIdResponse;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.configuration.PortalConfiguration;
import com.season.portal.devices.DeviceListPageModel;
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

    public String getPrincipalResellerId(ClientReseller clientReseller){

        ClientUserDetails user = Utils.getPrincipalDetails(true);
        String resellerId = user.getResellerId();
        if(resellerId == null){
            Reseller r = getPrincipalReseller(clientReseller);
            if(r != null){
                resellerId = r.getResellerId();
                user.setResellerId(resellerId);
            }

        }
        return resellerId;
    }

    public Support getPrincipalSupport(ClientSupport clientSupport){
        ClientUserDetails user = Utils.getPrincipalDetails(true);
        return getPrincipalSupport(clientSupport, user);
    }

    public Support getPrincipalSupport(ClientSupport clientSupport, ClientUserDetails user){
        Support s = null;

        if(user != null) {
            if (user.hasRole("ROLE_SUPPORT") ) {
                GetSupportByUserIdResponse response = clientSupport.getSupportByUserId(user.getUserId());
                if (response != null) {
                    s = response.getSupport();
                }
            }
        }
        return s;
    }

    public String getPrincipalSupportId(ClientSupport clientSupport){
        ClientUserDetails user = Utils.getPrincipalDetails(true);
        String supportId = user.getSupportId();

        if(supportId == null){
            Support s = getPrincipalSupport(clientSupport);
            if(s != null){
                supportId = s.getSupportId();
                user.setSupportId(supportId);
            }
        }
        return supportId;
    }

    public boolean principalCanSee(ClientReseller clientReseller, Reseller resellerChild){
        boolean isParent = false;
        if(request.isUserInRole("ROLE_ADMIN")) {
            isParent = true;
        }
        else{
            String principalResellerId = getPrincipalResellerId(clientReseller);
            if(principalResellerId != null){
                IsHierarchyValidResponse responseValid = clientReseller.isHierarchyValid(principalResellerId, resellerChild.getResellerId());
                if(clientReseller.validateIsHierarchyValid(responseValid, true)){
                    isParent = true;
                }
            }
        }
        return isParent;
    }

    public boolean principalCanSee(ClientSupport clientSupport, Support supportChild){
        boolean isParent = false;
        if(request.isUserInRole("ROLE_ADMIN")) {
            isParent = true;
        }
        else{
            String principalResellerId = getPrincipalSupportId(clientSupport);
            if(principalResellerId != null){
                com.season.portal.client.generated.support.IsHierarchyValidResponse responseValid = clientSupport.isHierarchyValid(principalResellerId, supportChild.getSupportId());
                if(clientSupport.validateIsHierarchyValid(responseValid, true)){
                    isParent = true;
                }
            }
        }
        return isParent;
    }
}
