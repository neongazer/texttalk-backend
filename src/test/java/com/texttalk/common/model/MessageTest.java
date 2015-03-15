package com.texttalk.common.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MessageTest {

    private static Logger logger = LoggerFactory.getLogger(MessageTest.class);

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMessage() throws Exception {

        String json = "{\"id\": \"text1\", \"channel\": \"c1\", \"orderId\": 0, \"hashCode\": \"abcdefgh123456\", \"text\": \"labas ir visogero\", \"synth\": \"luss\", \"totalChunks\": 0, \"voiceTrack\": \"\"}";
        Message msg = Message.getMessage(json);

        logger.info("text: " + msg.getText());
        logger.info("JSON: " + Message.getJSON(msg));

        Assert.assertEquals("labas ir visogero", msg.getText());

    }

    @Test
    public void testGetJSON() throws Exception {

    }
}