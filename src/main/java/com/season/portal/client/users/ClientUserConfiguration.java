package com.season.portal.client.users;

import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@Import({PortalConfiguration.class})
public class ClientUserConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    @Bean
    public Jaxb2Marshaller marshallerClientUser() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.season.portal.client.generated");
        return marshaller;
    }
    @Bean
    public ClientUser clientUser(Jaxb2Marshaller marshallerClientUser) {
        ClientUser client = new ClientUser();
        client.setDefaultUri(portalConfig.getClientUserURI());
        client.setMarshaller(marshallerClientUser);
        client.setUnmarshaller(marshallerClientUser);
        return client;
    }
}
