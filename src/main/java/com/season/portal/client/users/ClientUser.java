package com.season.portal.client.users;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.GetAllUsersRequest;
import com.season.portal.client.generated.GetAllUsersResponse;
import com.season.portal.client.generated.GetUserByIdRequest;
import com.season.portal.client.generated.GetUserByIdResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class ClientUser extends WebServiceGatewaySupport{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public GetAllUsersResponse getAllUsers() {
        GetAllUsersRequest request = new GetAllUsersRequest();

        GetAllUsersResponse response = null;
        try {
            response = (GetAllUsersResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        }catch (Exception e){
            PortalApplication.log(LOGGER, e);
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
}

