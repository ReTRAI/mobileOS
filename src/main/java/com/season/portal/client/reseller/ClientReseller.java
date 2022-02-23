package com.season.portal.client.reseller;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.reseller.*;
import com.season.portal.reseller.ResellerListPageModel;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

public class ClientReseller extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public SetResellerResponse setReseller(String userId, String actionUserId) {
        SetResellerRequest request = new SetResellerRequest();
        request.setUserId(userId);
        request.setActionUserId(actionUserId);

        SetResellerResponse response = null;
        try {
            response = (SetResellerResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientReseller_setReseller_noCode");
            else
                PortalApplication.addErrorKey("api_ClientReseller_setReseller_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_setReseller_ex");
        }

        return response;
    }

    public RemoveResellerResponse removeReseller(String resellerId, String actionUserId) {
        RemoveResellerRequest request = new RemoveResellerRequest();
        request.setResellerId(resellerId);
        request.setActionUserId(actionUserId);

        RemoveResellerResponse response = null;
        try {
            response = (RemoveResellerResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientReseller_removeReseller_noCode");
            else
                PortalApplication.addErrorKey("api_ClientReseller_removeReseller_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_removeReseller_ex");
        }

        return response;
    }

    public GetResellerByUserIdResponse getResellerByUserId(String userId) {
        GetResellerByUserIdRequest request = new GetResellerByUserIdRequest();
        request.setUserId(userId);

        GetResellerByUserIdResponse response = null;
        try {
            response = (GetResellerByUserIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientReseller_getResellerByUserId_noCode");
            else
                PortalApplication.addErrorKey("api_ClientReseller_getResellerByUserId_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_getResellerByUserId_ex");
        }

        return response;
    }

    public SetResellerAssociationResponse setResellerAssociation(String parentResellerId, String childResellerId, String actionUserId ){
        SetResellerAssociationRequest request = new SetResellerAssociationRequest();
        request.setParentResellerId(parentResellerId);
        request.setChildResellerId(childResellerId);
        request.setActionUserId(actionUserId);

        SetResellerAssociationResponse response = null;
        try {
            response = (SetResellerAssociationResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientReseller_setResellerAssociation_noCode");
            else
                PortalApplication.addErrorKey("api_ClientReseller_setResellerAssociation_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_setResellerAssociation_ex");
        }

        return response;
    }

    public RemoveResellerAssociationResponse removeResellerAssociation(String parentResellerId, String childResellerId, String actionUserId ){
        RemoveResellerAssociationRequest request = new RemoveResellerAssociationRequest();
        request.setParentResellerId(parentResellerId);
        request.setChildResellerId(childResellerId);
        request.setActionUserId(actionUserId);

        RemoveResellerAssociationResponse response = null;
        try {
            response = (RemoveResellerAssociationResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientReseller_removeResellerAssociation_noCode");
            else
                PortalApplication.addErrorKey("api_ClientReseller_removeResellerAssociation_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_removeResellerAssociation_ex");
        }

        return response;
    }

    public GetResellerParentByChildIdResponse getParent(String childResellerId, boolean addMsg_dontExist){
        GetResellerParentByChildIdRequest request = new GetResellerParentByChildIdRequest();
        request.setChildResellerId(childResellerId);

        GetResellerParentByChildIdResponse response = null;
        try {
            response = (GetResellerParentByChildIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            switch(code){
                case "":
                    PortalApplication.addErrorKey("api_ClientReseller_getParent_noCode");
                    break;
                case "ASSOCIATION_DONT_EXISTS":
                    if(addMsg_dontExist)
                        PortalApplication.addErrorKey("api_ClientReseller_getParent_"+code);
                    break;
                default:
                    PortalApplication.addErrorKey("api_ClientReseller_getParent_"+code);
                    break;
            }

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_getParent_ex");
        }

        return response;
    }

    public GetCountResellerFilteredResponse countResellerFiltered(ResellerListPageModel model){
        String resellerName=model.getResellerName();
        resellerName = (resellerName == null)?"":resellerName;

        return countResellerFiltered(
                model.getResellerId(),
                resellerName,
                model.isOnlyChildren()
        );
    }
    public GetCountResellerFilteredResponse countResellerFiltered(String resellerId, String resellerName, boolean onlyChildren){
        GetCountResellerFilteredRequest request = new GetCountResellerFilteredRequest();
        request.setResellerId(resellerId);
        request.setResellerName(resellerName);
        request.setOnlyChildren(onlyChildren);

        GetCountResellerFilteredResponse response = null;
        try {
            response = (GetCountResellerFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientReseller_countResellerFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientReseller_countResellerFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_countResellerFiltered_ex");
        }

        return response;
    }

    public GetResellerFilteredResponse getResellerFiltered(ResellerListPageModel model){
        String resellerName=model.getResellerName();
        resellerName = (resellerName == null)?"":resellerName;

        return getResellerFiltered(
                model.getResellerId(),
                resellerName,
                model.isOnlyChildren(),
                model.getValidOffset(),
                model.getValidNumPerPage(),
                model.getValidSort(),
                model.getValidOrder()
        );
    }
    public GetResellerFilteredResponse getResellerFiltered(String resellerId, String resellerName, boolean onlyChildren,
                                                   int offset, int numberRecords, String field, String order){
        GetResellerFilteredRequest request = new GetResellerFilteredRequest();
        request.setResellerId(resellerId);
        request.setResellerName(resellerName);
        request.setOnlyChildren(onlyChildren);

        request.setOffset(offset);
        request.setNumberRecords(numberRecords);
        request.setField(field);
        request.setOrderField(order);

        GetResellerFilteredResponse response = null;
        try {
            response = (GetResellerFilteredResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientReseller_getResellerFiltered_noCode");
            else
                PortalApplication.addErrorKey("api_ClientReseller_getResellerFiltered_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientReseller_getResellerFiltered_ex");
        }

        return response;
    }

}

