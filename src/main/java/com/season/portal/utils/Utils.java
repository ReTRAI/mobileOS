package com.season.portal.utils;

import javax.security.auth.x500.X500Principal;
import java.security.cert.X509Certificate;
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
}
