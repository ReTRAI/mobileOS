package com.season.portal.handler;

import com.season.portal.PortalApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class ErrorHandlerController implements ErrorController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //Vem do CustomAccessDeniedHandler
    @RequestMapping(value={"/securityErrorHandler"})
    public ModelAndView errorHandler(){
        ModelAndView mv = new ModelAndView("errorHandler");
        return PortalApplication.addStatus(mv);
    }

    //geral da aplicação
    @RequestMapping(value={"/error"})
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new ModelAndView("errorHandler");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        PortalApplication.addMsg("errorTitle","api_error_title");
        PortalApplication.addMsg("errorMsg","api_error_message");

        String logMsg = "\nStatus code: "+response.getStatus()+ "\n" +
                "Method: "+request.getMethod()+ "\n" +
                "Request URI: "+request.getRequestURI()+ "\n" +
                "Remote Addr: "+request.getRemoteAddr()+ "\n" +
                "Remote User: "+request.getRemoteUser() + "\n" +
                "Request URL: "+request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);

        LOGGER.error(logMsg);

        if(status != null){

            switch(status.toString()){
                case "404":
                case "403":
                    PortalApplication.addMsg("errorTitle","api_error_"+status.toString()+"_title");
                    PortalApplication.addMsg("errorMsg","api_error_"+status.toString()+"_message");
                    break;
            }
        }

        return PortalApplication.addStatus(mv);
    }
}
