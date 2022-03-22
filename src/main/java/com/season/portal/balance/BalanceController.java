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
    @GetMapping("/reseller/balance")
    public ModelAndView balance() {
        return balanceView(new BalanceListPageModel());
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/reseller/balance/filter")
    public ModelAndView balance(@Valid BalanceListPageModel model, BindingResult result) {
        /*if(!result.hasErrors()){

        }
*/
        return balanceView(model);
    }

    private ModelAndView balanceView(BalanceListPageModel model){
        ModelAndView mv = new ModelAndView("reseller/balance/view");
        ArrayList<ResellerBalance> elements = new ArrayList<ResellerBalance>();
        long totalElements = 0;

        Reseller r = getPrincipalReseller(clientReseller);
        if(r != null) {
            mv.addObject("resellerName", r.getResellerName());
            mv.addObject("resellerBalance", r.getCurrentBalance());

            String resellerId = r.getResellerId();
            model.setResellerId(resellerId);

            GetCountResellerBalanceMovementsResponse responseCount = clientReseller.countResellerBalanceMovements(model);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetResellerBalanceMovementsResponse response = clientReseller.getResellerBalanceMovements(model);
                    if(response != null){
                        elements = new ArrayList(response.getResellerBalance());
                    }
                }
            }

        }
        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);



        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("balanceListPageModel", model);

        return dispatchView(mv);
    }




}
