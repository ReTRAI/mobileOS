package com.season.portal.client.user;

import com.season.portal.client.users.ClientUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClientUserTest {

    @Autowired
    ClientUser client;
    /*
    @Test
    public void getUserById_1() {
        GetUserByIdResponse response = client.getUserById(9);
        if(response != null){
            User u = response.getUser();
            if(u != null){
                assertEquals("pedro.di.moura@gmail.com", u.getUserEmail());
            }
        }
    }
    */
}
