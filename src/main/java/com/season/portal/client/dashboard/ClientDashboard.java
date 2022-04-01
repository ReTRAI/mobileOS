package com.season.portal.client.dashboard;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.dashboard.*;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;

public class ClientDashboard extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public GetDashboardByResellerIdResponse getDashboardByResellerId(String resellerId, boolean adminRecursive) {
        GetDashboardByResellerIdRequest request = new GetDashboardByResellerIdRequest();
        request.setResellerId(resellerId);
        request.setRecursive(adminRecursive);

        GetDashboardByResellerIdResponse response = null;
        try {
            response = (GetDashboardByResellerIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapDetail(soapEx, "code") ;

            if(code.equals(""))
                PortalApplication.addErrorKey("api_ClientDashboard_getDashboardByResellerId_noCode");
            else
                PortalApplication.addErrorKey("api_ClientDashboard_getDashboardByResellerId_"+code);

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.addErrorKey("api_ClientDashboard_getDashboardByResellerId_ex");
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

}

