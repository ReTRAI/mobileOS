package com.season.portal.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.user.ChangeLangPreferenceResponse;
import com.season.portal.client.notification.ClientNotification;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.RestHashMapModel;
import com.season.portal.utils.model.RestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_ALL;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_SUP_ADMIN;
import static com.season.portal.utils.validation.LangCodeValidator.LANGUAGE_CODES;

@RestController
public class LanguageController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final static String SESSION_LANGUAGE_CODE = "SESSION_LANGUAGE_CODE";
    @Autowired
    ClientUser clientUser;

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
    @PreAuthorize(ALLOW_ROLES_ALL)
    public RestHashMapModel getTranslation(@Valid LanguageModel model, BindingResult result, HttpServletRequest request) {
        RestHashMapModel restModel = new RestHashMapModel();
        if(!result.hasErrors()){
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if(user == null){
                restModel.addErrorKey("api_error_sessionExpired");
            }
            else{
                Resource resource = new ClassPathResource("/private/language/translation_"+model.getCode()+".json");
                HashMap<String, String> r = getTranslations(resource, model.getCode());
                if(r != null){
                    restModel.setResult(r, true);
                    HttpSession session = request.getSession(true);
                    session.setAttribute(SESSION_LANGUAGE_CODE, model.getCode());

                    ChangeLangPreferenceResponse response = clientUser.setLanguage(user.getUserId(), model.getCode(), user.getUserId());
                }
            }
        }
        else{
            PortalApplication.addErrorKey(result);
            PortalApplication.log(LOGGER, request, "Invalid lang code:"+model.getCode());
        }

        return (RestHashMapModel)PortalApplication.addStatus(restModel);
    }


    public static void setCurrentLanguageCode(HttpSession session, String code){
        if(Arrays.asList(LANGUAGE_CODES).contains(code.toLowerCase())){
            session.setAttribute(SESSION_LANGUAGE_CODE, code.toLowerCase());
        }
    }

    public static String getCurrentLanguageCode(HttpSession session){
        String code = (String)session.getAttribute(SESSION_LANGUAGE_CODE);
        if(!Arrays.asList(LANGUAGE_CODES).contains(code)){
            if (LANGUAGE_CODES.length>0)
                code = LANGUAGE_CODES[0];
            else
                code = "";
        }
        return code;
    }
}
