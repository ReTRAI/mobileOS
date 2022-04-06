package com.season.portal.notifications;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.auth.admin.AdminController;
import com.season.portal.client.generated.notification.GetCountUserNotificationFilteredResponse;
import com.season.portal.client.generated.notification.GetUserNotificationFilteredResponse;
import com.season.portal.client.generated.notification.UserNotification;
import com.season.portal.client.generated.support.*;
import com.season.portal.client.notification.ClientNotification;
import com.season.portal.client.support.ClientSupport;
import com.season.portal.client.users.ClientUser;
import com.season.portal.support.SupportListPageModel;
import com.season.portal.ticket.*;
import com.season.portal.users.UserRoleModel;
import com.season.portal.utils.ModelViewBaseController;
import com.season.portal.utils.Utils;
import com.season.portal.utils.model.GuidRequiredModel;
import com.season.portal.utils.model.StringModel;
import com.season.portal.utils.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static com.season.portal.configuration.AnnotationSecurityConfiguration.*;

@Controller
public class NotificationController extends ModelViewBaseController {
    @Autowired
    ClientNotification clientNotification;

    @PreAuthorize(ALLOW_ROLES_ALL)
    @GetMapping("/notifications/open")
    public ModelAndView notificationListOpen() {
        NotificationListPageModel model = new NotificationListPageModel();
        model.setNumPerPage(10);
        model.tryDefaultOrder();

        return notificationListView(model);
    }

    @PreAuthorize(ALLOW_ROLES_SUP_ADMIN)
    @GetMapping("/notifications")
    public ModelAndView notificationList(@Valid NotificationListPageModel model, BindingResult result) {
        if(!result.hasErrors()){
            model.tryDefaultOrder();
        }
        return notificationListView(model);
    }

    private ModelAndView notificationListView(NotificationListPageModel model){
        ModelAndView mv = new ModelAndView("notification/notifications");
        ArrayList<UserNotification> elements = new ArrayList<UserNotification>();
        long totalElements = 0;

        ClientUserDetails user = Utils.getPrincipalDetails(true);
        if(user != null){
            model.setElementGuid(user.getUserId());
            GetCountUserNotificationFilteredResponse responseCount = clientNotification.countUserNotificationsFiltered(model);
            if(responseCount != null){
                totalElements = responseCount.getResult();
                if(totalElements>0){
                    GetUserNotificationFilteredResponse response = clientNotification.getUserNotificationsFiltered(model);
                    if(response != null){
                        elements = new ArrayList(response.getUserNotification());
                    }
                }
            }
        }

        Pagination pagination = new Pagination(totalElements, model.getPage(), model.getNumPerPage(), 4);

        mv.addObject("elements", elements);
        mv.addObject("Pagination", pagination);
        mv.addObject("notificationListPageModel", model);

        return dispatchView(mv);
    }


}
