package com.season.portal.language;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    @PostMapping(value={"/getIndexTranslation"})
    public Map<String, String> getIndexTranslation(@Valid LanguageModel model, BindingResult result) throws IOException {

        if(!result.hasErrors()){
            Resource resource = new ClassPathResource("/private/language/index_"+model.getCode()+".json");
            try {
                ObjectMapper mapper = new ObjectMapper();
                //Object obj =  mapper.readValue(resource.getInputStream(), Object.class);
                Map<String, String> map = mapper.readValue(resource.getInputStream(), Map.class);
                map.put("code", model.getCode());
                return map;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @PostMapping(value={"/getTranslation"})
    public Map<String, String> getTranslation(@Valid LanguageModel model, BindingResult result) throws IOException {
        //TO_DO:Validate Login
        if(!result.hasErrors()){
            Resource resource = new ClassPathResource("/private/language/translation_"+model.getCode()+".json");
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, String> map =  mapper.readValue(resource.getInputStream(), Map.class);
                map.put("code", model.getCode());
                return map;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
