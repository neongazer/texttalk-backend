package com.texttalk.common;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.io.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.stream.Stream;

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

    public static String convertInputStreamToString(InputStream inStream) {
        java.util.Scanner s = new java.util.Scanner(inStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String getMD5(String str) {
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString();
    }

    public static Config getConfig() {
        return ConfigFactory.load();
    }
}
