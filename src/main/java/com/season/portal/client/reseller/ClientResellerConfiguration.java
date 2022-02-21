package com.season.portal.client.reseller;

import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@Import({PortalConfiguration.class})
public class ClientResellerConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    @Bean
    public Jaxb2Marshaller marshallerClientUser() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.season.portal.client.generated.reseller");
        return marshaller;
    }
    @Bean
    public ClientReseller clientUser(Jaxb2Marshaller marshallerClientResseler) {
        ClientReseller client = new ClientReseller();
        client.setDefaultUri(portalConfig.getClientResellerURI());
        client.setMarshaller(marshallerClientResseler);
        client.setUnmarshaller(marshallerClientResseler);
        return client;
    }
}
