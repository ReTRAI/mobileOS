package com.season.portal.reseller;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.LoginModel;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.balance.BalanceListPageModel;
import com.season.portal.balance.BalanceMovementModel;
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
import com.season.portal.utils.model.StringModel;
import com.season.portal.utils.model.StringPageModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;

import static com.season.portal.client.reseller.ClientReseller.MOVEMENT_CREDIT;
import static com.season.portal.client.reseller.ClientReseller.MOVEMENT_DEBIT;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class ResellerController extends ModelViewBaseController {
    @Autowired
    ClientReseller clientReseller;
    @Autowired
    ClientDevice clientDevice;


    private String SESSION_RESELLER_CONTROLLER_LIST_MODEL = "SESSION_RESELLER_CONTROLLER_LIST_MODEL";
    private String SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY = "SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY";
    private String SESSION_RESELLER_CONTROLLER_BALANCE_RESELLER = "SESSION_RESELLER_CONTROLLER_BALANCE_RESELLER";

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
    public ModelAndView resellers(@Valid ResellerListPageModel model, BindingResult result) {
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

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @PostMapping("/openResellerChild")
    public ModelAndView openResellerChild(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            DeviceListPageModel m = new DeviceListPageModel();
            m.setResellerId(model.getValue());
            return resellerView(m);
        }
        return resellerViewBySession();
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
    @GetMapping("/resellers/resellerParent")
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
        else if(size == 1){//The Last saved
            model = viewedModels.remove(size-1);
            session.setAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY, viewedModels);
            PortalApplication.addErrorKey("api_reseller_noParentSessionReseller");
        }
        else{
            PortalApplication.addErrorKey("api_reseller_noSessionReseller");
        }

        return resellerView(model);
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers/resellerHistory")
    public ModelAndView resellerBackBySession(){
        return resellerView(getRemoveLast_DeviceListPageModel());
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

                mv.addObject("guidModel_openBalance", new GuidRequiredModel(reseller.getResellerId()));

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

                long childCount = 0;

                GetCountResellerFilteredResponse responseChildCount = clientReseller.countResellerFiltered(reseller.getResellerId(), "", true );
                if(responseChildCount != null){
                    childCount = responseChildCount.getResult();

                }
                Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

                mv.addObject("guidModel_parentId", new GuidRequiredModel(reseller.getResellerId()));
                mv.addObject("childCount", childCount);
                mv.addObject("elements", elements);
                mv.addObject("Pagination", pagination);
                mv.addObject("deviceListPageModel", model);
                mv.addObject("guidModel_openDevice", new GuidRequiredModel());

            }
        }

        return dispatchView(mv);
    }


    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @PostMapping("/resellers/viewResellerChilds")
    public ModelAndView openResellerChilds(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ResellerListPageModel m = new ResellerListPageModel();
            m.setResellerId(model.getValue());
            return resellerChildsView(m);
        }
        return resellerViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @PostMapping("/resellers/resellerChilds")
    public ModelAndView resellerChilds(@Valid ResellerListPageModel model, BindingResult result) {
        if(!result.hasErrors()){
            /*
            HttpSession session = request.getSession(true);
            ArrayList<DeviceListPageModel> viewedModels = (ArrayList<DeviceListPageModel>)session.getAttribute(SESSION_RESELLER_CONTROLLER_RESELLER_MODEL_HISTORY);
            int size = viewedModels.size();
            if (viewedModels != null && size>0){
                DeviceListPageModel parentReseller = viewedModels.get(size-1);
                model.setResellerId(parentReseller.getResellerId());
            }
            else{
                PortalApplication.addErrorKey("api_reseller_noSessionReseller");
            }

            */
        }

        return resellerChildsView(model);
    }


    private ModelAndView resellerChildsView(ResellerListPageModel model){
        ModelAndView mv = new ModelAndView("reseller/resellerChilds");
        Reseller reseller = null;
        ArrayList<Reseller> elements = new ArrayList<Reseller>();
        long totalElements = 0;
        String resellerId = model.getResellerId();

        if(principalCanSee(clientReseller, resellerId)){

            GetResellerByIdResponse response = clientReseller.getResellerById(resellerId);
            if(response != null){
                reseller = response.getReseller();
            }
            if(reseller != null){
                model.setOnlyChildren(true);
                GetCountResellerFilteredResponse responseCount = clientReseller.countResellerFiltered(model);
                if(responseCount != null){
                    totalElements = responseCount.getResult();
                    if(totalElements>0){
                        GetResellerFilteredResponse responseChilds = clientReseller.getResellerFiltered(model);
                        if(responseChilds != null){
                            elements = new ArrayList(responseChilds.getReseller());
                        }
                    }
                }
            }
        }

        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

        mv.addObject("Reseller", reseller);
        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("resellerListPageModel", model);
        mv.addObject("guidRequiredModel_viewReseller", new GuidRequiredModel());

        return dispatchView(mv);
    }


    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @PostMapping("/resellers/openBalance")
    public ModelAndView openResellerBalance(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            BalanceListPageModel balanceModel = new BalanceListPageModel(model.getValue());
            balanceModel.setOrder("desc");
            balanceModel.setSort("movementDate");
            return resellerBalanceView(balanceModel);
        }
        return resellerViewBySession();
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping("/resellers/balance")
    public ModelAndView resellerBalance(@Valid BalanceListPageModel model, BindingResult result) {
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            BalanceListPageModel oldModel = (BalanceListPageModel)session.getAttribute(SESSION_RESELLER_CONTROLLER_BALANCE_RESELLER);
            if(oldModel != null){
                model.setResellerId(oldModel.getResellerId());
                return resellerBalanceView(model);
            }
        }
        return resellerViewBySession();
    }

    private ModelAndView resellerBalanceView(BalanceListPageModel model){
        ModelAndView mv = new ModelAndView("reseller/balance");
        ArrayList<ResellerBalance> elements = new ArrayList<ResellerBalance>();
        long totalElements = 0;



        if(principalCanSee(clientReseller, model.getResellerId())) {
            GetResellerByIdResponse responseReseller = clientReseller.getResellerById(model.getResellerId());
            if(responseReseller != null){
                Reseller  r = responseReseller.getReseller();
                if(r != null) {
                    HttpSession session = request.getSession(true);
                    session.setAttribute(SESSION_RESELLER_CONTROLLER_BALANCE_RESELLER, model);

                    mv.addObject("resellerId", r.getResellerId());
                    mv.addObject("resellerName", r.getResellerName());
                    mv.addObject("resellerBalance", r.getCurrentBalance());

                    String resellerId = r.getResellerId();
                    model.setResellerId(resellerId);

                    GetCountResellerBalanceMovementsResponse responseCount = clientReseller.countResellerBalanceMovements(model);
                    if (responseCount != null) {
                        totalElements = responseCount.getResult();
                        if (totalElements > 0) {
                            GetResellerBalanceMovementsResponse response = clientReseller.getResellerBalanceMovements(model);
                            if (response != null) {
                                elements = new ArrayList(response.getResellerBalance());
                            }
                        }
                    }

                    mv.addObject("balanceMovementModel_sendCredits", new BalanceMovementModel(resellerId));

                    if(request.isUserInRole("ROLE_ADMIN")){
                        mv.addObject("balanceMovementModel_addCredits", new BalanceMovementModel(resellerId));
                        mv.addObject("balanceMovementModel_rmvCredits", new BalanceMovementModel(resellerId));
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




    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @GetMapping(value={"/reseller/lookup"})
    public ModelAndView lookup(@Valid StringModel model, BindingResult result){
        if(!result.hasErrors()){
            return lookupView(model, !model.getValue().isEmpty());
        }
        return lookupView(model, false);
    }

    private ModelAndView lookupView(StringModel model, boolean successSearch) {
        ModelAndView mv = new ModelAndView("reseller/lookup");

        if(successSearch){
            Reseller reseller = null;

            GetResellerByUserDeviceNameResponse response = clientReseller.getResellerByUserDeviceName(model.getValue());
            if(response != null){
                reseller = response.getReseller();

            }
            boolean canSee =  principalCanSee(clientReseller, reseller);
            mv.addObject("principalIsParent", canSee);
            if(canSee){
                mv.addObject("guidRequiredModel_viewReseller", new GuidRequiredModel(reseller.getResellerId()));
            }

            mv.addObject("Reseller", reseller);
        }

        mv.addObject("stringModel", model);
        return dispatchView(mv);
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @PostMapping(value={"/resellers/sendCredits"})
    public ModelAndView sendCredits(@Valid BalanceMovementModel model, BindingResult result){
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            String principalResellerId = getPrincipalResellerId(clientReseller);
            String toResellerId = model.getResellerId();

            if(user != null && principalResellerId != null){
                model.setMovementType(ClientReseller.MOVEMENT_TYPE.TRANSFER.toString());

                model.setDebitCredit(MOVEMENT_DEBIT);
                model.setResellerId(principalResellerId);
                SetResellerBalanceMovementResponse response = clientReseller.setResellerBalanceMovement(model, user.getUserId() );

                if(clientReseller.validateSetResellerBalanceMovement(response, true)){

                    model.setDebitCredit(MOVEMENT_CREDIT);
                    model.setResellerId(toResellerId);
                    response = clientReseller.setResellerBalanceMovement(model, user.getUserId() );

                    if(clientReseller.validateSetResellerBalanceMovement(response, true)){
                        getPrincipalReseller(clientReseller);//updateBalance
                        PortalApplication.addSuccessKey("api_ClientReseller_validateSetResellerBalanceMovement_success");
                    }
                }
            }
        }
        else{
            PortalApplication.addErrorKey(result);
        }

        HttpSession session = request.getSession(true);
        BalanceListPageModel balanceModel = (BalanceListPageModel)session.getAttribute(SESSION_RESELLER_CONTROLLER_BALANCE_RESELLER);
        return resellerBalanceView(balanceModel);
    }

    @PreAuthorize(ALLOW_ROLES_ADMIN)
    @PostMapping(value={"/resellers/addCredits"})
    public ModelAndView addCredits(@Valid BalanceMovementModel model, BindingResult result){
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);

            if(user != null){
                model.setMovementType(ClientReseller.MOVEMENT_TYPE.MANUAL_CREDIT.toString());
                model.setDebitCredit(MOVEMENT_CREDIT);
                SetResellerBalanceMovementResponse response = clientReseller.setResellerBalanceMovement(model, user.getUserId() );

                if(clientReseller.validateSetResellerBalanceMovement(response, true)){
                    String principalResellerId = getPrincipalResellerId(clientReseller);
                    if(principalResellerId != null && principalResellerId.equals(model.getResellerId())){
                        getPrincipalReseller(clientReseller);//updateBalance
                    }
                    PortalApplication.addSuccessKey("api_ClientReseller_validateSetResellerBalanceMovement_success");
                }
            }
        }
        else{
            PortalApplication.addErrorKey(result);
        }

        HttpSession session = request.getSession(true);
        BalanceListPageModel balanceModel = (BalanceListPageModel)session.getAttribute(SESSION_RESELLER_CONTROLLER_BALANCE_RESELLER);
        return resellerBalanceView(balanceModel);
    }

    @PreAuthorize(ALLOW_ROLES_ADMIN)
    @PostMapping(value={"/resellers/removeCredits"})
    public ModelAndView removeCredits(@Valid BalanceMovementModel model, BindingResult result){
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);

            if(user != null){
                model.setMovementType(ClientReseller.MOVEMENT_TYPE.MANUAL_DEBIT.toString());
                model.setDebitCredit(MOVEMENT_DEBIT);
                SetResellerBalanceMovementResponse response = clientReseller.setResellerBalanceMovement(model, user.getUserId() );

                if(clientReseller.validateSetResellerBalanceMovement(response, true)){
                    String principalResellerId = getPrincipalResellerId(clientReseller);
                    if(principalResellerId != null && principalResellerId.equals(model.getResellerId())){
                        getPrincipalReseller(clientReseller);//updateBalance
                    }
                    PortalApplication.addSuccessKey("api_ClientReseller_validateSetResellerBalanceMovement_success");
                }
            }
        }
        else{
            PortalApplication.addErrorKey(result);
        }

        HttpSession session = request.getSession(true);
        BalanceListPageModel balanceModel = (BalanceListPageModel)session.getAttribute(SESSION_RESELLER_CONTROLLER_BALANCE_RESELLER);
        return resellerBalanceView(balanceModel);
    }
}
