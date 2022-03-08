package com.season.portal.reseller;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.device.ClientDevice;
import com.season.portal.client.generated.dashboard.GetDashboardByResellerIdResponse;
import com.season.portal.client.generated.device.Device;
import com.season.portal.client.generated.device.GetCountDevicesFilteredResponse;
import com.season.portal.client.generated.device.GetDevicesFilteredResponse;
import com.season.portal.client.generated.reseller.*;
import com.season.portal.client.generated.support.GetCountSupportFilteredResponse;
import com.season.portal.client.generated.support.GetSupportByUserIdResponse;
import com.season.portal.client.generated.support.GetSupportParentByChildIdResponse;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.generated.user.*;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.devices.DeviceListPageModel;
import com.season.portal.devices.SimpleDeviceModel;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.users.UserRoleModel;
import com.season.portal.users.UsersListPageModel;
import com.season.portal.users.hierarchy.HierarchyModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidModel;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.model.StringPageModel;
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

import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class ResellerController extends ModelViewBaseController {
    @Autowired
    ClientReseller clientReseller;
    @Autowired
    ClientDevice clientDevice;


    private String SESSION_RESELLER_CONTROLLER_LIST_MODEL = "SESSION_RESELLER_CONTROLLER_LIST_MODEL";
    private String SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY = "SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY";


    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers/listBack")
    public ModelAndView resellersBySession(){
        HttpSession session = request.getSession(true);
        ResellerListPageModel model = (ResellerListPageModel)session.getAttribute(SESSION_RESELLER_CONTROLLER_LIST_MODEL);
        if(model == null){
            model = new ResellerListPageModel();
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        return resellersView(model);
    }
    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers")
    public ModelAndView resellers(ResellerListPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_LIST_MODEL, model);
        }

        return resellersView(model);
    }

    private ModelAndView resellersView(ResellerListPageModel model){
        ModelAndView mv = new ModelAndView("reseller/resellers");
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


    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @PostMapping("/openReseller")
    public ModelAndView openReseller(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, new ArrayList<DeviceListPageModel>());
            DeviceListPageModel m = new DeviceListPageModel();
            m.setResellerId(model.getValue());
            return resellerView(m);

        }
        return resellerView(null);
    }

    private DeviceListPageModel getRemoveLast_DeviceListPageModel(){
        HttpSession session = request.getSession(true);
        ArrayList<DeviceListPageModel> viewedModels = (ArrayList<DeviceListPageModel>)session.getAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY);
        DeviceListPageModel last = null;
        if (viewedModels == null){
            viewedModels = new ArrayList<DeviceListPageModel>();
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, viewedModels);
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        else if(viewedModels.size()>0){
            last = viewedModels.remove(viewedModels.size()-1);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, viewedModels);
        }
        else{
            PortalApplication.addErrorKey("api_reseller_noSessionReseller");
        }
        return last;
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/reseller")
    public ModelAndView reseller(@Valid DeviceListPageModel model, BindingResult result) {
        if(!result.hasErrors()){
            DeviceListPageModel prevModel = getRemoveLast_DeviceListPageModel();
            model.setResellerId(prevModel.getResellerId());
            return resellerView(model);
        }
        return resellerView(null);
    }


    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers/resellerHistory")
    public ModelAndView resellerParentViewBySession(){
        HttpSession session = request.getSession(true);
        ArrayList<DeviceListPageModel> viewedModels = (ArrayList<DeviceListPageModel>)session.getAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY);
        DeviceListPageModel model = null;
        int size = viewedModels.size();
        if (viewedModels == null){
            viewedModels = new ArrayList<DeviceListPageModel>();
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, viewedModels);
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        else if(size > 1){//The Parent
            viewedModels.remove(size-1);//The Last sawed
            model = viewedModels.remove(size-2);//The Parent
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, viewedModels);
        }
        else if(size == 1){//The Last sawed
            model = viewedModels.remove(size-1);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, viewedModels);
            PortalApplication.addErrorKey("api_reseller_noParentSessionReseller");
        }
        else{
            PortalApplication.addErrorKey("api_reseller_noSessionReseller");
        }

        return resellerView(model);
    }

    public ModelAndView resellerViewBySession(){

        return resellerView(getRemoveLast_DeviceListPageModel());
    }

    private ModelAndView resellerView(DeviceListPageModel model){
        ModelAndView mv = new ModelAndView("reseller/view");
        Reseller reseller = null;

        GetResellerByIdResponse response = clientReseller.getResellerById(model.getResellerId());
        if(response != null){
            reseller = response.getReseller();
        }

        mv.addObject("Reseller", reseller);
        if(reseller != null){
            if(principalCanSee(clientReseller, reseller)){
                HttpSession session = request.getSession(true);
                ArrayList<DeviceListPageModel> viewedModels = (ArrayList<DeviceListPageModel>)session.getAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY);
                if(viewedModels == null){
                    viewedModels = new ArrayList<DeviceListPageModel>();
                }
                mv.addObject("resellerHistory", (viewedModels.size() > 0));
                viewedModels.add(model);
                session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, viewedModels);

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
                mv.addObject("guidModel_openDevice", new GuidRequiredModel());
            }
        }

        return dispatchView(mv);
    }
}
