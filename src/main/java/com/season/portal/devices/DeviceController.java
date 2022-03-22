package com.season.portal.devices;

import com.season.portal.PortalApplication;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.device.ClientDevice;
import com.season.portal.client.generated.device.Device;
import com.season.portal.client.generated.device.GetCountDevicesFilteredResponse;
import com.season.portal.client.generated.device.GetDeviceByIdResponse;
import com.season.portal.client.generated.device.GetDevicesFilteredResponse;
import com.season.portal.client.generated.reseller.GetCountResellerBalanceMovementsResponse;
import com.season.portal.client.generated.reseller.GetResellerBalanceMovementsResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.reseller.ResellerBalance;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.reseller.ResellerListPageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_RES_ADMIN;

@Controller
public class DeviceController extends ModelViewBaseController {
    @Autowired
    ClientReseller clientReseller;
    @Autowired
    ClientDevice clientDevice;

    private String SESSION_DEVICE_CONTROLLER_LIST_MODEL = "SESSION_DEVICE_CONTROLLER_LIST_MODEL";

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/devices/listBack")
    public ModelAndView resellersBySession(){
        HttpSession session = request.getSession(true);
        DeviceListPageModel model = (DeviceListPageModel)session.getAttribute(SESSION_DEVICE_CONTROLLER_LIST_MODEL);
        if(model == null){
            model = new DeviceListPageModel();
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        return devicesView(model);
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/devices")
    public ModelAndView devices(@Valid DeviceListPageModel model, BindingResult result) {
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_DEVICE_CONTROLLER_LIST_MODEL, model);
        }
        return devicesView(model);
    }

    private ModelAndView devicesView(DeviceListPageModel model){
        ModelAndView mv = new ModelAndView("devices/devices");

        String resellerId = getPrincipalResellerId(clientReseller);
        if(request.isUserInRole("ROLE_ADMIN")){
            HttpSession session = request.getSession(true);
            if(AdminController.getAdminView(session)){
                resellerId = "";
            }
        }
        model.setResellerId(resellerId);

        ArrayList<SimpleDeviceModel> elements = new ArrayList<SimpleDeviceModel>();
        long totalElements = 0;

        GetCountDevicesFilteredResponse responseCount = clientDevice.countDeviceFiltered(model);
        if(responseCount != null){
            totalElements = responseCount.getResult();
            if(totalElements>0){
                GetDevicesFilteredResponse responseDevices = clientDevice.getDeviceFiltered(model);
                if(responseDevices != null){
                    ArrayList<Device> devices = new ArrayList(responseDevices.getDevice());
                    elements = Utils.resellerToSimpleDeviceModel(devices);
                }
            }
        }

        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("deviceListPageModel", model);
        mv.addObject("guidModel_openDevice", new GuidModel());
        return dispatchView(mv);
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @PostMapping("/device")
    public ModelAndView deviceOpen(@Valid GuidModel model, BindingResult result) {
        if(result.hasErrors()){
            PortalApplication.addErrorKey("device_invalid");
            return resellersBySession();
        }
        return deviceView(model);
    }

    private ModelAndView deviceView(GuidModel model){
        ModelAndView mv = new ModelAndView("devices/device");
        Device d = null;

        GetDeviceByIdResponse response = clientDevice.getDeviceById(model.getValue());
        if(response != null){
            d = response.getDevice();
        }

        mv.addObject("device", d);

        return dispatchView(mv);
    }

}
