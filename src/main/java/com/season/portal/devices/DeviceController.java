package com.season.portal.devices;

import com.season.portal.PortalApplication;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.model.GuidModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;

@Controller
public class DeviceController extends ModelViewBaseController {

    @GetMapping("/devices")
    public ModelAndView devices(@Valid DevicePageModel model, BindingResult result) {
        model.setNumPerPage(10);

        /*
        if(result.hasErrors()){
        }
        */

        return devicesView(model);
    }
    private ModelAndView devicesView(DevicePageModel model){
        ModelAndView mv = new ModelAndView("devices/devices");

        ArrayList<Device> devices = new ArrayList<Device>();
        int totalElements = 47;

        Pagination devicesPag = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

        int max = devicesPag.getOffset()+model.getNumPerPage();
        max = (max > totalElements)?totalElements:max;


        for(int i = devicesPag.getOffset()+1; i <= max; i++) {
            devices.add(new Device( i+"", true, false, false, true, false, new Date()));
        }

        mv.addObject("devices", devices);
        mv.addObject("Pagination", devicesPag);
        mv.addObject("devicePageModel", model);
        mv.addObject("guidModel", new GuidModel());
        return dispatchView(mv);
    }

    @PostMapping("/device")
    public ModelAndView deviceOpen(@Valid GuidModel model, BindingResult result) {

        if(result.hasErrors()){
            PortalApplication.addErrorKey("device_invalid");
            return devicesView(new DevicePageModel());
        }

        return deviceView(model);
    }

    private ModelAndView deviceView(GuidModel model){
        ModelAndView mv = new ModelAndView("devices/device");

        Device d = new Device(model.getValue(), true, false, false, true, false, new Date());
        mv.addObject("device", d);

        return dispatchView(mv);
    }

}
