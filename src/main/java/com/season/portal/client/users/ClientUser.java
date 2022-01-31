package com.season.portal.client.users;

import com.season.portal.client.users.generated.GetUserByIdRequest;
import com.season.portal.client.users.generated.GetUserByIdResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import com.season.portal.client.users.generated.GetAllUsersRequest;
import com.season.portal.client.users.generated.GetAllUsersResponse;

public class ClientUser extends WebServiceGatewaySupport{
    public GetAllUsersResponse getAllUsers() {
        GetAllUsersRequest request = new GetAllUsersRequest();

        GetAllUsersResponse response = (GetAllUsersResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }

    public GetUserByIdResponse getUserById(int userId) {
        GetUserByIdRequest request = new GetUserByIdRequest();
        request.setUserId(userId);

        GetUserByIdResponse response = (GetUserByIdResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }
}

