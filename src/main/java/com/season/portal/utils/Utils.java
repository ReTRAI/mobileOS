package com.season.portal.utils;

import com.season.portal.PortalApplication;
import com.season.portal.client.generated.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.w3c.dom.Node;

import javax.security.auth.x500.X500Principal;
import javax.xml.transform.dom.DOMSource;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Utils {

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
        return dateToStr(d, new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"));
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
        return strToDate(strDate, new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"));
    }
    public static Date strToTime(String strDate){
        return strToDate(strDate, new SimpleDateFormat ("HH:mm:ss"));
    }
    public static Date strToDate(String strDate, String pattern){
        return strToDate(strDate, new SimpleDateFormat (pattern));
    }
    public static Date strToDate(String s, SimpleDateFormat ft){
        Date result = null;
        try {
            result = ft.parse(s);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        return result;
    }


    public static ArrayList<GrantedAuthority> rolesToGrantedAuthorities(List<UserRole> roles) {
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        GrantedAuthority a = new SimpleGrantedAuthority("ROLE_ADMIN");
        authorities.add(a);

        for(UserRole role:roles){
            GrantedAuthority ga = new SimpleGrantedAuthority(role.getUserRoleName());
            authorities.add(ga);
        }

        return authorities;
    }
}
