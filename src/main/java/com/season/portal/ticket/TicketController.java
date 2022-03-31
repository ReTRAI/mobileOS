package com.season.portal.ticket;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.balance.BalanceListPageModel;
import com.season.portal.client.generated.reseller.GetCountResellerFilteredResponse;
import com.season.portal.client.generated.reseller.GetResellerFilteredResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.*;
import com.season.portal.client.generated.user.GetUserByIdResponse;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.client.users.ClientUser;
import com.season.portal.devices.DeviceListPageModel;
import com.season.portal.reseller.ResellerListPageModel;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.users.UserRoleModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.model.StringModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class TicketController extends ModelViewBaseController {
    @Autowired
    ClientSupport clientSupport;
    @Autowired
    ClientUser clientUser;

    private String SESSION_TICKET_CONTROLLER_LIST_MODEL = "SESSION_TICKET_CONTROLLER_LIST_MODEL";
    private String SESSION_TICKET_CONTROLLER_DETAIL_MODEL = "SESSION_TICKET_CONTROLLER_DETAIL_MODEL";
    private String SESSION_TICKET_CONTROLLER_LIST_TYPE = "SESSION_TICKET_CONTROLLER_LIST_TYPE";

    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping("/ticket/new")
    public ModelAndView ticketNew() {
        return ticketNewView(new NewTicketModel());
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @PostMapping(value={"/ticket/new"})
    public ModelAndView ticketNew(@Valid NewTicketModel model, BindingResult result){

        if(!result.hasErrors()){
            String path = Utils.saveFileIfExist(model.getFile(), true);
            if(path != null){
                ClientUserDetails user = Utils.getPrincipalDetails(true);
                if (user != null) {
                    SetTicketResponse response = clientSupport.setTicket(model.getTitle(), model.getDetail(), path, user.getUserId());
                    if (response != null) {
                        PortalApplication.addSuccessKey("api_ClientSupport_setTicket_success");
                        model = new NewTicketModel();
                    }
                    else
                        PortalApplication.addErrorKey("api_ClientSupport_setTicket_error");
                }
            }
        }

        return ticketNewView( model);
    }

    private ModelAndView ticketNewView(NewTicketModel model){
        ModelAndView mv = new ModelAndView("ticket/new");

        mv.addObject("newTicketModel", model);

        return dispatchView(mv);
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping("/ticket/listBack")
    public ModelAndView listBack(){
        HttpSession session = request.getSession(true);
        TicketListPageModel model = (TicketListPageModel)session.getAttribute(SESSION_TICKET_CONTROLLER_LIST_MODEL);
        if(model == null){
            model = new TicketListPageModel();
            PortalApplication.addErrorKey("api_session_invalidModel");
        }

        String type = (String)session.getAttribute(SESSION_TICKET_CONTROLLER_LIST_TYPE);

        if(type != null){
            switch(type){
                case "own":
                    return ticketOwnList(model);
                case "assigned":
                    return ticketAssignedList(model);
            }
        }

        return ticketAllList(model);
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/ticket/all")
    public ModelAndView ticketAllList(@Valid TicketListPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_TICKET_CONTROLLER_LIST_MODEL, model);
            session.setAttribute(SESSION_TICKET_CONTROLLER_LIST_TYPE, "all");
        }

        return ticketAllList(model);
    }

    private ModelAndView ticketAllList(TicketListPageModel model){
        ModelAndView mv = new ModelAndView("ticket/ticketsAll");
        ArrayList<Ticket> elements = new ArrayList<Ticket>();
        long totalElements = 0;


        GetCountTicketFilteredResponse responseCount = clientSupport.countTicketFiltered(model);
        if(responseCount != null){
            totalElements = responseCount.getResult();
            if(totalElements>0){
                GetTicketFilteredResponse response = clientSupport.getTicketFiltered(model);
                if(response != null){
                    elements = new ArrayList(response.getTicket());
                }
            }
        }

        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);


        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("ticketListPageModel", model);
        mv.addObject("guidRequiredModel_viewTicket", new GuidRequiredModel());

        return dispatchView(mv);
    }


    @PreAuthorize(ALLOW_ROLES_SUP)
    @GetMapping("/ticket/assigned")
    public ModelAndView ticketAssignedList(@Valid TicketListPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_TICKET_CONTROLLER_LIST_MODEL, model);
            session.setAttribute(SESSION_TICKET_CONTROLLER_LIST_TYPE, "assigned");
        }

        return ticketAssignedList(model);
    }

    private ModelAndView ticketAssignedList(TicketListPageModel model){
        ModelAndView mv = new ModelAndView("ticket/ticketsAssigned");
        ArrayList<Ticket> elements = new ArrayList<Ticket>();
        long totalElements = 0;

        ClientUserDetails user = Utils.getPrincipalDetails(true);
        if(user != null) {
            model.setAssignedUserId(user.getUserId());
            GetCountTicketFilteredResponse responseCount = clientSupport.countTicketFiltered(model);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetTicketFilteredResponse response = clientSupport.getTicketFiltered(model);
                    if(response != null){
                        elements = new ArrayList(response.getTicket());
                    }
                }
            }

        }
        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);


        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("ticketListPageModel", model);
        mv.addObject("guidRequiredModel_viewTicket", new GuidRequiredModel());

        return dispatchView(mv);
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping("/ticket/own")
    public ModelAndView ticketOwnList(@Valid TicketListPageModel model, BindingResult result) {
        model.setNumPerPage(10);
        if(!result.hasErrors()){
            HttpSession session = request.getSession(true);
            session.setAttribute(SESSION_TICKET_CONTROLLER_LIST_MODEL, model);
            session.setAttribute(SESSION_TICKET_CONTROLLER_LIST_TYPE, "own");
        }

        return ticketOwnList(model);
    }

    private ModelAndView ticketOwnList(TicketListPageModel model){
        ModelAndView mv = new ModelAndView("ticket/ticketsOwn");
        ArrayList<Ticket> elements = new ArrayList<Ticket>();
        long totalElements = 0;

        ClientUserDetails user = Utils.getPrincipalDetails(true);
        if(user != null) {
            model.setOpenUserId(user.getUserId());
            GetCountTicketFilteredResponse responseCount = clientSupport.countTicketFiltered(model);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetTicketFilteredResponse response = clientSupport.getTicketFiltered(model);
                    if(response != null){
                        elements = new ArrayList(response.getTicket());
                    }
                }
            }

        }
        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);


        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("ticketListPageModel", model);
        mv.addObject("guidRequiredModel_viewTicket", new GuidRequiredModel());

        return dispatchView(mv);
    }


    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping("/ticket/back")
    public ModelAndView ticketBack(){
        return openTicketBySession();
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @PostMapping("/openTicket")
    public ModelAndView openTicket(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            TicketDetailListPageModel m = new TicketDetailListPageModel();
            m.setTicketId(model.getValue());
            m.setSort("detailDate");
            m.setOrder("desc");
            return ticketView(m);
        }
        return ticketAllList(new TicketListPageModel());
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping("/ticket")
    public ModelAndView openTicket(@Valid TicketDetailListPageModel model, BindingResult result) {
        HttpSession session = request.getSession(true);
        TicketDetailListPageModel oldModel = (TicketDetailListPageModel)session.getAttribute(SESSION_TICKET_CONTROLLER_DETAIL_MODEL);
        if(oldModel != null){
            model.setTicketId(oldModel.getTicketId());
        }
        else{
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        return ticketView(model);
    }

    private ModelAndView openTicketBySession(){
        HttpSession session = request.getSession(true);
        TicketDetailListPageModel model = (TicketDetailListPageModel)session.getAttribute(SESSION_TICKET_CONTROLLER_DETAIL_MODEL);
        if(model == null){
            model = new TicketDetailListPageModel();
            PortalApplication.addErrorKey("api_session_invalidModel");
        }
        return ticketView(model);
    }


    private ModelAndView ticketView(TicketDetailListPageModel model){
        ModelAndView mv = new ModelAndView("ticket/ticket");
        ArrayList<TicketDetail> elements = new ArrayList<TicketDetail>();
        long totalElements = 0;
        Ticket ticket = null;
        boolean canCancel = false;
        boolean canUpdateStatus = false;
        boolean canReply = false;
        boolean canAssign = false;

        GetTicketFilteredResponse ticketResponse = clientSupport.getTicketFiltered(new TicketListPageModel(model.getTicketId()));
        if(ticketResponse != null ){
            ArrayList<Ticket> tickets = new ArrayList(ticketResponse.getTicket());
            if(tickets.size() == 1){
                ticket = tickets.get(0);
            }
        }

        if(ticket != null){
            boolean canView = false;
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if(user != null) {
                if(ticket.getCreationUserId().equals(user.getUserId())){
                    canView = true;

                    if(     !ticket.getStatus().equals( ClientSupport.TICKET_STATUS.CANCELED.toString()) &&
                            !ticket.getStatus().equals( ClientSupport.TICKET_STATUS.COMPLETED.toString()))
                    {
                        canCancel = true;
                        canReply = true;
                    }
                }
                if(user.hasRole("ROLE_SUPPORT") || user.hasRole("ROLE_ADMIN")){
                    canView = true;
                    if(!ticket.getStatus().equals(ClientSupport.TICKET_STATUS.CANCELED.toString()))
                    {
                        canUpdateStatus = true;

                        if(!ticket.getStatus().equals(ClientSupport.TICKET_STATUS.COMPLETED.toString())){
                            canReply = true;
                            canAssign = true;
                        }
                    }
                }
            }

            if(canView){
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_TICKET_CONTROLLER_DETAIL_MODEL, model);

                mv.addObject("userId", user.getUserId());

                GetCountTicketDetailFilteredResponse responseCount = clientSupport.countTicketDetailFiltered(model);
                if(responseCount != null){
                    totalElements = responseCount.getResult();
                    if(totalElements>0){
                        GetTicketDetailFilteredResponse response = clientSupport.getTicketDetailFiltered(model);
                        if(response != null){
                            elements = new ArrayList(response.getTicketDetail());
                        }
                    }
                }

                if(ticket.getAssignedUserId() != null){
                    GetSupportByUserIdResponse responseAssigned = clientSupport.getSupportByUserId(ticket.getAssignedUserId());
                    if(responseAssigned != null){
                        mv.addObject("UserRoleAssigned", new UserRoleModel(responseAssigned.getSupport()));
                    }
                }
            }
            else{
                ticket = null;
            }
        }

        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

        if(canCancel)
            mv.addObject("guidModel_canCancel", new GuidRequiredModel(ticket.getTicketId()));
        if(canUpdateStatus)
            mv.addObject("updateTicketStatusModel", new UpdateTicketStatusModel(ticket.getTicketId(), ticket.getStatus()));
        if(canReply)
            mv.addObject("newTicketDetailModel", new NewTicketDetailModel(ticket.getTicketId()));

        mv.addObject("canAssign", canAssign);
        mv.addObject("canReply", canReply);
        mv.addObject("canUpdateStatus", canUpdateStatus);
        mv.addObject("canCancel", canCancel);
        mv.addObject("Ticket", ticket);
        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("ticketDetailListPageModel", model);

        return dispatchView(mv);
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @PostMapping("/ticket/cancelTicket")
    public ModelAndView cancelTicket(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                UpdateTicketModel updateModel = new UpdateTicketModel(model.getValue());
                updateModel.setStatus(ClientSupport.TICKET_STATUS.CANCELED.name());
                UpdateTicketResponse response = clientSupport.updateTicket(updateModel, user.getUserId());
                clientSupport.validateUpdateTicket(response, true, true);
            }
        }
        else{
            PortalApplication.addErrorKey(result);
        }

        return openTicketBySession();
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @PostMapping("/ticket/updateTicketStatus")
    public ModelAndView updateTicketStatus(@Valid UpdateTicketStatusModel model, BindingResult result) {
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if (user != null) {
                UpdateTicketModel updateModel = new UpdateTicketModel(model.getTicketId());
                updateModel.setStatus(model.getStatus());

                UpdateTicketResponse response = clientSupport.updateTicket(updateModel, user.getUserId());
                clientSupport.validateUpdateTicket(response, true, true);
            }
        }
        else{
            PortalApplication.addErrorKey(result);
        }

        return openTicketBySession();
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @PostMapping(value={"/ticket/reply"})
    public ModelAndView reply(@Valid NewTicketDetailModel model, BindingResult result){
        HttpSession session = request.getSession(true);
        TicketDetailListPageModel Ticketmodel = (TicketDetailListPageModel)session.getAttribute(SESSION_TICKET_CONTROLLER_DETAIL_MODEL);
        if(Ticketmodel != null){
            if(!result.hasErrors()){
                String path = Utils.saveFileIfExist(model.getFile(), true);
                if(path != null){
                    ClientUserDetails user = Utils.getPrincipalDetails(true);
                    if (user != null) {
                        model.setTicketId(Ticketmodel.getTicketId());
                        SetTicketDetailResponse response = clientSupport.setTicketDetail(model.getTicketId(), model.getDetail(), path, user.getUserId());
                        if (response != null) {
                            PortalApplication.addSuccessKey("api_ClientSupport_setTicketDetail_success");
                        }
                        else
                            PortalApplication.addErrorKey("api_ClientSupport_setTicketDetail_error");
                    }
                }
            }
            else{
                PortalApplication.addErrorKey(result);
            }
        }
        else{
            Ticketmodel = new TicketDetailListPageModel();
            PortalApplication.addErrorKey("api_session_invalidModel");
        }

        return ticketView(Ticketmodel);
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping(value={"/ticket/getAttach"})
    public ResponseEntity<Resource> download(StringModel model) throws IOException {

        boolean canView = false;
        HttpSession session = request.getSession(true);

        Ticket ticket = getTicketByDetailSession(session);

        if(ticket != null){

            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if(user != null) {
                if(ticket.getCreationUserId().equals(user.getUserId())){
                    canView = true;
                }
                if(user.hasRole("ROLE_SUPPORT") || user.hasRole("ROLE_ADMIN")){
                    canView = true;
                }
            }
        }



        File file = new File("");
        ByteArrayResource resource = new ByteArrayResource(new byte[]{});
        if(canView){
            File filePath = new File("src/main/resources/media/tickets/"+model.getValue());

            if (filePath.exists()) {
                file = new File(filePath.getAbsolutePath());
                Path path = Paths.get(file.getAbsolutePath());
                resource = new ByteArrayResource(Files.readAllBytes(path));
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attach."+Utils.getFileNameExt(model.getValue()));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/ticket/assignOpen")
    public ModelAndView assign() {

        return assignView(new SupportListPageModel());
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/ticket/assign")
    public ModelAndView assign(@Valid SupportListPageModel model, BindingResult result) {

        return assignView(model);
    }

    private Ticket getTicketByDetailSession(HttpSession session){
        Ticket ticket = null;

        TicketDetailListPageModel ticketModel = (TicketDetailListPageModel)session.getAttribute(SESSION_TICKET_CONTROLLER_DETAIL_MODEL);
        if(ticketModel != null) {
            GetTicketFilteredResponse ticketResponse = clientSupport.getTicketFiltered(new TicketListPageModel(ticketModel.getTicketId()));
            if (ticketResponse != null) {
                ArrayList<Ticket> tickets = new ArrayList(ticketResponse.getTicket());
                if (tickets.size() == 1) {
                    ticket = tickets.get(0);
                }
            }
        }
        else{
            PortalApplication.addErrorKey("api_session_invalidModel");
        }

        return ticket;
    }

    private ModelAndView assignView(SupportListPageModel model){
        ModelAndView mv = new ModelAndView("ticket/assignSupport");
        ArrayList<UserRoleModel> elements = new ArrayList<UserRoleModel>();
        long totalElements = 0;

        HttpSession session = request.getSession(true);
        Ticket ticket = getTicketByDetailSession(session);

        if(ticket != null){
            String supportId = getPrincipalSupportId(clientSupport);
            if(request.isUserInRole("ROLE_ADMIN")){
                if(AdminController.getAdminView(session)){
                    supportId = "";
                }
            }

            if(supportId != null) {

                model.setSupportId(supportId);
                model.setOnlyChildren(true);
                GetCountSupportFilteredResponse responseCount = clientSupport.countSupportFiltered(model);
                if (responseCount != null) {
                    totalElements = responseCount.getResult();
                    if (totalElements > 0) {
                        GetSupportFilteredResponse response = clientSupport.getSupportFiltered(model);
                        if (response != null) {
                            ArrayList<Support> supports = new ArrayList(response.getSupport());
                            elements = Utils.supportToUserRole(supports);
                        }
                    }
                }

                if(request.isUserInRole("ROLE_SUPPORT")){
                    ClientUserDetails user = Utils.getPrincipalDetails(true);
                    mv.addObject("userId", user.getUserId());
                }

            }
        }


        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

        mv.addObject("Ticket", ticket);
        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("supportListPageModel", model);
        mv.addObject("guidModel_assign", new GuidRequiredModel());

        return dispatchView(mv);
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @PostMapping("/ticket/assignTicket")
    public ModelAndView assignTicket(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            String ticketId = null;
            HttpSession session = request.getSession(true);
            TicketDetailListPageModel ticketModel = (TicketDetailListPageModel)session.getAttribute(SESSION_TICKET_CONTROLLER_DETAIL_MODEL);
            if(ticketModel != null) {
                ticketId = ticketModel.getTicketId();

                ClientUserDetails user = Utils.getPrincipalDetails(true);
                if (user != null) {
                    UpdateTicketModel updateModel = new UpdateTicketModel(ticketId);
                    updateModel.setAssignedUserId(model.getValue());
                    UpdateTicketResponse response = clientSupport.updateTicket(updateModel, user.getUserId());
                    if(clientSupport.validateUpdateTicket(response, true, true)){
                        return openTicketBySession();
                    }
                }
            }
            else{
                PortalApplication.addErrorKey("api_session_invalidModel");
            }
        }
        else{
            PortalApplication.addErrorKey(result);
        }

        return assignView(new SupportListPageModel());
    }

}
