package com.texttalk.core.synthesizers;

import com.texttalk.commons.io.FilePath;
import com.texttalk.core.encoders.MP3LameEncoder;
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
import static org.testng.Assert.assertEquals;

public class PSOLASynthesizerTest {

    private final static Logger logger = LoggerFactory.getLogger(PSOLASynthesizerTest.class);

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testStreamInFileOut() throws Exception {

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new String("Any data, asdasdj hajshd jahsdkja").getBytes("UTF-8")));
        String resultFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/" + UUID.randomUUID().toString() + ".wav");
        String dummySynthPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/synthesizer1.0/dummy_sint_psola", 2);

        assertFalse(new File(resultFilePath).exists(), "Output file should not exist");

        logger.debug("Dummy synthesizer path: " + dummySynthPath);

        // should throw no exceptions
        new PSOLASynthesizer()
                .setPSOLASynthCmd(dummySynthPath)
                .setInputStream(in)
                .setOutputFile(new File(resultFilePath))
                .process();

        assertTrue(new File(resultFilePath).exists(), "Dummy wave file should have been created");
        assertEquals(new File(resultFilePath).length(), 95436, "File size should match with the test sample");
    }

    @Test
    public void testStreamInStreamOut() throws Exception {

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new String("Any data, asdasdj hajshd jahsdkja").getBytes("UTF-8")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String dummySynthPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/synthesizer1.0/dummy_sint_psola", 2);

        assertEquals(out.size(), 0, "Output stream should be empty");

        // should throw no exceptions
        new PSOLASynthesizer()
                .setPSOLASynthCmd(dummySynthPath)
                .setInputStream(in)
                .setOutputStream(out)
                .process();

        assertEquals(out.size(), 95436, "Output stream should contain dummy sample data");
    }

    @Test
    public void testFileInFileOut() throws Exception {

        String inputFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/transcriber1.0/samples/sample1.pho", 2);
        String resultFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/" + UUID.randomUUID().toString() + ".wav");
        String dummySynthPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/synthesizer1.0/dummy_sint_psola", 2);

        assertFalse(new File(resultFilePath).exists(), "Output file should not exist");

        // should throw no exceptions
        new PSOLASynthesizer()
                .setPSOLASynthCmd(dummySynthPath)
                .setInputFile(new File(inputFilePath))
                .setOutputFile(new File(resultFilePath))
                .process();

        assertTrue(new File(resultFilePath).exists(), "Dummy wav should have been created");
        assertEquals(new File(resultFilePath).length(), 95436, "File size should match with the test sample");
    }

    @Test
    public void testFileInStreamOut() throws Exception {

        String inputFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/transcriber1.0/samples/sample1.pho", 2);
        String dummySynthPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/synthesizer1.0/dummy_sint_psola", 2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertEquals(out.size(), 0, "Output stream should be empty");

        // should throw no exceptions
        new PSOLASynthesizer()
                .setPSOLASynthCmd(dummySynthPath)
                .setInputFile(new File(inputFilePath))
                .setOutputStream(out)
                .process();

        assertEquals(out.size(), 95436, "Output stream should contain dummy sample data");
    }
}