package com.season.portal.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@PropertySource(value={"classpath:application.properties"})
@ConfigurationProperties(prefix = "portal")
public class PortalConfiguration {

    private String baseURL;
    private String baseURLPath;
    private int normalPort;
    private int sslPort;

    private String ticketTmp;

    private String clientUserURI;
    private String clientNotificationURI;
    private String clientResellerURI;
    private String clientSupportURI;
    private String clientDashboardURI;
    private String clientDeviceURI;

    public String getAppBaseTmpPath(){
        Path root = Paths.get(System.getProperty("user.dir")).getFileSystem()
                .getRootDirectories().iterator().next();
        return root.toString()+"portalApp/";
    }

    public String getClientNotificationURI() {
        return clientNotificationURI;
    }

    public void setClientNotificationURI(String clientNotificationURI) {
        this.clientNotificationURI = clientNotificationURI;
    }

    public String getClientDeviceURI() {
        return clientDeviceURI;
    }

    public void setClientDeviceURI(String clientDeviceURI) {
        this.clientDeviceURI = clientDeviceURI;
    }

    public String getClientDashboardURI() {
        return clientDashboardURI;
    }

    public void setClientDashboardURI(String clientDashboardURI) {
        this.clientDashboardURI = clientDashboardURI;
    }

    public String getClientSupportURI() {
        return clientSupportURI;
    }

    public void setClientSupportURI(String clientSupportURI) {
        this.clientSupportURI = clientSupportURI;
    }

    public String getClientResellerURI() {
        return clientResellerURI;
    }

    public void setClientResellerURI(String clientResellerURI) {
        this.clientResellerURI = clientResellerURI;
    }

    public String getClientUserURI() {
        return clientUserURI;
    }

    public void setClientUserURI(String clientUserURI) {
        this.clientUserURI = clientUserURI;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public void setBaseURLPath(String baseURLPath) {
        this.baseURLPath = baseURLPath;
    }

    public void setNormalPort(int normalPort) {
        this.normalPort = normalPort;
    }

    public void setSslPort(int sslPort) {
        this.sslPort = sslPort;
    }

    public String getPortalURL() {
        String result = baseURL;
        if(sslPort != 80){
            result += ":"+sslPort;
        }
        result+=baseURLPath;
        if(!result.endsWith("/")){
            result+="/";
        }
        return result;
    }
    public String getPortalURL(String path) {
        return getPortalURL()+path;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getBaseURLPath() {
        return baseURLPath;
    }

    public int getNormalPort() {
        return normalPort;
    }

    public int getSslPort() {
        return sslPort;
    }



    public String getTicketTmp() {
        return getAppBaseTmpPath()+ticketTmp;
    }

    public void setTicketTmp(String ticketTmp) {
        this.ticketTmp = ticketTmp;
    }
}