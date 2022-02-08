package com.season.portal.client.user;

import com.season.portal.client.users.ClientUser;
import com.season.portal.client.users.generated.GetAllUsersRequest;
import com.season.portal.client.users.generated.GetAllUsersResponse;
import com.season.portal.client.users.generated.GetUserByIdResponse;

import com.season.portal.client.users.generated.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClientUserTest {

    @Autowired
    ClientUser client;

    @Test
    public void getAllUsers() {
        GetAllUsersResponse response = client.getAllUsers();
        if(response.getUsers() != null){
            //assertEquals("pedro.di.moura@gmail.com", response.getUser().getUserEmail());
        }
    }

    @Test
    public void getUserById_1() {
        GetUserByIdResponse response = client.getUserById(1);
        if(response != null){
            User u = response.getUser();
            if(u != null){
                assertEquals("pedro.di.moura@gmail.com", u.getUserEmail());
            }
        }
    }
}
