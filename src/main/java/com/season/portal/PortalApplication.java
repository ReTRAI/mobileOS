package com.season.portal;

import com.season.portal.auth.ChangePassModel;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.generated.user.GetUserByIdResponse;
import com.season.portal.client.users.ClientUser;
import com.season.portal.language.LanguageController;
import com.season.portal.notifications.Notification;
import com.season.portal.utils.model.RestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ws.soap.client.SoapFaultClientException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.season.portal.utils.validation.LangCodeValidator.LANGUAGE_CODES;

@SpringBootApplication
public class PortalApplication{

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static ArrayList<String> errorKeys = new ArrayList<>();
	private static ArrayList<String> successKeys = new ArrayList<>();


	public static void main(String[] args) {
		SpringApplication.run(PortalApplication.class, args);
	}

	public static void addErrorKey(String sKey) {
		errorKeys.add(sKey);
	}
	public static void addErrorKey(BindingResult bindingResult) {
		List<FieldError> errors = bindingResult.getFieldErrors();
		for (FieldError error : errors ) {
			addErrorKey(error.getDefaultMessage());
		}
	}
	public static void addSuccessKey(String sKey) {
		successKeys.add(sKey);
	}

	public static ModelAndView addStatus(ModelAndView mv, HttpServletRequest request) {



		HttpSession session = request.getSession(true);
		String code = LanguageController.getCurrentLanguageCode(session);

		mv.addObject("selectedLang", code);


		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null && auth.isAuthenticated()){
			var principal = auth.getPrincipal();

			//Logged
			if(principal instanceof ClientUserDetails){

				mv.addObject("userName", auth.getName());
				mv.addObject("userRole", auth.getAuthorities().toString());
				if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
					mv.addObject("adminView",AdminController.getAdminView(session));
				}

				if(((ClientUserDetails)principal).needChangPass()){
					String viewName = mv.getViewName();
					mv.addObject("needToChangePass",true);

					if(!viewName.equals("changePassword")){
						mv.setViewName("changePassword");
						mv.addObject("changePassModel", new ChangePassModel());
						addErrorKey("api_needToChangePass");
					}

				}
				else{
					ArrayList<Notification> notifications = new ArrayList<Notification>();
					notifications.add(new Notification(1l, "login/", "Login", "go to login page"));
					notifications.add(new Notification(2l, "logout/", "Logout", ""));
					notifications.add(new Notification(3l, "", "No link", "test sub"));
					mv.addObject("notifications", notifications);
				}
			}
		}

		mv.addObject("langCodes", LANGUAGE_CODES);
		mv.addObject("errorKeys",errorKeys);
		mv.addObject("successKeys",successKeys);
		clearStatus();
		return mv;
	}

	public static RestModel addStatus(RestModel pr) {
		pr.addErrorKeys(errorKeys);
		pr.addSuccessKeys(successKeys);

		clearStatus();
		return pr;
	}

	private static void clearStatus() {
		errorKeys = new ArrayList<>();
		successKeys = new ArrayList<>();
	}

	public static void log(Logger LOGGER, String msg){
		LOGGER.error(msg+ "\n-----------------------------------------------\n");
	}
	public static void log(Logger LOGGER, Exception ex){
		log(LOGGER, ex.getMessage());
	}

	public static void log(Logger LOGGER, SoapFaultClientException ex, String code){
		log(LOGGER, code+ "\n" +ex.getMessage());
	}
	public static void log(Logger LOGGER, HttpServletRequest request){

		String logMsg = "Method: "+request.getMethod()+ "\n" +
				"Request URI: "+request.getRequestURI()+ "\n" +
				"Remote Addr: "+request.getRemoteAddr()+ "\n" +
				"Remote User: "+request.getRemoteUser() + "\n" +
				"Request URL: "+request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);

		log(LOGGER, request, "");
	}
	public static void log(Logger LOGGER, HttpServletRequest request, String msg){

		String logMsg = "Method: "+request.getMethod()+ "\n" +
				"Request URI: "+request.getRequestURI()+ "\n" +
				"Remote Addr: "+request.getRemoteAddr()+ "\n" +
				"Remote User: "+request.getRemoteUser() + "\n" +
				"Request URL: "+request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI)+ "\n" +msg;

		log(LOGGER, logMsg);
	}

	public static void logout(HttpServletRequest request, HttpServletResponse response){
		invalidateSSLSession(request);

		request.setAttribute("javax.servlet.request.X509Certificate", "");

		HttpSession session= request.getSession(false);
		if(session != null) {
			session.invalidate();
		}

		SecurityContextHolder.getContext().setAuthentication(null);
		SecurityContextHolder.clearContext();

		response.addHeader("Connection", "close");// open new socket next time

	}

	public static boolean invalidateSSLSession(HttpServletRequest httpRequest) {
		Logger LOGGER = LoggerFactory.getLogger(PortalApplication.class);

         /*
        SSLSessionManager mgr =(SSLSessionManager)request.getAttribute("javax.servlet.request.ssl_session_mgr");
        if(mgr != null) {
            mgr.invalidateSession();
        }
        try {
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init( null, null, null );
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        }catch (Exception e){
            LOGGER.error("Failed to get SSLContext: " + e.getMessage(), e);
        }
        */


		Method invalidateSessionMethod = null;
		Object mgr = httpRequest.getAttribute("javax.servlet.request.ssl_session_mgr");
		if (mgr != null) {
			try {
				invalidateSessionMethod =
						mgr.getClass().getMethod("invalidateSession");
				if (invalidateSessionMethod == null) {
					log(LOGGER, "Failed to reset SSL: failed to return method");
				}

				invalidateSessionMethod.setAccessible(true);
			} catch (Exception e) {
				log(LOGGER, e);
			}

			// Invalidate the session
			try {
				invalidateSessionMethod.invoke(mgr);
				return true;
			} catch (Exception e) {
				log(LOGGER, e);
			}
		}

		return false;
	}

	public static boolean updatePrincipal(ClientUser client, boolean addMsg) {
		boolean valid = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null && auth.isAuthenticated()) {
			var principal = auth.getPrincipal();
			if (principal instanceof ClientUserDetails) {
				ClientUserDetails user = (ClientUserDetails)principal;
				GetUserByIdResponse response = client.getUserById(user.getUserId());
				if(response != null){
					ClientUserDetails newUser = new ClientUserDetails(response.getUser(), (ArrayList<GrantedAuthority>) user.getAuthorities());
					Authentication authentication = new UsernamePasswordAuthenticationToken(newUser, "userObject.getPassword()", newUser.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					valid = true;
				}
			}
		}

		return valid;
	}
}
