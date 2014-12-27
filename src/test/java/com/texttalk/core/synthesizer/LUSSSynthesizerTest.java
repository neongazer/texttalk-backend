package com.texttalk.core.synthesizer;

import com.texttalk.common.io.FilePath;
import com.texttalk.common.player.MP3;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.UUID;

public class LUSSSynthesizerTest {

    private LUSSSynthesizer synthesizer = new LUSSSynthesizer("ws://xeon.host.tele1.co:59000", "dumb-increment-protocol", 5, 32);

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @AfterTest
    public void destroy() throws Exception {
        synthesizer.close();
    }

    @Test
    public void testProcess() throws Exception {

        String resultTempPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/");
        File outputFile = new File(resultTempPath + "/" + UUID.randomUUID().toString() + ".mp3");
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(
                new String(
                        "Mano vardas Regina"
                ).getBytes("UTF-8")
        ));

        synthesizer.setInputStream(in).setOutputFile(outputFile).process();

        Assert.assertEquals(outputFile.length(), 5016);

        new MP3(outputFile).play();
    }

    @Test
    public void testProcess1() throws Exception {

        String resultTempPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/");
        File outputFile = new File(resultTempPath + "/" + UUID.randomUUID().toString() + ".mp3");
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(
                new String(
                        "Žuvies patariama valgyti bent du kartus per savaitę."
                ).getBytes("UTF-8")
        ));

        synthesizer.setInputStream(in).setOutputFile(outputFile).process();

        Assert.assertEquals(outputFile.length(), 12330);

        new MP3(outputFile).play();
    }

    @Test
    public void testProcess2() throws Exception {

        String resultTempPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/");
        File outputFile = new File(resultTempPath + "/" + UUID.randomUUID().toString() + ".mp3");
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(
                new String(
                        "vienas vienas karvės pienas"
                ).getBytes("UTF-8")
        ));

        synthesizer.setInputStream(in).setOutputFile(outputFile).process();

        Assert.assertEquals(outputFile.length(), 7523);

        new MP3(outputFile).play();
    }

    @Test
    public void testProcess3() throws Exception {

        String resultTempPath = FilePath.getResourcePath(this.getClass().getClassLoader(), "temp/");
        File outputFile = new File(resultTempPath + "/" + UUID.randomUUID().toString() + ".mp3");
        BufferedInputStream in = new BufferedInputStream(new ByteArrayInputStream(
                new String(
                        "viso gero"
                ).getBytes("UTF-8")
        ));

        synthesizer.setInputStream(in).setOutputFile(outputFile).process();

        Assert.assertEquals(outputFile.length(), 3657);

        new MP3(outputFile).play();
    }
}