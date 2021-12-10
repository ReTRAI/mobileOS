package com.season.portal.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.portal.PortalApplication;
import com.season.portal.auth.model.LoginModel;
import com.season.portal.dashboard.DashboardController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
public class LanguageController {
    @PostMapping(value={"/getIndexTranslation"})
    public Object getIndexTranslation(@Valid LanguageModel model, BindingResult result) throws IOException {

        if(!result.hasErrors()){
            Resource resource = new ClassPathResource("/private/language/index_"+model.getCode()+".json");
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object objMapper =  mapper.readValue(resource.getInputStream(), Object.class);
                return objMapper;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @PostMapping(value={"/getTranslation"})
    public Object getTranslation(@Valid LanguageModel model, BindingResult result) throws IOException {
        //TO_DO:Validate Login
        if(!result.hasErrors()){
            Resource resource = new ClassPathResource("/private/language/translation_"+model.getCode()+".json");
            try {
                ObjectMapper mapper = new ObjectMapper();
                Object objMapper =  mapper.readValue(resource.getInputStream(), Object.class);
                return objMapper;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
