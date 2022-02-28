package com.season.portal.client.reseller;

import com.season.portal.client.generated.reseller.SetResellerResponse;
import com.season.portal.client.users.ClientUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClientResellerTest {

    @Autowired
    ClientReseller client;

    @Test
    public void getUserById_1() {
        //SetResellerResponse response = client.setReseller("", "");
        //String ola = "";
        /*
        GetUserByIdResponse response = client.getUserById(9);
        if(response != null){
            User u = response.getUser();
            if(u != null){
                assertEquals("pedro.di.moura@gmail.com", u.getUserEmail());
            }
        }

         */
    }

}
