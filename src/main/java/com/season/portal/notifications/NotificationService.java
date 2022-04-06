package com.season.portal.notifications;

import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.reseller.GetResellerByIdResponse;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.GetSupportByIdResponse;
import com.season.portal.client.generated.support.GetTicketFilteredResponse;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.generated.support.Ticket;
import com.season.portal.client.notification.ClientNotification;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.ticket.TicketListPageModel;
import com.season.portal.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class NotificationService {

    public static void sendNotifictionToResseler(ClientNotification clientNotification, ClientReseller clientReseller, String resellerId, String detailKey, String actionUserId, String extraInfo){
        if(extraInfo == null)
            extraInfo = "";
        GetResellerByIdResponse response = clientReseller.getResellerById(resellerId);
        if(response != null){
            Reseller reseller = response.getReseller();
            if(reseller != null){
                String userId = reseller.getUserId();
                clientNotification.setUserNotification(userId, detailKey, extraInfo, actionUserId);
            }
        }
    }

    public static void sendNotifictionToSupport(ClientNotification clientNotification, ClientSupport clientSupport, String supportId, String detailKey, String actionUserId, String extraInfo){
        if(extraInfo == null)
            extraInfo = "";
        GetSupportByIdResponse response = clientSupport.getSupportById(supportId);
        if(response != null){
            Support support = response.getSupport();
            if(support != null){
                String userId = support.getUserId();
                clientNotification.setUserNotification(userId, detailKey, extraInfo, actionUserId);
            }
        }
    }

    public static void sendNotifiction(ClientNotification clientNotification, String userId, String detailKey, String actionUserId, String extraInfo){
        if(extraInfo == null)
            extraInfo = "";

        clientNotification.setUserNotification(userId, detailKey, extraInfo, actionUserId);

    }

    public static void sendNotifictionByTicketReply(ClientNotification clientNotification, ClientSupport clientSupport, String ticketId, ClientUserDetails user){
        GetTicketFilteredResponse response = clientSupport.getTicketFiltered(new TicketListPageModel(ticketId));
        if(response != null){
            ArrayList<Ticket> tickets = new ArrayList(response.getTicket());
            if(tickets.size()>0){
                Ticket ticket = tickets.get(0);
                if(ticket.getCreationUserId().equals(user.getUserId())){//reply from ticket creator
                    if(Utils.isGuid(ticket.getAssignedUserId()) &&
                            !ticket.getAssignedUserId().equals(user.getUserId())){//if someone is assign and is not the creator
                        clientNotification.setUserNotification(ticket.getAssignedUserId(),
                                "api_notification_ticketReplyFromCreator", ticketId, user.getUserId());
                    }
                }
                else{//reply from support/admin
                    clientNotification.setUserNotification(ticket.getCreationUserId(),
                            "api_notification_ticketReplyFromSupport_toCreator", ticketId, user.getUserId());
                    if(Utils.isGuid(ticket.getAssignedUserId()) &&
                            !ticket.getAssignedUserId().equals(user.getUserId())){
                        clientNotification.setUserNotification(ticket.getCreationUserId(),
                                "api_notification_ticketReplyFromSupport_toAssignedUser", ticketId, user.getUserId());
                    }
                }

            }
        }
    }
}
