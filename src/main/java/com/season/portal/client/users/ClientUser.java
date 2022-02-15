package com.season.portal.client.users;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.*;
import com.season.portal.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.SoapFaultException;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

public class ClientUser extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public enum USER_STATUS {
        CHANGEPW
    }

    public UserLoginResponse tryLogin(String email, String pass) {
        UserLoginRequest request = new UserLoginRequest();
        request.setUserEmail(email);
        request.setUserPassword(pass);

        UserLoginResponse response = null;
        try {
            response = (UserLoginResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals("")){
                PortalApplication.addErrorKey("api_ClientUser_tryLogin_noCode");
            }
            else{
                PortalApplication.addErrorKey("api_ClientUser_tryLogin_"+code);
            }

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_tryLogin_ex");
        }

        return response;
    }

    public GetUserRolesByUserIdResponse getRolesById(int userId) {
        GetUserRolesByUserIdRequest request = new GetUserRolesByUserIdRequest();
        request.setUserId(userId);

        GetUserRolesByUserIdResponse response = null;
        try {
            response = (GetUserRolesByUserIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }
        catch (SoapFaultClientException soapEx){
            String code = Utils.getSoapCode(soapEx);

            if(code.equals("")){
                PortalApplication.addErrorKey("api_ClientUser_GetUserRolesByUserIdResponse_noCode");
            }
            else{
                PortalApplication.addErrorKey("api_ClientUser_GetUserRolesByUserIdResponse_"+code);
            }

            PortalApplication.log(LOGGER, soapEx, code);

        } catch (Exception e){
            PortalApplication.log(LOGGER, e);
            PortalApplication.addErrorKey("api_ClientUser_GetUserRolesByUserIdResponse_ex");
        }

        return response;
    }

    public GetUserByIdResponse getUserById(int userId) {
        GetUserByIdRequest request = new GetUserByIdRequest();
        request.setUserId(userId);

        GetUserByIdResponse response = null;
        try {
            response = (GetUserByIdResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }catch (Exception e){
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

    public UnblockUserResponse unblockUserResponse(int userId) {
        UnblockUserRequest request = new UnblockUserRequest();
        request.setUserId(userId);

        UnblockUserResponse response = null;
        try {
            response = (UnblockUserResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }catch (Exception e){
            PortalApplication.log(LOGGER, e);
        }

        return response;
    }

}

