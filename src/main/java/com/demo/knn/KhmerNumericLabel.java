package com.demo.knn;

import java.util.concurrent.ConcurrentHashMap;
/**
 * @author Engleang
 * Util class to map Khmer numeral to Arabic number.
 * */
public class KhmerNumericLabel {
    private static ConcurrentHashMap<String,String>labelMap = new ConcurrentHashMap<String, String>(){
        {
            put("0","០");
            put("1","១");
            put("2","២");
            put("3","៣");
            put("4","៤");
            put("5","៥");
            put("6","៦");
            put("7","៧");
            put("8","៨");
            put("9","៩");


        }
    };

    public static String valueOf(String numeric)
    {
        if(!labelMap.containsKey( numeric)) {
            throw new IllegalArgumentException( numeric +" does not exist in mapping ");
        }
        return labelMap.get( numeric);
    }
}
