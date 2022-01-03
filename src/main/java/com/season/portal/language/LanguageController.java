package com.season.portal.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@RestController
public class LanguageController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Map<String, String> getTranslations(Resource rTranslation, String code){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> translation = null;
        Resource rSharedTranslation = new ClassPathResource("/private/language/shared_"+code+
                ".json");

        try {
            translation =  mapper.readValue(rTranslation.getInputStream(), Map.class);

            Map<String, String> sharedTranslation =  mapper.readValue(rSharedTranslation.getInputStream(), Map.class);
            translation.putAll(sharedTranslation);
            translation.put("code", code);

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return translation;
    }

    @PostMapping(value={"/getIndexTranslation"})
    public Map<String, String> getIndexTranslation(@Valid LanguageModel model, BindingResult result) throws IOException {

        if(!result.hasErrors()){
            Resource resource = new ClassPathResource("/private/language/index_"+model.getCode()+".json");
            return getTranslations(resource, model.getCode());
        }

        return null;
    }

    @PostMapping(value={"/getTranslation"})
    public Map<String, String> getTranslation(@Valid LanguageModel model, BindingResult result) throws IOException {
        if(!result.hasErrors()){
            Resource resource = new ClassPathResource("/private/language/translation_"+model.getCode()+".json");
            return getTranslations(resource, model.getCode());
        }

        return null;
    }
}
