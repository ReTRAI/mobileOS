package com.season.portal.utils;

import com.season.portal.PortalApplication;
import com.season.portal.auth.ClientUserDetails;
import com.season.portal.client.generated.device.Device;
import com.season.portal.client.generated.reseller.Reseller;
import com.season.portal.client.generated.support.Support;
import com.season.portal.client.generated.user.UserRole;
import com.season.portal.devices.SimpleDeviceModel;
import com.season.portal.users.UserRoleModel;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.w3c.dom.Node;

import javax.security.auth.x500.X500Principal;
import javax.xml.transform.dom.DOMSource;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {

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
            PortalApplication.log(LoggerFactory.getLogger(new Utils().getClass()), e);
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
            PortalApplication.log(LoggerFactory.getLogger(new Utils().getClass()), e);
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
        try {
            result = ft.parse(s);
        } catch (Exception e) {
            //e.printStackTrace();
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
}
