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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LanguageController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

            if(r != null)
                restModel.setResult(r, true);
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
            Resource resource = new ClassPathResource("/private/language/translation_"+model.getCode()+".json");
            HashMap<String, String> r = getTranslations(resource, model.getCode());
            if(r != null)
                restModel.setResult(r, true);

        }
        else{
            PortalApplication.addErrorKey(result);
            PortalApplication.log(LOGGER, request, "Invalid lang code:"+model.getCode());
        }

        return (RestHashMapModel)PortalApplication.addStatus(restModel);
    }
}
