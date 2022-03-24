package com.season.portal.ticket;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.balance.BalanceListPageModel;
import com.season.portal.client.generated.reseller.GetCountResellerFilteredResponse;
import com.season.portal.client.generated.reseller.GetResellerFilteredResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.*;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.devices.DeviceListPageModel;
import com.season.portal.reseller.ResellerListPageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.ArrayList;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class TicketController extends ModelViewBaseController {
    @Autowired
    ClientSupport clientSupport;

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
    public ModelAndView ticketNew(@Valid NewTicketModel model, BindingResult result, HttpServletRequest request){

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
    public ModelAndView resellersBySession(){
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
    @PostMapping("/openTicket")
    public ModelAndView openTicket(@Valid GuidRequiredModel model, BindingResult result) {
        if(!result.hasErrors()){
            TicketDetailListPageModel m = new TicketDetailListPageModel();
            m.setTicketId(model.getValue());
            return ticketView(m);
        }
        return ticketAllList(new TicketListPageModel());
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping("/ticket")
    public ModelAndView openTicket(@Valid TicketDetailListPageModel model, BindingResult result) {
        /*
        HttpSession session = request.getSession(true);
        TicketDetailListPageModel oldModel = (TicketDetailListPageModel) session.getAttribute(SESSION_TICKET_CONTROLLER_DETAIL_MODEL);
        if (oldModel != null) {
            model.setTicketId(oldModel.getTicketId());
            if (!result.hasErrors()) {
                return ticketView(model);
            }
            return ticketView(model);
        }

        return ticketAllList(new TicketListPageModel());
        */
        return ticketView(model);
    }



    private ModelAndView ticketView(TicketDetailListPageModel model){
        ModelAndView mv = new ModelAndView("ticket/ticket");
        ArrayList<TicketDetail> elements = new ArrayList<TicketDetail>();
        long totalElements = 0;
        Ticket ticket = null;
        boolean canCancel = false;

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
                    if(ticket.getStatus() != ClientSupport.TICKET_STATUS.CANCELED.toString() &&
                        ticket.getStatus() != ClientSupport.TICKET_STATUS.COMPLETED.toString())
                    {

                        mv.addObject("guidModel_canCancel", new GuidRequiredModel(ticket.getTicketId()));
                        canCancel = true;
                    }
                }
                else if(user.hasRole("ROLE_SUPPORT") || user.hasRole("ROLE_ADMIN")){
                    canView = true;
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
                            elements.get(0).getDetail();
                            elements.get(0).getTicketId();
                            elements.get(0).getTicketDetailId();
                            elements.get(0).getAttachPath();
                            elements.get(0).getDetailUserId();

                        }
                    }
                }
            }
            else{
                ticket = null;
            }
        }

        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);


        mv.addObject("canCancel", canCancel);
        mv.addObject("Ticket", ticket);
        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("ticketDetailListPageModel", model);

        return dispatchView(mv);
    }

}
