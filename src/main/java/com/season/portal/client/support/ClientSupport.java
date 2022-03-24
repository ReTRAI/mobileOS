package com.season.portal.client.support;

import com.season.portal.PortalApplication;

import com.season.portal.client.generated.support.*;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.ticket.TicketDetailListPageModel;
import com.season.portal.ticket.TicketListPageModel;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

public class ClientSupport extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public enum TICKET_STATUS {
        OPEN,
        CANCELED,
        PENDING,
        ONPROGRESS,
        COMPLETED
    }

    public boolean validateSetSupport(SetSupportResponse response, boolean addMsg) {
        boolean valid = false;

        if(response != null){
            Support ele = response.getSupport();
            if(ele != null){
                valid = true;
                if(addMsg)
                    PortalApplication.addSuccessKey("api_ClientSupport_validateSetSupport_success");
            }
            else if(addMsg){
                PortalApplication.addErrorKey("api_ClientSupport_validateSetSupport_error");
            }
        }
        return valid;
    }

    public boolean validateRemoveSupport(RemoveSupportResponse response, boolean addMsg) {
        boolean valid = false;

        if(response != null){
            if(response.isResult()){
                valid = true;
                if(addMsg)
                    PortalApplication.addSuccessKey("api_ClientSupport_validateRemoveSupport_success");
            }
            else if(addMsg){
                PortalApplication.addErrorKey("api_ClientSupport_validateRemoveSupport_error");
            }
        }
        return valid;
    }

    public SetSupportResponse setSupport(String userId, String actionUserId) {
        SetSupportRequest request = new SetSupportRequest();
        request.setUserId(userId);
        request.setActionUserId(actionUserId);

        SetSupportResponse response = null;
        try {
            response = (SetSupportResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_setSupport_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_setSupport_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_setSupport_ex");
        }

        return response;
    }

    public RemoveSupportResponse removeSupport(String supportId, String actionUserId) {
        RemoveSupportRequest request = new RemoveSupportRequest();
        request.setSupportId(supportId);
        request.setActionUserId(actionUserId);

        RemoveSupportResponse response = null;
        try {
            response = (RemoveSupportResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_removeSupport_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_removeSupport_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_removeSupport_ex");
        }

        return response;
    }

    public GetSupportByUserIdResponse getSupportByUserId(String userId) {
        GetSupportByUserIdRequest request = new GetSupportByUserIdRequest();
        request.setUserId(userId);

        GetSupportByUserIdResponse response = null;
        try {
            response = (GetSupportByUserIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_getSupportByUserId_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_getSupportByUserId_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getSupportByUserId_ex");
        }

        return response;
    }

    public SetSupportAssociationResponse setSupportAssociation(String parentSupportId, String childSupportId, String actionUserId ){
        SetSupportAssociationRequest request = new SetSupportAssociationRequest();
        request.setParentSupportId(parentSupportId);
        request.setChildSupportId(childSupportId);
        request.setActionUserId(actionUserId);

        SetSupportAssociationResponse response = null;
        try {
            response = (SetSupportAssociationResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_setSupportAssociation_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_setSupportAssociation_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_setSupportAssociation_ex");
        }

        return response;
    }

    public boolean validateSetSupportAssociation(SetSupportAssociationResponse response, boolean addMsg) {
        boolean valid = false;

        if(response != null){
            if(response.isResult()){
                valid = true;
                if(addMsg)
                    PortalApplication.addSuccessKey("api_ClientSupport_validateSetSupportAssociation_success");
            }
            else if(addMsg){
                PortalApplication.addErrorKey("api_ClientSupport_validateSetSupportAssociation_error");
            }
        }

        return valid;
    }

    public RemoveSupportAssociationResponse removeSupportAssociation(String parentSupportId, String childSupportId, String actionUserId ){
        RemoveSupportAssociationRequest request = new RemoveSupportAssociationRequest();
        request.setParentSupportId(parentSupportId);
        request.setChildSupportId(childSupportId);
        request.setActionUserId(actionUserId);

        RemoveSupportAssociationResponse response = null;
        try {
            response = (RemoveSupportAssociationResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_removeSupportAssociation_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_removeSupportAssociation_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_removeSupportAssociation_ex");
        }

        return response;
    }

    public GetSupportParentByChildIdResponse getParent(String childSupportId, boolean addMsg_dontExist){
        GetSupportParentByChildIdRequest request = new GetSupportParentByChildIdRequest();
        request.setChildSupportId(childSupportId);

        GetSupportParentByChildIdResponse response = null;
        try {
            response = (GetSupportParentByChildIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;
            switch(code){
                case "":
                    PortalApplication.addErrorKey("api_ClientSupport_getParent_noCode");
                    break;
                case "ASSOCIATION_DONT_EXIST":
                    if(addMsg_dontExist){
                        PortalApplication.addErrorKey("api_ClientSupport_getParent_"+code);
                    }

                    break;
                default:
                    PortalApplication.addErrorKey("api_ClientSupport_getParent_"+code);
                    break;
            }
            if(code.equals("ASSOCIATION_DONT_EXIST")){
                if(addMsg_dontExist)
                    PortalApplication.log(LOGGER, soapEx, code);
            }else{
                PortalApplication.log(LOGGER, soapEx, code);
            }
        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getParent_ex");
        }

        return response;
    }


    public GetCountSupportFilteredResponse countSupportFiltered(SupportListPageModel model){
        String SupportName=model.getSupportName();
        SupportName = (SupportName == null)?"":SupportName;

        return countSupportFiltered(
                model.getSupportId(),
                SupportName,
                model.isOnlyChildren()
        );
    }
    public GetCountSupportFilteredResponse countSupportFiltered(String SupportId, String SupportName, boolean onlyChildren){
        GetCountSupportFilteredRequest request = new GetCountSupportFilteredRequest();
        request.setSupportId(SupportId);
        request.setSupportName(SupportName);
        request.setOnlyChildren(onlyChildren);

        GetCountSupportFilteredResponse response = null;
        try {
            response = (GetCountSupportFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_countSupportFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_countSupportFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_countSupportFiltered_ex");
        }

        return response;
    }

    public GetSupportFilteredResponse getSupportFiltered(SupportListPageModel model){
        String SupportName=model.getSupportName();
        SupportName = (SupportName == null)?"":SupportName;

        return getSupportFiltered(
                model.getSupportId(),
                SupportName,
                model.isOnlyChildren(),
                model.getValidOffset(),
                model.getValidNumPerPage(),
                model.getValidSort(),
                model.getValidOrder()
        );
    }
    public GetSupportFilteredResponse getSupportFiltered(String SupportId, String SupportName, boolean onlyChildren,
                                                           int offset, int numberRecords, String field, String order){
        GetSupportFilteredRequest request = new GetSupportFilteredRequest();
        request.setSupportId(SupportId);
        request.setSupportName(SupportName);
        request.setOnlyChildren(onlyChildren);

        request.setOffset(offset);
        request.setNumberRecords(numberRecords);
        request.setField(field);
        request.setOrderField(order);

        GetSupportFilteredResponse response = null;
        try {
            response = (GetSupportFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_getSupportFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_getSupportFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getSupportFiltered_ex");
        }

        return response;
    }

    public GetSupportByIdResponse getSupportById(String supportId) {
        GetSupportByIdRequest request = new GetSupportByIdRequest();
        request.setSupportId(supportId);

        GetSupportByIdResponse response = null;
        try {
            response = (GetSupportByIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_getSupportById_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_getSupportById_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getSupportById_ex");
        }

        return response;
    }

    public GetCountAvailableSupportParentResponse countAvailableSupportParent(String supportId){
        GetCountAvailableSupportParentRequest request = new GetCountAvailableSupportParentRequest();
        request.setSupportId(supportId);

        GetCountAvailableSupportParentResponse response = null;
        try {
            response = (GetCountAvailableSupportParentResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_countAvailableSupportParent_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_countAvailableSupportParent_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_countAvailableSupportParent_ex");
        }

        return response;
    }

    public GetAvailableSupportParentResponse getAvailableSupportParent(String supportId,
                                                                      int offset, int numberRecords, String field, String order){
        GetAvailableSupportParentRequest request = new GetAvailableSupportParentRequest();
        request.setSupportId(supportId);

        request.setOffset(offset);
        request.setNumberRecords(numberRecords);
        //request.setField(field);
        //request.setOrderField(order);

        GetAvailableSupportParentResponse response = null;
        try {
            response = (GetAvailableSupportParentResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_getAvailableSupportParent_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_getAvailableSupportParent_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getAvailableSupportParent_ex");
        }

        return response;
    }

    public IsHierarchyValidResponse isHierarchyValid(String parentSupportId, String childSupportId){
        IsHierarchyValidRequest request = new IsHierarchyValidRequest();
        request.setSupportId(parentSupportId);
        request.setChildSupportId(childSupportId);

        IsHierarchyValidResponse response = null;
        try {
            response = (IsHierarchyValidResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            switch(code){
                case "":
                    PortalApplication.addErrorKey("api_ClientSupport_isHierarchyValid_noCode");
                    break;
                default:
                    PortalApplication.addErrorKey("api_ClientSupport_isHierarchyValid_"+code);
                    break;
            }

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_isHierarchyValid_ex");
        }

        return response;
    }

    public boolean validateIsHierarchyValid(IsHierarchyValidResponse response, boolean addErrorMsg) {
        boolean valid = false;

        if(response != null){
            if(response.isResult()){
                valid = true;
            }
            else if(addErrorMsg){
                PortalApplication.addErrorKey("api_ClientSupport_validateIsHierarchyValid_error");
            }
        }
        return valid;
    }

    public GetCountTicketFilteredResponse countTicketFiltered(TicketListPageModel model){
        String ticketId = model.getTicketId();
        String ticketStatus = model.getTicketStatus();
        String assignedUserId = model.getAssignedUserId();
        String openUserId = model.getOpenUserId();

        String startCreationDate = Utils.strToStrDate(model.getStartCreationDate());
        String endCreationDate = Utils.strToStrDate(model.getEndCreationDate());

        ticketId = (ticketId == null)?"":ticketId;
        assignedUserId = (assignedUserId == null)?"":assignedUserId;
        openUserId = (openUserId == null)?"":openUserId;
        ticketStatus = (ticketStatus == null)?"":ticketStatus;

        return countTicketFiltered(
                ticketId,
                ticketStatus,
                startCreationDate,
                endCreationDate,
                assignedUserId,
                openUserId
        );
    }
    public GetCountTicketFilteredResponse countTicketFiltered(String ticketId, String ticketStatus,String startCreationDate, String endCreationDate,
                                                              String assignedUserId, String openUserId){
        GetCountTicketFilteredRequest request = new GetCountTicketFilteredRequest();
        request.setTicketId(ticketId);
        request.setTicketStatus(ticketStatus);
        request.setStartCreationDate(startCreationDate);
        request.setEndCreationDate(endCreationDate);
        request.setAssignedUserId(assignedUserId);
        request.setOpenUserId(openUserId);

        GetCountTicketFilteredResponse response = null;
        try {
            response = (GetCountTicketFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_countTicketFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_countTicketFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_countTicketFiltered_ex");
        }

        return response;
    }

    public GetTicketFilteredResponse getTicketFiltered(TicketListPageModel model){
        String ticketId = model.getTicketId();
        String assignedUserId = model.getAssignedUserId();
        String openUserId = model.getOpenUserId();
        String ticketStatus = model.getTicketStatus();

        String startCreationDate = Utils.strToStrDate(model.getStartCreationDate());
        String endCreationDate = Utils.strToStrDate(model.getEndCreationDate());

        ticketId = (ticketId == null)?"":ticketId;
        assignedUserId = (assignedUserId == null)?"":assignedUserId;
        openUserId = (openUserId == null)?"":openUserId;
        ticketStatus = (ticketStatus == null)?"":ticketStatus;

        return getTicketFiltered(
                ticketId,
                ticketStatus,
                startCreationDate,
                endCreationDate,
                assignedUserId,
                openUserId,
                model.getValidOffset(),
                model.getValidNumPerPage(),
                model.getValidSort(),
                model.getValidOrder()
        );
    }
    public GetTicketFilteredResponse getTicketFiltered(String ticketId, String ticketStatus,String startCreationDate, String endCreationDate,
                                                       String assignedUserId, String openUserId,
                                                         int offset, int numberRecords, String field, String order){
        GetTicketFilteredRequest request = new GetTicketFilteredRequest();
        request.setTicketId(ticketId);
        request.setTicketStatus(ticketStatus);
        request.setStartCreationDate(startCreationDate);
        request.setEndCreationDate(endCreationDate);
        request.setAssignedUserId(assignedUserId);
        request.setOpenUserId(openUserId);

        request.setOffset(offset);
        request.setNumberRecords(numberRecords);
        request.setField(field);
        request.setOrderField(order);

        GetTicketFilteredResponse response = null;
        try {
            response = (GetTicketFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_getTicketFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_getTicketFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getTicketFiltered_ex");
        }

        return response;
    }

    public SetTicketResponse setTicket(String title, String msg, String attachPath, String actionUserId) {
        SetTicketRequest request = new SetTicketRequest();
        request.setMessage(msg);
        request.setAttachPath(attachPath);
        request.setTitle(title);
        request.setCreationUserId(actionUserId);

        SetTicketResponse response = null;
        try {
            response = (SetTicketResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_setTicket_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_setTicket_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_setTicket_ex");
        }

        return response;
    }

    public GetCountTicketDetailFilteredResponse countTicketDetailFiltered(TicketDetailListPageModel model){
        GetCountTicketDetailFilteredRequest request = new GetCountTicketDetailFilteredRequest();
        request.setTicketId(model.getTicketId());
        request.setStartDetailDate(model.getValidStartCreationDate());
        request.setEndDetailDate(model.getValidEndCreationDate());
        request.setResponseUserId(model.getValidResponseUserId());

        GetCountTicketDetailFilteredResponse response = null;
        try {
            response = (GetCountTicketDetailFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_countTicketDetailFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_countTicketDetailFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_countTicketDetailFiltered_ex");
        }

        return response;
    }

    public GetTicketDetailFilteredResponse getTicketDetailFiltered(TicketDetailListPageModel model){
        GetTicketDetailFilteredRequest request = new GetTicketDetailFilteredRequest();
        request.setTicketId(model.getTicketId());
        request.setStartDetailDate(model.getValidStartCreationDate());
        request.setEndDetailDate(model.getValidEndCreationDate());

        request.setOffset(model.getValidOffset());
        request.setNumberRecords(model.getValidNumPerPage());
        request.setField(model.getValidSort());
        request.setOrderField(model.getValidOrder());

        GetTicketDetailFilteredResponse response = null;
        try {
            response = (GetTicketDetailFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_getTicketDetailFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_getTicketDetailFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getTicketDetailFiltered_ex");
        }

        return response;
    }

    public SetTicketDetailResponse setTicketDetail(String ticketId, String msg, String attachPath, String actionUserId) {
        SetTicketDetailRequest request = new SetTicketDetailRequest();
        request.setTicketId(ticketId);
        request.setMessage(msg);
        request.setAttachPath(attachPath);
        request.setActionUserId(actionUserId);

        SetTicketDetailResponse response = null;
        try {
            response = (SetTicketDetailResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_setTicketDetail_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_setTicketDetail_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_setTicketDetail_ex");
        }

        return response;
    }

}

