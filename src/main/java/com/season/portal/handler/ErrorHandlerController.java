package com.season.portal.handler;

import com.season.portal.PortalApplication;
import com.season.portal.language.LanguageModel;
import com.season.portal.utils.model.RestHashMapModel;
import com.season.portal.utils.model.RestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;

@Controller
public class ErrorHandlerController implements ErrorController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    //Vem do CustomAccessDeniedHandler
    @RequestMapping(value={"/securityErrorHandler"})
    public ModelAndView errorHandler(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("errorHandler");
        mv.addObject("errorTitle", request.getAttribute("errorTitle"));
        mv.addObject("errorMsg", request.getAttribute("errorMsg"));
        return PortalApplication.addStatus(mv);
    }

    //geral da aplicação
    @RequestMapping(value={"/error"})
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mv = new ModelAndView("errorHandler");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        mv.addObject("errorTitle","api_error_title");
        mv.addObject("errorMsg","api_error_message");

        PortalApplication.log(LOGGER, request, "Status code: "+response.getStatus());

        if(status != null){

            switch(status.toString()){
                case "404":
                case "403":
                    mv.addObject("errorTitle","api_error_"+status.toString()+"_title");
                    mv.addObject("errorMsg","api_error_"+status.toString()+"_message");
                    break;
            }
        }

        return PortalApplication.addStatus(mv);
    }
    /*
    @PostMapping(value={"/error"})
    public RestModel getTranslation(HttpServletRequest request, HttpServletResponse response) {
        RestModel restModel = new RestModel();

        PortalApplication.log(LOGGER, request, "Status code: "+response.getStatus());
        restModel.addErrorKey("api_error_message");

        return PortalApplication.addStatus(restModel);
    }
    */
}
