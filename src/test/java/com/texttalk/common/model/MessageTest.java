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

        String json = "{\"id\": \"text1\", \"channel\": \"c1\", \"orderId\": 0, \"hashCode\": \"abcdefgh123456\", \"text\": \"labas ir visogero\", \"synth\": \"luss\"}";
        Message msg = Message.getMessage(json);

        logger.debug("text: " + msg.getText());

        Assert.assertEquals("labas ir visogero", msg.getText());

        logger.debug(Message.getJSON(msg));
    }

    @Test
    public void testGetJSON() throws Exception {

    }
}