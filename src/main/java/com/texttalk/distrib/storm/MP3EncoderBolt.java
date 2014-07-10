package com.texttalk.distrib.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.texttalk.common.command.CommandExecutor;
import com.texttalk.core.encoder.MP3LameEncoder;
import com.texttalk.core.transcriber.PSOLATranscriber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;

/**
 * Created by Andrew on 10/07/2014.
 */
public class MP3EncoderBolt extends BaseRichBolt {

    private OutputCollector collector;
    private String encoderPath = "";
    private String voicePath = "";
    private Integer timeout = 0;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
        encoderPath = (String)config.get("encoders.lame.execPath");
        timeout = ((Long)config.get("encoders.lame.timeout")).intValue();
        voicePath = (String)config.get("voicePath");
    }

    public void execute(Tuple tuple) {

        byte[] voice = tuple.getBinaryByField("voice");
        String textChunk = tuple.getStringByField("textChunk");
        String fileName = Hashing.md5().hashString(textChunk, Charsets.UTF_8).toString() + ".mp3";
        File mp3File = new File(voicePath + "/" + fileName);

        try {

            ByteArrayInputStream in = new ByteArrayInputStream(voice);
            ByteArrayOutputStream error = new ByteArrayOutputStream();

            new MP3LameEncoder()
                    .setCmd(new CommandExecutor().setTimeoutSecs(timeout).setErrorStream(error))
                    .setLameEncoderCmd(encoderPath)
                    .setInputStream(in)
                    .setOutputFile(mp3File)
                    .process();

        } catch(Exception e) {
            // TODO: deal with exception
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
