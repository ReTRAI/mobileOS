package com.season.portal.client.notification;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.device.*;
import com.season.portal.client.generated.notification.*;
import com.season.portal.client.generated.support.*;
import com.season.portal.devices.DeviceListPageModel;
import com.season.portal.notifications.NotificationListPageModel;
import com.season.portal.ticket.TicketDetailListPageModel;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

public class ClientNotification extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public GetCountUserNotificationFilteredResponse countUserNotificationsFiltered(NotificationListPageModel model){
        GetCountUserNotificationFilteredRequest request = new GetCountUserNotificationFilteredRequest();
        request.setUserId(model.getElementGuid());
        request.setStartCreationDate(model.getValidStartCreationDate());
        request.setEndCreationDate(model.getValidEndCreationDate());
        request.setStartCheckedDate(model.getValidStartCheckedDate());
        request.setEndCheckedDate(model.getValidEndCheckedDate());
        request.setChecked(model.getValidChecked());

        GetCountUserNotificationFilteredResponse response = null;
        try {
            var oi = getWebServiceTemplate().marshalSendAndReceive(request);
            response = (GetCountUserNotificationFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_countUserNotificationsFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_countUserNotificationsFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_countUserNotificationsFiltered_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public GetUserNotificationFilteredResponse getUserNotificationsFiltered(NotificationListPageModel model){
        GetUserNotificationFilteredRequest request = new GetUserNotificationFilteredRequest();
        request.setUserId(model.getElementGuid());
        request.setStartCreationDate(model.getValidStartCreationDate());
        request.setEndCreationDate(model.getValidEndCreationDate());
        request.setStartCheckedDate(model.getValidStartCheckedDate());
        request.setEndCheckedDate(model.getValidEndCheckedDate());
        request.setChecked(model.getValidChecked());

        request.setOffset(model.getValidOffset());
        request.setNumberRecords(model.getValidNumPerPage());
        request.setField(model.getValidSort());
        request.setOrderField(model.getValidOrder());

        GetUserNotificationFilteredResponse response = null;
        try {
            response = (GetUserNotificationFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_getUserNotificationsFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_getUserNotificationsFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_getUserNotificationsFiltered_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public SetUserNotificationResponse setUserNotification(String userId, String detail, String actionUserId) {
        SetUserNotificationRequest request = new SetUserNotificationRequest();
        request.setUserId(userId);
        request.setDetail(detail);
        request.setActionUserId(actionUserId);

        SetUserNotificationResponse response = null;
        try {
            response = (SetUserNotificationResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_setUserNotification_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_setUserNotification_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_setUserNotification_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public SetUserNotificationCheckedResponse checkUserNotification(String userNotificationId, String actionUserId) {
        SetUserNotificationCheckedRequest request = new SetUserNotificationCheckedRequest();
        request.setUserNotificationId(userNotificationId);
        request.setActionUserId(actionUserId);

        SetUserNotificationCheckedResponse response = null;
        try {
            response = (SetUserNotificationCheckedResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_checkUserNotification_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_checkUserNotification_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_checkUserNotification_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public GetCountDeviceNotificationFilteredResponse countDeviceNotificationsFiltered(NotificationListPageModel model){
        GetCountDeviceNotificationFilteredRequest request = new GetCountDeviceNotificationFilteredRequest();
        request.setDeviceId(model.getElementGuid());
        request.setStartCreationDate(model.getValidStartCreationDate());
        request.setEndCreationDate(model.getValidEndCreationDate());
        request.setStartCheckedDate(model.getValidStartCheckedDate());
        request.setEndCheckedDate(model.getValidEndCheckedDate());
        request.setChecked(model.getValidChecked());

        GetCountDeviceNotificationFilteredResponse response = null;
        try {
            response = (GetCountDeviceNotificationFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_countDeviceNotificationsFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_countDeviceNotificationsFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_countDeviceNotificationsFiltered_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public GetDeviceNotificationFilteredResponse getDeviceNotificationsFiltered(NotificationListPageModel model){
        GetDeviceNotificationFilteredRequest request = new GetDeviceNotificationFilteredRequest();
        request.setDeviceId(model.getElementGuid());
        request.setStartCreationDate(model.getValidStartCreationDate());
        request.setEndCreationDate(model.getValidEndCreationDate());
        request.setStartCheckedDate(model.getValidStartCheckedDate());
        request.setEndCheckedDate(model.getValidEndCheckedDate());
        request.setChecked(model.getValidChecked());

        request.setOffset(model.getValidOffset());
        request.setNumberRecords(model.getValidNumPerPage());
        request.setField(model.getValidSort());
        request.setOrderField(model.getValidOrder());

        GetDeviceNotificationFilteredResponse response = null;
        try {
            response = (GetDeviceNotificationFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_getDeviceNotificationsFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_getDeviceNotificationsFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_getDeviceNotificationsFiltered_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public SetDeviceNotificationResponse setDeviceNotification(String deviceId, String detail, String actionUserId) {
        SetDeviceNotificationRequest request = new SetDeviceNotificationRequest();
        request.setDeviceId(deviceId);
        request.setDetail(detail);
        request.setActionUserId(actionUserId);

        SetDeviceNotificationResponse response = null;
        try {
            response = (SetDeviceNotificationResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_setDeviceNotification_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_setDeviceNotification_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_setDeviceNotification_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public SetDeviceNotificationCheckedResponse checkDeviceNotification(String deviceNotificationId, String actionUserId) {
        SetDeviceNotificationCheckedRequest request = new SetDeviceNotificationCheckedRequest();
        request.setDeviceNotificationId(deviceNotificationId);
        request.setActionUserId(actionUserId);

        SetDeviceNotificationCheckedResponse response = null;
        try {
            response = (SetDeviceNotificationCheckedResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientNotification_checkDeviceNotification_noCode");
            else
                PortalApplication.addErrorKey("api_ClientNotification_checkDeviceNotification_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientNotification_checkDeviceNotification_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

}

