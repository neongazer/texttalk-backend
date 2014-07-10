package com.texttalk.core.transcriber;

import com.texttalk.common.io.FilePath;
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

public class PSOLATranscriberTest {

    private final static Logger logger = LoggerFactory.getLogger(PSOLATranscriberTest.class);

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testProcess() throws Exception {

    }

    @Test
    public void testStreamInFileOut() throws Exception {

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new String("Testas 1 2 3 Testas").getBytes("UTF-8")));
        String resultFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/" + UUID.randomUUID().toString() + ".pho");
        String dummyTranscribPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_transcriber/dummy_transcribe", 2);

        assertFalse(new File(resultFilePath).exists(), "Output file should not exist");

        logger.debug("Dummy transcriber path: " + dummyTranscribPath);

        // should throw no exceptions
        new PSOLATranscriber()
                .setPSOLATranscribeCmd(dummyTranscribPath)
                .setInputStream(in)
                .setOutputFile(new File(resultFilePath))
                .process();

        assertTrue(new File(resultFilePath).exists(), "Dummy pho file should have been created");
        assertEquals(new File(resultFilePath).length(), 317, "File size should match with the test sample");
    }

    @Test
    public void testStreamInStreamOut() throws Exception {

        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(new String("Testas 1 2 3 Testas").getBytes("UTF-8")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        String dummyTranscribPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_transcriber/dummy_transcribe", 2);

        assertEquals(out.size(), 0, "Output stream should be empty");

        // should throw no exceptions
        new PSOLATranscriber()
                .setPSOLATranscribeCmd(dummyTranscribPath)
                .setInputStream(in)
                .setOutputStream(out)
                .process();

        assertEquals(out.size(), 317, "Output stream should contain dummy sample data");
    }

    @Test
    public void testFileInFileOut() throws Exception {

        String inputFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_transcriber/samples/sample1.txt", 2);
        String resultFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/" + UUID.randomUUID().toString() + ".pho");
        String dummyTranscribPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_transcriber/dummy_transcribe", 2);

        assertFalse(new File(resultFilePath).exists(), "Output file should not exist");

        // should throw no exceptions
        new PSOLATranscriber()
                .setPSOLATranscribeCmd(dummyTranscribPath)
                .setInputFile(new File(inputFilePath))
                .setOutputFile(new File(resultFilePath))
                .process();

        assertTrue(new File(resultFilePath).exists(), "Dummy pho should have been created");
        assertEquals(new File(resultFilePath).length(), 317, "File size should match with the test sample");
    }

    @Test
    public void testFileInStreamOut() throws Exception {

        String inputFilePath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_transcriber/samples/sample1.txt", 2);
        String dummyTranscribPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "vm/vagrant/apps/psola_transcriber/dummy_transcribe", 2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        assertEquals(out.size(), 0, "Output stream should be empty");

        // should throw no exceptions
        new PSOLATranscriber()
                .setPSOLATranscribeCmd(dummyTranscribPath)
                .setInputFile(new File(inputFilePath))
                .setOutputStream(out)
                .process();

        assertEquals(out.size(), 317, "Output stream should contain dummy sample data");
    }
}