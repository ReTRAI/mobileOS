package com.season.portal.utils;

import javax.security.auth.x500.X500Principal;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Utils {

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
}
