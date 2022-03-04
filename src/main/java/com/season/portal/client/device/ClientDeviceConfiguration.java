package com.season.portal.client.device;

import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@Import({PortalConfiguration.class})
public class ClientDeviceConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    @Bean
    public Jaxb2Marshaller marshallerClientDevice() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.season.portal.client.generated.device");
        return marshaller;
    }
    @Bean
    public ClientDevice clientDevice(Jaxb2Marshaller marshallerClientDevice) {
        ClientDevice client = new ClientDevice();
        client.setDefaultUri(portalConfig.getClientDeviceURI());
        client.setMarshaller(marshallerClientDevice);
        client.setUnmarshaller(marshallerClientDevice);
        return client;
    }
}
