package com.season.portal.users;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.user.ExistUserEmailResponse;
import com.season.portal.client.generated.user.ExistUserNameResponse;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.model.RestBoolModel;
import com.season.portal.utils.model.StringModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_SUP_ADMIN;


@RestController
public class RestUsersController extends ModelViewBaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ClientUser client;

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @ResponseBody
    @PostMapping(value={"users/validNickname"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestBoolModel validNickname(StringModel model){
        RestBoolModel restBoolModel = new RestBoolModel();

        boolean validNickname = false;
        boolean worked = false;
        String value = model.getValue();
        if(value != null && !value.equals("")){
            ExistUserNameResponse response = client.existUserName(value);
            if(response != null){
                validNickname = !response.isResult();
                worked = true;
            }
        }
        else{
            worked = true;
        }

        restBoolModel.setResult(validNickname, worked);
        return (RestBoolModel)PortalApplication.addStatus(restBoolModel);
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @ResponseBody
    @PostMapping(value={"users/validEmail"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestBoolModel validEmail(StringModel model){
        RestBoolModel restBoolModel = new RestBoolModel();

        boolean validEmail = false;
        boolean worked = false;
        String value = model.getValue();
        if(value != null && !value.equals("")){
            ExistUserEmailResponse response = client.existUserEmail(value);
            if(response != null){
                validEmail = !response.isResult();
                worked = true;
            }
        }
        else{
            worked = true;
        }

        restBoolModel.setResult(validEmail, worked);
        return (RestBoolModel)PortalApplication.addStatus(restBoolModel);
    }
}
