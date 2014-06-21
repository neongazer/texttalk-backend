package com.texttalk.core.encoders;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;

import static org.testng.Assert.*;

public class MP3LameEncoderTest {

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testProcess() throws Exception {

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new String("Hello World!").getBytes("UTF-8")));

        new MP3LameEncoder().setInputStream(in)
        .setOutputFile(new File("resources/test.txt"))
        .process();
    }
}