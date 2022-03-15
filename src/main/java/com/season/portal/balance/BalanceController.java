package com.season.portal.balance;

import com.season.portal.PortalApplication;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.device.ClientDevice;
import com.season.portal.client.generated.device.Device;
import com.season.portal.client.generated.device.GetCountDevicesFilteredResponse;
import com.season.portal.client.generated.device.GetDevicesFilteredResponse;
import com.season.portal.client.generated.reseller.*;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.devices.DeviceListPageModel;
import com.season.portal.devices.SimpleDeviceModel;
import com.season.portal.reseller.ResellerListPageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.model.StringModel;
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

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_RES_ADMIN;

@Controller
public class BalanceController extends ModelViewBaseController {
    @Autowired
    ClientReseller clientReseller;


    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/balance")
    public ModelAndView balance(ResellerListPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
        }

        return balanceView(model);
    }

    private ModelAndView balanceView(ResellerListPageModel model){
        ModelAndView mv = new ModelAndView("balance/view");
        ArrayList<Reseller> elements = new ArrayList<Reseller>();
        long totalElements = 0;

        Reseller r = getPrincipalReseller(clientReseller);
        if(r != null) {
            String resellerId = r.getResellerId();
            if(request.isUserInRole("ROLE_ADMIN")){
                HttpSession session = request.getSession(true);
                if(AdminController.getAdminView(session)){
                    resellerId = "";
                }
            }

            model.setResellerId(resellerId);
            model.setOnlyChildren(true);
            GetCountResellerFilteredResponse responseCount = clientReseller.countResellerFiltered(model);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetResellerFilteredResponse response = clientReseller.getResellerFiltered(model);
                    if(response != null){
                        elements = new ArrayList(response.getReseller());
                    }
                }
            }

        }
        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);


        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("resellerListPageModel", model);
        //mv.addObject("deviceListPageModel_viewReseller", new DeviceListPageModel());
        mv.addObject("guidRequiredModel_viewReseller", new GuidRequiredModel());

        return dispatchView(mv);
    }




}
