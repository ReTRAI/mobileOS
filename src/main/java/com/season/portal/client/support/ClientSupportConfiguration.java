package com.season.portal.client.support;

import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@Import({PortalConfiguration.class})
public class ClientSupportConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    @Bean
    public Jaxb2Marshaller marshallerClientSupport() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.season.portal.client.generated.support");
        return marshaller;
    }
    @Bean
    public ClientSupport clientSupport(Jaxb2Marshaller marshallerClientSupport) {
        ClientSupport client = new ClientSupport();
        client.setDefaultUri(portalConfig.getClientSupportURI());
        client.setMarshaller(marshallerClientSupport);
        client.setUnmarshaller(marshallerClientSupport);
        return client;
    }
}
