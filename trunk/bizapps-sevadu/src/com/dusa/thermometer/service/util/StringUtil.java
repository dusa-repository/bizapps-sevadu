package com.dusa.thermometer.service.util;

public class StringUtil {
    
    public static String fillWithString(String s, String character, int size, boolean right) {
        String result = "";
        int amount = size - s.length();
        for (int i = 0; i < amount; i++) {
            result += character;
        }
        if (right) 
            result = s + result;
        else
            result += s;
        return result;
    }
    
}
