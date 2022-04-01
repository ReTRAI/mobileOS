package com.season.portal.client.notification;

import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@Import({PortalConfiguration.class})
public class ClientNotificationConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    @Bean
    public Jaxb2Marshaller marshallerClientNotification() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.season.portal.client.generated.notification");
        return marshaller;
    }
    @Bean
    public ClientNotification clientNotification(Jaxb2Marshaller marshallerClientNotification) {
        ClientNotification client = new ClientNotification();
        client.setDefaultUri(portalConfig.getClientNotificationURI());
        client.setMarshaller(marshallerClientNotification);
        client.setUnmarshaller(marshallerClientNotification);
        return client;
    }
}
