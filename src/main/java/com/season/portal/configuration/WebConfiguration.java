package com.season.portal.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
        @Autowired
        PortalConfiguration portalConfig;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new TransactionInterceptor(portalConfig));
        }
}
