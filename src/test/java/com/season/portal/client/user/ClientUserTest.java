package com.season.portal.client.user;

import com.season.portal.client.users.ClientUser;
import com.season.portal.client.users.generated.GetUserByIdResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClientUserTest {

    @Autowired
    ClientUser client;

    @Test
    public void getUserById_1() {
        GetUserByIdResponse response = client.getUserById(1);
        assertEquals(response.getUser().getUserEmail(), "pedro.di.moura@gmail.com");
    }
}
