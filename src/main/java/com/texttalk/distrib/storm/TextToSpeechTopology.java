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
    private static final String PSOLA_SYNTHESIS_BOLT_ID = "psola-synthesis-bolt";
    private static final String LUSS_SYNTHESIS_BOLT_ID = "luss-synthesis-bolt";
    private static final String ENCODER_BOLT_ID = "encoder-bolt";
    private static final String TOPOLOGY_NAME = "text-to-speech-topology";

    public static void main(String[] args) throws Exception {

        RedisQueueSpout queueSpout = new RedisQueueSpout(
                Settings.all.getString("redis.host"),
                Settings.all.getInt("redis.port"),
                Settings.all.getString("redis.password"),
                Settings.all.getString("redis.incomingQueue")
        );
        TextSplitterBolt splitBolt = new TextSplitterBolt();
        TextTranscriptionBolt transcriptionBolt = new TextTranscriptionBolt();
        PSOLASynthesisBolt psolaSynthesisBolt = new PSOLASynthesisBolt();
        LUSSSynthesisBolt lussSynthesisBolt = new LUSSSynthesisBolt();
        MP3EncoderBolt encoderBolt = new MP3EncoderBolt();

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(TEXT_SPOUT_ID, queueSpout);

        builder.setBolt(SPLIT_BOLT_ID, splitBolt, 2).setNumTasks(4).shuffleGrouping(TEXT_SPOUT_ID);

        builder.setBolt(LUSS_SYNTHESIS_BOLT_ID, lussSynthesisBolt, 2).setNumTasks(2).shuffleGrouping(SPLIT_BOLT_ID, "luss");

        builder.setBolt(TRANSCRIPTION_BOLT_ID, transcriptionBolt, 2).setNumTasks(2).shuffleGrouping(SPLIT_BOLT_ID, "psola");
        builder.setBolt(PSOLA_SYNTHESIS_BOLT_ID, psolaSynthesisBolt, 2).setNumTasks(2).shuffleGrouping(TRANSCRIPTION_BOLT_ID, "psola");
        builder.setBolt(ENCODER_BOLT_ID, encoderBolt, 2).setNumTasks(4).shuffleGrouping(PSOLA_SYNTHESIS_BOLT_ID, "psola");

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

        config.put("synthesizers.luss.url", Settings.all.getString("synthesizers.luss.url"));
        config.put("synthesizers.luss.protocol", Settings.all.getString("synthesizers.luss.protocol"));
        config.put("synthesizers.luss.timeout", Settings.all.getInt("synthesizers.luss.timeout"));
        config.put("synthesizers.luss.bitrate", Settings.all.getInt("synthesizers.luss.bitrate"));

        config.put("synthesizers.psola.execPath", Settings.all.getString("synthesizers.psola.execPath"));
        config.put("synthesizers.psola.voiceDbPath", Settings.all.getString("synthesizers.psola.voiceDbPath"));
        config.put("synthesizers.psola.timeout", Settings.all.getInt("synthesizers.psola.timeout"));

        config.put("transcribers.psola.execPath", Settings.all.getString("transcribers.psola.execPath"));
        config.put("transcribers.psola.timeout", Settings.all.getInt("transcribers.psola.timeout"));

        config.put("encoders.lame.execPath", Settings.all.getString("encoders.lame.execPath"));
        config.put("encoders.lame.timeout", Settings.all.getInt("encoders.lame.timeout"));

        config.put("voicePath", Settings.all.getString("voicePath"));

        config.put("redis.host", Settings.all.getString("redis.host"));
        config.put("redis.port", Settings.all.getInt("redis.port"));
        config.put("redis.password", Settings.all.getString("redis.password"));
        config.put("redis.incomingQueue", Settings.all.getString("redis.incomingQueue"));
        config.put("redis.outgoingQueue", Settings.all.getString("redis.outgoingQueue"));

        return config;
    }
}
