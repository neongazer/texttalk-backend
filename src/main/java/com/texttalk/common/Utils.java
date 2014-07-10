package com.texttalk.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by Andrew on 24/06/2014.
 */
public class Utils {

    public static void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
        }
    }

    public static void waitForMillis(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
        }
    }

    public static String convertFromUTF8(String s, String encoding) {
        String out = null;
        try {
            out = new String(s.getBytes(encoding), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    public static String convertToUTF8(String s, String encoding) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), encoding);
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    public static Config getConfig() {
        return ConfigFactory.load();
    }
}
