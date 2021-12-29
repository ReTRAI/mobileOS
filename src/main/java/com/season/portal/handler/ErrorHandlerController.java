package com.season.portal.handler;

import com.season.portal.PortalApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ErrorHandlerController implements ErrorController {

    //Vem do CustomAccessDeniedHandler
    @RequestMapping(value={"/errorHandler"})
    public ModelAndView errorHandler(){
        ModelAndView mv = new ModelAndView("errorHandler");
        return PortalApplication.addStatus(mv);
    }

    //geral da aplicação
    @RequestMapping(value={"/error"})
    public ModelAndView error(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("errorHandler");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);


        if(status != null){
            switch(status.toString()){
                case "404":
                case "403":
                    PortalApplication.addMsg("errorTitle","api_error_"+status.toString()+"_title");
                    PortalApplication.addMsg("errorMsg","api_error_"+status.toString()+"_message");
                    break;
                default:
                    PortalApplication.addMsg("errorTitle","api_error_title");
                    PortalApplication.addMsg("errorMsg","api_error_message");
            }
        }

        return PortalApplication.addStatus(mv);
    }
}
