package com.season.portal.utils;

import com.season.portal.PortalApplication;
import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class ModelViewBaseController {

    @Autowired
    protected PortalConfiguration portalConfig;

    public ModelAndView dispatchView(ModelAndView mv){
        mv.addObject("portalURL", portalConfig.getPortalURL());
        return PortalApplication.addStatus(mv);
    }
}
