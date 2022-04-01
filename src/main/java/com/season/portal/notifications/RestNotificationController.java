package com.season.portal.notifications;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.notification.*;
import com.season.portal.client.notification.ClientNotification;
import com.season.portal.client.reseller.ClientReseller;
import com.season.portal.language.LanguageModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.model.RestBoolModel;
import com.season.portal.utils.model.RestModel;
import com.season.portal.utils.model.RestStringModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.ArrayList;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_ADMIN;
import static com.season.portal.configuration.AnnotationSecurityConfiguration.ALLOW_ROLES_ALL;


@RestController
public class RestNotificationController extends ModelViewBaseController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ClientNotification clientNotification;

    @PreAuthorize(ALLOW_ROLES_ALL)
    @ResponseBody
    @PostMapping(value={"/notifications/readAll"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestModel readAll(){
        RestModel restModel = new RestModel();
        boolean worked = false;

        ClientUserDetails user = Utils.getPrincipalDetails(true);
        if(user != null) {
            ArrayList<UserNotification> notifications = new ArrayList<UserNotification>();
            NotificationListPageModel model = new NotificationListPageModel();
            model.setChecked("0");
            model.setElementGuid(user.getUserId());

            GetUserNotificationFilteredResponse response = clientNotification.getUserNotificationsFiltered(model);

            if(response != null){
                boolean allValid = true;
                notifications = new ArrayList(response.getUserNotification());

                for (UserNotification n : notifications) {
                    SetUserNotificationCheckedResponse result = clientNotification.checkUserNotification(n.getUserNotificationId(), user.getUserId(), false);
                    if(result == null){
                        allValid = false;
                    }
                }
                if(!allValid)
                    PortalApplication.addErrorKey("api_ClientNotification_checkUserNotificationAll_error");
                worked = allValid;
            }
        }

        restModel.setWorked(worked);
        return (RestModel)PortalApplication.addStatus(restModel);
    }

    @PreAuthorize(ALLOW_ROLES_ALL)
    @ResponseBody
    @PostMapping(value={"/notifications/checkRead"}, produces={MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE})
    public RestStringModel checkRead(@Valid GuidRequiredModel model, BindingResult result){

        RestStringModel restStringModel = new RestStringModel();
        if(!result.hasErrors()) {
            ClientUserDetails user = Utils.getPrincipalDetails(true);
            if(user != null) {
                /*SetUserNotificationCheckedResponse response = clientNotification.checkUserNotification(model.getValue(), user.getUserId(), true);
                if(response != null){
                    restStringModel.setResult(model.getValue(),  true);
                }*/
                restStringModel.setResult(model.getValue(),  true);
            }
        }
        else{
            PortalApplication.addErrorKey(result);
            PortalApplication.log(LOGGER, request, "Invalid notification guid :"+model.getValue());
        }

        return (RestStringModel)PortalApplication.addStatus(restStringModel);
    }

}
