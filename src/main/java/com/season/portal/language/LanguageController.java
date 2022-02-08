package com.season.portal.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.portal.PortalApplication;
import com.season.portal.utils.model.RestHashMapModel;
import com.season.portal.utils.model.RestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LanguageController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final static String SESSION_LANGUAGE_CODE = "SESSION_LANGUAGE_CODE";


    private HashMap<String, String> getTranslations(Resource rTranslation, String code){
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> translation = null;
        Resource rSharedTranslation = new ClassPathResource("/private/language/shared_"+code+
                ".json");

        try {
            translation =  mapper.readValue(rTranslation.getInputStream(), HashMap.class);

            Map<String, String> sharedTranslation =  mapper.readValue(rSharedTranslation.getInputStream(), HashMap.class);
            translation.putAll(sharedTranslation);
            translation.put("code", code);

        } catch (IOException e) {
            PortalApplication.log(LOGGER, e);
        }

        return translation;
    }

    @ResponseBody
    @PostMapping(value={"/getIndexTranslation"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestHashMapModel getIndexTranslation(@Valid LanguageModel model, BindingResult result, HttpServletRequest request){
        RestHashMapModel restModel = new RestHashMapModel();
        if(!result.hasErrors()){
            Resource resource = new ClassPathResource("/private/language/index_"+model.getCode()+".json");
            HashMap<String, String> r = getTranslations(resource, model.getCode());

            if(r != null) {
                restModel.setResult(r, true);
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_LANGUAGE_CODE, model.getCode());
            }
        }
        else{
            PortalApplication.addErrorKey(result);
            PortalApplication.log(LOGGER, request, "Invalid lang code:"+model.getCode());
        }
        restModel = (RestHashMapModel) PortalApplication.addStatus(restModel);
        return restModel;
    }

    @PostMapping(value={"/getTranslation"})
    public RestHashMapModel getTranslation(@Valid LanguageModel model, BindingResult result, HttpServletRequest request) {
        RestHashMapModel restModel = new RestHashMapModel();
        if(!result.hasErrors()){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth == null || auth instanceof AnonymousAuthenticationToken){
                restModel.addErrorKey("api_error_sessionExpired");
            }
            else{
                Resource resource = new ClassPathResource("/private/language/translation_"+model.getCode()+".json");
                HashMap<String, String> r = getTranslations(resource, model.getCode());
                if(r != null){
                    restModel.setResult(r, true);
                    HttpSession session = request.getSession(true);
                    session.setAttribute(SESSION_LANGUAGE_CODE, model.getCode());
                }
            }
        }
        else{
            PortalApplication.addErrorKey(result);
            PortalApplication.log(LOGGER, request, "Invalid lang code:"+model.getCode());
        }

        return (RestHashMapModel)PortalApplication.addStatus(restModel);
    }

    public static String getCurrentLanguageCode(HttpSession session){
        return (String)session.getAttribute(SESSION_LANGUAGE_CODE);
    }
}
