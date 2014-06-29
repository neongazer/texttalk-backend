package com.texttalk.commons;

import com.typesafe.config.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class UtilsTest {

    private final static Logger logger = LoggerFactory.getLogger(UtilsTest.class);

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