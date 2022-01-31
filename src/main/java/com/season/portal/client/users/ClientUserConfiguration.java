package com.season.portal.client.users;

import com.season.portal.configuration.PortalConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
//@ComponentScan(value = "com.season.portal.configuration")//Para conseguir inicializar/encontrar PortalConfiguration
@Import({PortalConfiguration.class})
public class ClientUserConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.season.portal.client.users.generated");
        return marshaller;
    }
    @Bean
    public ClientUser clientUser(Jaxb2Marshaller marshaller) {
        ClientUser client = new ClientUser();
        client.setDefaultUri(portalConfig.getClientUserURI());
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }
}
