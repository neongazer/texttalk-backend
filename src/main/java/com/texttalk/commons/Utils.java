package com.texttalk.commons;

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

    public static Config getConfig() {
        return ConfigFactory.load();
    }
}
