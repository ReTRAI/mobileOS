package com.season.portal.handler;

import com.season.portal.PortalApplication;
import com.season.portal.language.LanguageModel;
import com.season.portal.utils.ModelViewBaseController;
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
public class ErrorHandlerController extends ModelViewBaseController implements ErrorController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value={"/error"})
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response){
        int statusCode = response.getStatus();
        PortalApplication.log(LOGGER, request, "Status code: "+statusCode);

        //Endpoint original
        String uri = request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI).toString();
        //Se o pedido for a um endpoint REST é marcado para não ser devolvida a página html de resposta de erro
        switch(uri){
            case "/getTranslation":
            case "/getIndexTranslation":
            case "/toggleAdminView":
                return null;
        }

        ModelAndView mv = new ModelAndView("errorHandler");

        mv.addObject("errorTitle","api_error_title");
        mv.addObject("errorMsg","api_error_message");

        switch(statusCode){
            case 404:
                mv.addObject("errorTitle","api_error_"+response.getStatus()+"_title");
                mv.addObject("errorMsg","api_error_"+response.getStatus()+"_message");
                break;
            case 403:
                mv.addObject("errorTitle","api_error_"+response.getStatus()+"_title");

                HttpSession session = request.getSession(false);
                if(session.isNew()){
                    mv.addObject("errorMsg","api_error_sessionExpired");
                }
                else{
                    mv.addObject("errorMsg","api_error_"+response.getStatus()+"_message");
                }
                break;
        }


        return dispatchView(mv);
    }
}
