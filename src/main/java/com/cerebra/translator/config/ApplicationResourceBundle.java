package com.cerebra.translator.config;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class ApplicationResourceBundle {

    public static String getString(String key) {
        if (ResourceBundle.getBundle("messages").containsKey(key)) {
            String value = ResourceBundle.getBundle("messages").getString(key);
            try {
                return new String(value.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return value;
            }
        } else {
            return key;
        }
    }
}
