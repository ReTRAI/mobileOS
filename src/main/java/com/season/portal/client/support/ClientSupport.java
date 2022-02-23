package com.season.portal.client.support;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.reseller.GetResellerByUserIdRequest;
import com.season.portal.client.generated.reseller.GetResellerByUserIdResponse;
import com.season.portal.client.generated.reseller.SetResellerRequest;
import com.season.portal.client.generated.reseller.SetResellerResponse;
import com.season.portal.client.generated.support.GetSupportByUserIdRequest;
import com.season.portal.client.generated.support.GetSupportByUserIdResponse;
import com.season.portal.client.generated.support.SetSupportRequest;
import com.season.portal.client.generated.support.SetSupportResponse;
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
            String code = Utils.getSoapCode(soapEx);

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

    public GetSupportByUserIdResponse getSupportByUserId(String userId) {
        GetSupportByUserIdRequest request = new GetSupportByUserIdRequest();
        request.setUserId(userId);

        GetSupportByUserIdResponse response = null;
        try {
            response = (GetSupportByUserIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientSupport_getResellerByUserId_noCode");
            else
                PortalApplication.addErrorKey("api_ClientSupport_getResellerByUserId_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientSupport_getResellerByUserId_ex");
        }

        return response;
    }

}

