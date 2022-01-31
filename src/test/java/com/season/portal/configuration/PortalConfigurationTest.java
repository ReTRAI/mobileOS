package com.season.portal.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PortalConfigurationTest {
    @Autowired
    PortalConfiguration portalConfig;
    @Test
    public void portalConfigLoad_baseURL() {
        assertEquals(portalConfig.getBaseURL(), "https://localhost");
    }
    @Test
    public void portalConfigLoad_SSLPORT() {
        assertTrue((portalConfig.getSslPort()== 8451));
    }
}
