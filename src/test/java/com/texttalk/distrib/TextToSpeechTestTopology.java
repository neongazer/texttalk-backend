package com.texttalk.distrib;

import com.texttalk.distrib.storm.TextToSpeechTopology;
import org.testng.annotations.Test;

public class TextToSpeechTestTopology {

    @Test(enabled = true)
    public void testMain() throws Exception {

        TextToSpeechTopology.main(null);
    }
}