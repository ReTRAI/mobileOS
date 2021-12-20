package com.season.portal;

import com.season.portal.auth.model.LoginModel;
import com.season.portal.language.LanguageService;
import com.season.portal.notifications.Notification;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class PortalApplication {

	private static ArrayList<String> errorKeys = new ArrayList<>();
	private static ArrayList<String> successKeys = new ArrayList<>();
	private static HashMap<String, String> messages = new HashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
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

		boolean logged = true;
		if(logged){

			ArrayList<Notification> notifications = new ArrayList<Notification>();
			notifications.add(new Notification(1l, "login/", "Login", "go to login page"));
			notifications.add(new Notification(2l, "logout/", "Logout", ""));
			notifications.add(new Notification(3l, "", "No link", "test sub"));

			mv.addObject("notifications", notifications);
			mv.addObject("userName", "Clera");
			mv.addObject("userRole", "Admin");
		}

		clearStatus();
		return mv;
	}



	private static void clearStatus() {
		errorKeys = new ArrayList<>();
		successKeys = new ArrayList<>();
		messages = new HashMap<>();
	}

	public static boolean login(HttpSession httpSession, LoginModel model){
		httpSession.setAttribute("USER_ROLE", "SERV");
		httpSession.setAttribute("IS_LOGGED", true);
		return true;
	}

	public static boolean logout(HttpSession httpSession){
		httpSession.setAttribute("USER_ROLE", "");
		httpSession.setAttribute("IS_LOGGED", false);
		httpSession.invalidate();

		return true;
	}
}
