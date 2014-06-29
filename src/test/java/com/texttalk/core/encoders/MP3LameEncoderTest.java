package com.texttalk.core.encoders;

import com.texttalk.commons.Settings;
import com.texttalk.commons.io.FilePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import static org.testng.Assert.*;

public class MP3LameEncoderTest {

    private final static Logger logger = LoggerFactory.getLogger(MP3LameEncoderTest.class);
    private final static String encoderPath = "vm/vagrant/apps/lame_encoder/dummy_lame";

    @Test
    public void testStreamInFileOut() throws Exception {

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new String("Any data, asdasdj hajshd jahsdkja").getBytes("UTF-8")));
        String resultFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/" + UUID.randomUUID().toString() + ".mp3");
        String dummyLamePath = FilePath.getResourcePath(this.getClass().getClassLoader(), encoderPath, 2);

        assertFalse(new File(resultFilePath).exists(), "Output file should not exist");

        logger.debug("Dummy lame encoder path: " + dummyLamePath);

        // should throw no exceptions
        new MP3LameEncoder()
        .setLameEncoderCmd(dummyLamePath)
        .setInputStream(in)
        .setOutputFile(new File(resultFilePath))
        .process();

        assertTrue(new File(resultFilePath).exists(), "Dummy mp3 should have been created");
        assertEquals(new File(resultFilePath).length(), 21632, "File size should match with the test sample");
    }

    @Test
    public void testStreamInStreamOut() throws Exception {

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new String("Any data, asdasdj hajshd jahsdkja").getBytes("UTF-8")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String dummyLamePath = FilePath.getResourcePath(this.getClass().getClassLoader(), encoderPath, 2);

        assertEquals(out.size(), 0, "Output stream should be empty");

        // should throw no exceptions
        new MP3LameEncoder()
                .setLameEncoderCmd(dummyLamePath)
                .setInputStream(in)
                .setOutputStream(out)
                .process();

        assertEquals(out.size(), 21632, "Output stream should contain dummy sample data");
    }

    @Test
    public void testFileInFileOut() throws Exception {

        String inputFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_synthesizer/samples/sample1.wav", 2);
        String resultFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/" + UUID.randomUUID().toString() + ".mp3");
        String dummyLamePath = FilePath.getResourcePath(this.getClass().getClassLoader(), encoderPath, 2);

        assertFalse(new File(resultFilePath).exists(), "Output file should not exist");

        // should throw no exceptions
        new MP3LameEncoder()
                .setLameEncoderCmd(dummyLamePath)
                .setInputFile(new File(inputFilePath))
                .setOutputFile(new File(resultFilePath))
                .process();

        assertTrue(new File(resultFilePath).exists(), "Dummy mp3 should have been created");
        assertEquals(new File(resultFilePath).length(), 21632, "File size should match with the test sample");
    }

    @Test
    public void testFileInStreamOut() throws Exception {

        String inputFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_synthesizer/samples/sample1.wav", 2);
        String dummyLamePath = FilePath.getResourcePath(this.getClass().getClassLoader(), encoderPath, 2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertEquals(out.size(), 0, "Output stream should be empty");

        // should throw no exceptions
        new MP3LameEncoder()
                .setLameEncoderCmd(dummyLamePath)
                .setInputFile(new File(inputFilePath))
                .setOutputStream(out)
                .process();

        assertEquals(out.size(), 21632, "Output stream should contain dummy sample data");
    }
}