package com.season.portal.utils;

import com.season.portal.PortalApplication;
import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ModelViewBaseController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    protected PortalConfiguration portalConfig;

    public ModelAndView dispatchView(ModelAndView mv ){
        mv.addObject("portalURL", portalConfig.getPortalURL());
        return PortalApplication.addStatus(mv, request);
    }


}
