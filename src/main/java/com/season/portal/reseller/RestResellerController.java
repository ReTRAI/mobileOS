package com.season.portal.reseller;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.reseller.GetCountResellerFilteredResponse;
import com.season.portal.client.generated.reseller.GetResellerFilteredResponse;
import com.season.portal.client.generated.user.ExistUserEmailResponse;
import com.season.portal.client.generated.user.ExistUserNameResponse;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.client.users.ClientUser;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.ArrayList;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_RES_ADMIN;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_SUP_ADMIN;


@RestController
public class RestResellerController extends ModelViewBaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ClientReseller clientReseller;

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @ResponseBody
    @PostMapping(value={"resellers/countResellers"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestLongModel countResellers(@Valid GuidRequiredModel model, BindingResult result){
        RestLongModel restLongModel = new RestLongModel();
        if(!result.hasErrors()) {
            ResellerListPageModel requestModel = new ResellerListPageModel();
            requestModel.setResellerId(model.getValue());
            requestModel.setOnlyChildren(true);

            GetCountResellerFilteredResponse responseCount = clientReseller.countResellerFiltered(requestModel);
            if(responseCount != null) {
                restLongModel.setResult(responseCount.getResult(), true);
            }
        }
        return (RestLongModel)PortalApplication.addStatus(restLongModel);
    }

    @PreAuthorize(ALLOW_ROLES_RES_ADMIN)
    @ResponseBody
    @PostMapping(value={"resellers/getResellers"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestResellerListModel getResellers(@Valid GuidRequiredPageModel model, BindingResult result){
        RestResellerListModel restResellerListModel = new RestResellerListModel();
        if(!result.hasErrors()) {
            ResellerListPageModel requestModel = new ResellerListPageModel();
            requestModel.setResellerId(model.getValue());
            requestModel.setOnlyChildren(true);

            GetResellerFilteredResponse response = clientReseller.getResellerFiltered(requestModel);
            if(response != null){
                restResellerListModel.setResult(new ArrayList(response.getReseller()), true);
            }
        }
        return (RestResellerListModel)PortalApplication.addStatus(restResellerListModel);
    }
}
