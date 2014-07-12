package com.texttalk.distrib.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.texttalk.common.Settings;
import com.texttalk.common.Utils;
import com.texttalk.distrib.storm.queue.RedisQueueSpout;

/**
 * Created by Andrew on 24/06/2014.
 */
public class TextToSpeechTopology {

    private static final String TEXT_SPOUT_ID = "text-spout";
    private static final String SPLIT_BOLT_ID = "split-bolt";
    private static final String TRANSCRIPTION_BOLT_ID = "transcription-bolt";
    private static final String SYNTHESIS_BOLT_ID = "synthesis-bolt";
    private static final String ENCODER_BOLT_ID = "encoder-bolt";
    private static final String TOPOLOGY_NAME = "text-to-speech-topology";

    public static void main(String[] args) throws Exception {

        RedisQueueSpout queueSpout = new RedisQueueSpout("redis", 6379, "text-to-speech");
        TextSplitterBolt splitBolt = new TextSplitterBolt();
        TextTranscriptionBolt transcriptionBolt = new TextTranscriptionBolt();
        SpeechSynthesisBolt synthesisBolt = new SpeechSynthesisBolt();
        MP3EncoderBolt encoderBolt = new MP3EncoderBolt();

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(TEXT_SPOUT_ID, queueSpout);

        builder.setBolt(SPLIT_BOLT_ID, splitBolt, 2).setNumTasks(4).noneGrouping(TEXT_SPOUT_ID);

        builder.setBolt(TRANSCRIPTION_BOLT_ID, transcriptionBolt, 2).setNumTasks(2).fieldsGrouping(SPLIT_BOLT_ID, new Fields("textChunk"));

        builder.setBolt(SYNTHESIS_BOLT_ID, synthesisBolt, 2).setNumTasks(2).globalGrouping(TRANSCRIPTION_BOLT_ID);

        builder.setBolt(ENCODER_BOLT_ID, encoderBolt, 2).setNumTasks(4).globalGrouping(SYNTHESIS_BOLT_ID);

        if(args == null || args.length == 0) {

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(TOPOLOGY_NAME, TextToSpeechTopology.getConfigSettings(), builder.createTopology());
            Utils.waitForSeconds(30);
            cluster.killTopology(TOPOLOGY_NAME);
            cluster.shutdown();

        } else {

            StormSubmitter.submitTopology(args[0], TextToSpeechTopology.getConfigSettings(), builder.createTopology());
        }
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

        config.put("voicePath", Settings.all.getString("voicePath"));

        return config;
    }
}
