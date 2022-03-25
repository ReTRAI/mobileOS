package com.season.portal.utils;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.device.Device;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.generated.user.UserRole;
import com.season.portal.devices.SimpleDeviceModel;
import com.season.portal.users.UserRoleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.w3c.dom.Node;

import javax.security.auth.x500.X500Principal;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.season.portal.utils.validation.FileValidatorUtils.groupsToFileTypes;
import static com.season.portal.utils.validation.GuidValidator.UUID_REGEX_PATTERN;

public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger("Utils");

    public static ClientUserDetails getPrincipalDetails(boolean addMsg){
        ClientUserDetails user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated()) {
            var principal = auth.getPrincipal();
            if (principal instanceof ClientUserDetails) {
                user = (ClientUserDetails)principal;
            }
        }

        if(user == null && addMsg)
            PortalApplication.addErrorKey("api_invalidUserDetails");

        return user;
    }

    public static String getSoapCode(SoapFaultClientException soapEx){
        String code = "";
        try {
            SoapFault soapFault = soapEx.getSoapFault();
            SoapFaultDetail detail  = soapFault.getFaultDetail();
            SoapFaultDetailElement detailElementChild = detail.getDetailEntries().next();
            DOMSource domDetailSource = (DOMSource)detailElementChild.getSource();
            Node detailNode = domDetailSource.getNode();
            Node detailChildNode_code = detailNode.getFirstChild();
            code = detailChildNode_code.getTextContent();
        }catch (Exception e){
            PortalApplication.log(LOGGER, e);
        }

        return code;
    }

    public static String getSoapDetail(SoapFaultClientException soapEx, String name){
        String message = "";
        try {
            SoapFault soapFault = soapEx.getSoapFault();
            SoapFaultDetail detail  = soapFault.getFaultDetail();
            var entries = detail.getDetailEntries();
            while(entries.hasNext()) {
                SoapFaultDetailElement detailElementChild = entries.next();
                if(detailElementChild.getName().toString().equals(name)){
                    DOMSource domDetailSource = (DOMSource)detailElementChild.getSource();
                    Node detailNode = domDetailSource.getNode();
                    Node detailChildNode_description = detailNode.getFirstChild();
                    if(detailChildNode_description != null){
                        message = detailChildNode_description.getTextContent();
                    }

                    break;
                }
            }

        }catch (Exception e){
            PortalApplication.log(LOGGER, e);
        }

        return message;
    }

    public static String gsbk(HashMap<String, String> hm, String key, String defaultValue ){

        if(hm != null){
            if(hm.containsKey(key)){
                return hm.get(key);
            }
        }

        return defaultValue;
    }

    public static String parseCertificate(X500Principal certificate, String param){
        String result = "";

        String dn = certificate.toString();
        String[] parts = dn.split(",");

        for (String part : parts)
        {
            if(part.trim().startsWith(param) && part.indexOf("=")!=-1){
                result = part.split("=")[1];
                break;
            };
        }

        return result;
    }
    public static int certificateExpireIn(X509Certificate certificate){
        Date certExpiryDate = certificate.getNotAfter();
        Date today = new Date();
        long dateDiff = (certExpiryDate.getTime() - today.getTime());
        long expiresIn = dateDiff / (24 * 60 * 60 * 1000);
        /** /
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String text =("Expires On: " + certExpiryDate + "\tFormated Date: " + ft.format(certExpiryDate) + "\tToday's Date: " + ft.format(today) + "\tExpires In: "+ expiresIn);
        /**/
        return (int)expiresIn;
    }

    public static String dateToStr(Date d){
        return dateToStr(d, new SimpleDateFormat ("yyyy-MM-dd"));
    }
    public static String dateTimeToStr(Date d){
        return dateToStr(d, new SimpleDateFormat ("yyyy-MM-dd HH:mm"));
    }
    public static String timeToStr(Date d){
        return dateToStr(d, new SimpleDateFormat ("HH:mm:ss"));
    }
    public static String dateToStr(Date d, String pattern){
        return dateToStr(d, new SimpleDateFormat (pattern));
    }
    public static String dateToStr(Date d, SimpleDateFormat ft){
        String result = "";
        if(d != null)
            result = ft.format(d);
        return result;
    }
    public static Date strToDate(String strDate){
        return strToDate(strDate, new SimpleDateFormat ("yyyy-MM-dd"));
    }
    public static Date strToDateTime(String strDate){
        return strToDate(strDate, new SimpleDateFormat ("yyyy-MM-dd HH:mm"));
    }
    public static Date strToTime(String strDate){
        return strToDate(strDate, new SimpleDateFormat ("HH:mm"));
    }
    public static Date strToDate(String strDate, String pattern){
        return strToDate(strDate, new SimpleDateFormat (pattern));
    }
    public static Date strToDate(String s, SimpleDateFormat ft){
        Date result = null;
        if(s != null){
            try {
                result = ft.parse(s);
            } catch (Exception e) {
                PortalApplication.log(LOGGER, e, "String: "+s);
            }
        }
        return result;
    }
    public static String strToStrDate(String strDate){
        return strToStrDate(strDate, new SimpleDateFormat ("yyyy-MM-dd"));
    }
    public static String strToStrDate(String strDate, SimpleDateFormat originalDateFt){
        String result = "";
        Date date = strToDate(strDate, originalDateFt);
        if(date != null) {
            result = dateToStr(date);
        }
        return result;
    }
    public static String strToStrDateTime(String strDate){
        return strToStrDateTime(strDate, new SimpleDateFormat ("yyyy-MM-dd HH:mm"));
    }
    public static String strToStrDateTime(String strDate, SimpleDateFormat originalDateFt){
        String result = "";
        Date date = strToDate(strDate, originalDateFt);
        if(date != null) {
            result = dateTimeToStr(date);
        }
        return result;
    }

    public static ArrayList<GrantedAuthority> rolesToGrantedAuthorities(List<UserRole> roles) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for(UserRole role:roles){
            GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_"+role.getUserRoleName());
            authorities.add(ga);
        }

        return authorities;
    }
    public static boolean hasRole(List<UserRole> roles, String checkRole) {
        boolean has = false;
        for(UserRole role:roles) {
            if (role.getUserRoleName().equals(checkRole)){
                has = true;
                break;
            }
        }
        return has;
    }


    public static ArrayList<UserRoleModel> resellerToUserRole(ArrayList<Reseller> elements) {
        ArrayList<UserRoleModel> result = new ArrayList<>();
        if(elements != null){
            for(Reseller e:elements){
                UserRoleModel u = new UserRoleModel(e);
                result.add(u);
            }
        }
        return result;
    }
    public static ArrayList<UserRoleModel> supportToUserRole(ArrayList<Support> elements) {
        ArrayList<UserRoleModel> result = new ArrayList<>();
        if(elements != null){
            for(Support e:elements){
                UserRoleModel u = new UserRoleModel(e);
                result.add(u);
            }
        }
        return result;
    }

    public static ArrayList<SimpleDeviceModel> resellerToSimpleDeviceModel(ArrayList<Device> elements) {
        ArrayList<SimpleDeviceModel> result = new ArrayList<SimpleDeviceModel>();
        if(elements != null){
            for(Device e:elements){
                SimpleDeviceModel u = new SimpleDeviceModel(e);
                result.add(u);
            }
        }
        return result;
    }

    public static float parseFloat(String number){
        float result = 0f;
        if(number != null){
            number = number.replace(",", ".");
            try{
                result = Float.parseFloat(number);
            }catch(Exception e){
                PortalApplication.log(LOGGER, e, "String: "+number);
            }
        }
        return result;
    }

    public static String generateRandomName(int charNum){
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(charNum);
        for (int i = 0; i < charNum; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    public static String saveFileIfExist(MultipartFile file, boolean addErrorMsg){
        String savedfileName = null;
        if(file != null && !file.isEmpty()){
            String extension = getMediaTypeExt(file, addErrorMsg);
            if(extension != null){
                String fileName = generateRandomName(24)+"."+extension;
                File filePath = new File("src/main/resources/media/tickets/");
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }

                File dest = new File(filePath.getAbsolutePath(), fileName);
                while(dest.exists()){
                    fileName = generateRandomName(24)+"."+extension;
                    dest = new File(filePath.getAbsolutePath(), fileName);
                }

                try{
                    file.transferTo(dest);
                    savedfileName = fileName;
                }catch(Exception e){
                    PortalApplication.log(LOGGER, e);
                    if(addErrorMsg)
                        PortalApplication.addErrorKey("api_utils_saveFileIfExist_error");
                }
            }
        }
        else{
            savedfileName = "";
        }
        return savedfileName;
    }

    public static String getMediaTypeExt(MultipartFile file, boolean addErrorMsg){
        //String fileName = file.getOriginalFilename();
        //fileName.substring(fileName.lastIndexOf(".") + 1);
        return getMediaTypeExt(file.getContentType(), addErrorMsg);
    }

    public static String getMediaTypeExt(String type, boolean addErrorMsg){
        type = type.toLowerCase();
        String result = null;
        switch(type){
            case "image/jpeg":
            case "image/jpg":
                result = "jpg";
                break;
            case "image/png":
                result = "png";
                break;
            case "application/zip":
                result = "zip";
                break;
            case "application/pdf":
                result = "pdf";
                break;
            case "application/x-rar-compressed":
                result = "rar";
                break;
            default:
                if(addErrorMsg)
                    PortalApplication.addErrorKey("api_utils_getMediaTypeExt_invalidMediaType");
                break;
        }
        return result;
    }

    public static String getAcceptTypes(String[] groups, String[] fileTypes){
        String result = "";
        fileTypes = groupsToFileTypes(groups, fileTypes);
        for (int i = 0; i < fileTypes.length; i++){
            String ft = fileTypes[i];
            if(i!=0)
                result+= ", ";
            switch(ft){
                case "jpeg":
                case "jpg":
                case "png":
                    result+="image/"+ft;
                    break;
                case "zip":
                case "pdf":
                    result+="application/"+ft;
                    break;
                case "rar":
                    result+="application/x-rar-compressed";
                    break;
            }
        }
        return result;
    }

    public static String makeResume(String str, int maxChar){
        String result = "";
        if(str != null){
            if(str.length()>maxChar){
                result = str.substring(0, maxChar);
                result = result.substring(0, result.lastIndexOf(" "));
                result += "(...)";
            }
            else{
                result = str;
            }
        }
        return result;
    }

    public static boolean isGuid(String guid){
        boolean result = false;
        if(guid != null){
            if(UUID_REGEX_PATTERN.matcher(guid).matches()){
                result = true;
            }
        }
        return result;
    }

}
