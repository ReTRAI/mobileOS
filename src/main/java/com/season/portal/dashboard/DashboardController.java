package com.season.portal.dashboard;

import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.dashboard.ClientDashboard;
import com.season.portal.client.generated.dashboard.GetDashboardByResellerIdResponse;
import com.season.portal.client.generated.notification.GetCountUserNotificationFilteredResponse;
import com.season.portal.client.generated.notification.UserNotification;
import com.season.portal.client.generated.reseller.GetResellerByUserIdResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.GetCountTicketFilteredResponse;
import com.season.portal.client.generated.support.GetTicketFilteredResponse;
import com.season.portal.client.generated.support.Ticket;
import com.season.portal.client.notification.ClientNotification;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.client.users.ClientUser;
import com.season.portal.notifications.NotificationListPageModel;
import com.season.portal.ticket.TicketListPageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static com.fasterxml.jackson.databind.type.LogicalType.DateTime;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class DashboardController extends ModelViewBaseController {
    @Autowired
    ClientDashboard clientDashboard;

    @Autowired
    ClientReseller clientReseller;

    @Autowired
    ClientSupport clientSupport;

    @PreAuthorize(ALLOW_ROLES_ALL)
    @RequestMapping(value={"/dashboard"})
    public ModelAndView dashboard(){
        return dashboardView();
    }

    private ModelAndView dashboardView(){
        ModelAndView mv = new ModelAndView("dashboard");
        mv.addObject("dashboard", null);
        boolean canViewTickets = false;
        boolean canViewDevices = false;

        ClientUserDetails user = Utils.getPrincipalDetails(true);
        if(user != null) {

            //Reseller------------------------------
            Reseller r = getPrincipalReseller(clientReseller, user);
            if(r != null){
                boolean recursiveAdmin = false;
                canViewDevices = true;

                if(request.isUserInRole("ROLE_ADMIN")){
                    HttpSession session = request.getSession(true);
                    recursiveAdmin = AdminController.getAdminView(session);
                }
                GetDashboardByResellerIdResponse dashboardResponse = clientDashboard.getDashboardByResellerId(r.getResellerId(), recursiveAdmin);

                if(dashboardResponse != null){

                    mv.addObject("overview", dashboardResponse.getGlobal());
                    mv.addObject("inactive", dashboardResponse.getInactive());
                    mv.addObject("expiring", dashboardResponse.getExpiring());
                    mv.addObject("activations", dashboardResponse.getActive());
                }
            }

            //Support------------------------------

            if(user.hasRole("ROLE_SUPPORT") || user.hasRole("ROLE_ADMIN")){
                TicketListPageModel model = new TicketListPageModel();
                model.setAssignedUserId(user.getUserId());

                setTotalTicketDashboard(mv, model, "ticketAllTime_");

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, -1);
                model.setStartCreationDate(format.format(cal.getTime()));
                setTotalTicketDashboard(mv, model, "ticketLastMonth_");
                canViewTickets = true;
            }

        }
        mv.addObject("canViewDevices", canViewDevices);
        mv.addObject("canViewTickets", canViewTickets);
        return dispatchView(mv);
    }

    private void setTotalTicketDashboard(ModelAndView mv, TicketListPageModel model, String prefix){
        model.setTicketStatus(ClientSupport.TICKET_STATUS.OPEN.name());
        mv.addObject(prefix+"open", getTotalTicket(model));
        model.setTicketStatus(ClientSupport.TICKET_STATUS.ONPROGRESS.name());
        mv.addObject(prefix+"onProgress", getTotalTicket(model));
        model.setTicketStatus(ClientSupport.TICKET_STATUS.PENDING.name());
        mv.addObject(prefix+"pending", getTotalTicket(model));

        long finished = 0;
        model.setTicketStatus(ClientSupport.TICKET_STATUS.COMPLETED.name());
        finished +=getTotalTicket(model);
        model.setTicketStatus(ClientSupport.TICKET_STATUS.CANCELED.name());
        finished +=getTotalTicket(model);
        mv.addObject(prefix+"finished", finished);
    }

    private long getTotalTicket(TicketListPageModel model){
        long total = 0;

        GetCountTicketFilteredResponse responseCount = clientSupport.countTicketFiltered(model);
        if(responseCount != null){
            total = responseCount.getResult();

        }

        return total;
    }
}
