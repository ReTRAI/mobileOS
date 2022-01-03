package com.season.portal;

import com.season.portal.language.LanguageService;
import com.season.portal.notifications.Notification;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootApplication
public class PortalApplication {

	private static ArrayList<String> errorKeys = new ArrayList<>();
	private static ArrayList<String> successKeys = new ArrayList<>();
	private static HashMap<String, String> messages = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
	}

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

			}
		};

		tomcat.addAdditionalTomcatConnectors( httpToHttpsRedicetConnector());
		return tomcat;
	}

	private Connector httpToHttpsRedicetConnector() {
		int normalport = 8080;
		int securePort = 8443;
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(normalport);
		connector.setSecure(false);
		connector.setRedirectPort(securePort);
		return connector;
	}


	public static void addErrorKey(String sKey) {
		errorKeys.add(sKey);
	}
	public static void addSuccessKey(String sKey) {
		successKeys.add(sKey);
	}
	public static void addMsg(String k, String v)
	{
		messages.put(k, v);
	}

	public static ModelAndView addStatus(ModelAndView mv) {
		mv.addObject("langCodes", LanguageService.LANGUAGE_CODES);
		mv.addObject("selectedLang", "pt");

		mv.addObject("errorKeys",errorKeys);
		mv.addObject("successKeys",successKeys);
		mv.addObject("messages",messages);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()){

			ArrayList<Notification> notifications = new ArrayList<Notification>();
			notifications.add(new Notification(1l, "login/", "Login", "go to login page"));
			notifications.add(new Notification(2l, "logout/", "Logout", ""));
			notifications.add(new Notification(3l, "", "No link", "test sub"));

			mv.addObject("notifications", notifications);
			mv.addObject("userName", auth.getName());
			mv.addObject("userRole", auth.getAuthorities().toString());
		}

		clearStatus();
		return mv;
	}



	private static void clearStatus() {
		errorKeys = new ArrayList<>();
		successKeys = new ArrayList<>();
		messages = new HashMap<>();
	}
}
