package com.texttalk.distrib;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.texttalk.commons.Settings;
import com.texttalk.commons.Utils;

/**
 * Created by Andrew on 24/06/2014.
 */
public class TextToSpeechTopology {

    private static final String TEXT_SPOUT_ID = "text-spout";
    private static final String SPLIT_BOLT_ID = "split-bolt";
    private static final String TRANSCRIPTION_BOLT_ID = "transcription-bolt";
    private static final String SYNTHESIS_BOLT_ID = "synthesis-bolt";
    private static final String TOPOLOGY_NAME = "text-to-speech-topology";

    public static void main(String[] args) throws Exception {

        TextSpout spout = new TextSpout();
        TextSplitterBolt splitBolt = new TextSplitterBolt();
        TextTranscriptionBolt transcriptionBolt = new TextTranscriptionBolt();
        SpeechSynthesisBolt synthesisBolt = new SpeechSynthesisBolt();


        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(TEXT_SPOUT_ID, spout);

        builder.setBolt(SPLIT_BOLT_ID, splitBolt).shuffleGrouping(TEXT_SPOUT_ID);

        builder.setBolt(TRANSCRIPTION_BOLT_ID, transcriptionBolt).fieldsGrouping(SPLIT_BOLT_ID, new Fields("textChunk"));

        builder.setBolt(SYNTHESIS_BOLT_ID, synthesisBolt).globalGrouping(TRANSCRIPTION_BOLT_ID);

        LocalCluster cluster = new LocalCluster();

        cluster.submitTopology(TOPOLOGY_NAME, TextToSpeechTopology.getConfigSettings(), builder.createTopology());
        Utils.waitForSeconds(10);
        cluster.killTopology(TOPOLOGY_NAME);
        cluster.shutdown();
    }

    public static Config getConfigSettings() {

        Config config = new Config();

        config.put("splitTextLength", Settings.all.getInt("splitTextLength"));

        config.put("synthesizers.psola.execPath", Settings.all.getString("synthesizers.psola.execPath"));
        config.put("synthesizers.psola.voiceDbPath", Settings.all.getString("synthesizers.psola.voiceDbPath"));
        config.put("synthesizers.psola.timeout", Settings.all.getInt("synthesizers.psola.timeout"));

        config.put("transcribers.psola.execPath", Settings.all.getString("transcribers.psola.execPath"));
        config.put("transcribers.psola.timeout", Settings.all.getInt("transcribers.psola.timeout"));

        config.put("encoders.lame.execPath", Settings.all.getString("encoders.lame.execPath"));
        config.put("encoders.lame.timeout", Settings.all.getInt("encoders.lame.timeout"));

        return config;
    }
}
