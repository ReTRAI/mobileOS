package com.season.portal.client.support;

import com.season.portal.PortalApplication;

import com.season.portal.client.generated.support.*;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

public class ClientSupport extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
                case "ASSOCIATION_DONT_EXISTS":
                    if(addMsg_dontExist){
                        PortalApplication.addErrorKey("api_ClientSupport_getParent_"+code);
                    }

                    break;
                default:
                    PortalApplication.addErrorKey("api_ClientSupport_getParent_"+code);
                    break;
            }
            if(code.equals("ASSOCIATION_DONT_EXISTS")){
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
/*
    public GetCountAvailableSupportParentResponse countAvailableSupportParent(String supportId){
        GetCountAvailableSupportParentRequest request = new GetCountAvailableSupportParentRequest();
        request.setSupportId(support);

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
    */

}

