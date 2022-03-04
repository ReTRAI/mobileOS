package com.season.portal.client.dashboard;

import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@Import({PortalConfiguration.class})
public class ClientDashboardConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    @Bean
    public Jaxb2Marshaller marshallerClientDashboard() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.season.portal.client.generated.dashboard");
        return marshaller;
    }

    @Bean
    public ClientDashboard clientDashboard(Jaxb2Marshaller marshallerClientDashboard) {
        ClientDashboard client = new ClientDashboard();
        client.setDefaultUri(portalConfig.getClientDashboardURI());
        client.setMarshaller(marshallerClientDashboard);
        client.setUnmarshaller(marshallerClientDashboard);
        return client;
    }
}
