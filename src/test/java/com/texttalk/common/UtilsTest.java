package com.texttalk.common;

import com.typesafe.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UtilsTest {

    private final static Logger logger = LoggerFactory.getLogger(UtilsTest.class);

    @Test
    public void testConvertFromAndToUTF8() throws Exception {

        String originalText = "Labas, as krabas!";
        String convertedText = Utils.convertFromUTF8(originalText, "Windows-1257");

        logger.debug("Original text: " + originalText);
        logger.debug("Converted text: " + convertedText);

        assertEquals(Utils.convertToUTF8(convertedText, "Windows-1257"), originalText);
    }

    @Test
    public void testGetConfig() throws Exception {

        logger.debug(Utils.getConfig().getString("appPrefix"));
        assertEquals(Utils.getConfig().getString("appPrefix"), "dummy_");
    }

    @Test
    public void testSettings() throws Exception {

        logger.debug(Settings.all.getString("synthesizers.psola.execPath"));

        assertEquals(Settings.all.getString("appPrefix"), "dummy_");
        assertTrue(Settings.all.getString("transcribers.psola.execPath").contains("dummy_"));
        assertTrue(Settings.all.getString("synthesizers.psola.execPath").contains("dummy_"));
    }

    @Test(expectedExceptions = ConfigException.class)
    public void testNonExistingSettings() throws Exception {

        logger.debug(Settings.all.getString("something"));
    }
}