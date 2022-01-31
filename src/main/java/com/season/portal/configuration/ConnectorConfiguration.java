package com.season.portal.configuration;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectorConfiguration {
    @Autowired
    PortalConfiguration portalConfig;

    //https://www.youtube.com/watch?v=HLSmjZ5vN0w
    @Bean
    public ServletWebServerFactory servletContainer(){
        // Enable SSL Trafic
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(org.apache.catalina.Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");

                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");

                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);


                // Define Same site cookie parameter
                Rfc6265CookieProcessor rfc6265CookieProcessor = new Rfc6265CookieProcessor();
                rfc6265CookieProcessor.setSameSiteCookies("Strict");

                context.setCookieProcessor(rfc6265CookieProcessor);
            }
        };

        tomcat.addAdditionalTomcatConnectors( httpToHttpsRedicetConnector());
        return tomcat;
    }


    private Connector httpToHttpsRedicetConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(portalConfig.getNormalPort());
        connector.setSecure(false);
        connector.setRedirectPort(portalConfig.getSslPort());
        return connector;
    }
}
