package com.season.portal.devices;

import com.season.portal.PortalApplication;
import com.season.portal.auth.LoginModel;
import com.season.portal.resseler.Resseler;
import com.season.portal.utils.model.StringPageModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class DeviceController {

    @GetMapping("/devices")
    public ModelAndView devices(@Valid DeviceModel model, BindingResult result, ModelMap modelMap) {
        model.setNumPerPage(10);

        if(result.hasErrors()){
            modelMap.put(BindingResult.class.getName()+".ciclo",result);
        }

        return devicesView(model);
    }
    private ModelAndView devicesView(DeviceModel model){
        ModelAndView mv = new ModelAndView("devices/devices");

        ArrayList<Device> devices = new ArrayList<Device>();
        int totalElements = 47;

        Pagination devicesPag = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

        int max = devicesPag.getOffset()+model.getNumPerPage();
        max = (max > totalElements)?totalElements:max;


        for(int i = devicesPag.getOffset()+1; i <= max; i++) {
            devices.add(new Device((long) i, true, false, false, true, false, new Date()));
        }

        mv.addObject("devices", devices);
        mv.addObject("Pagination", devicesPag);
        mv.addObject("deviceModel", model);

        return PortalApplication.addStatus(mv);
    }


}
