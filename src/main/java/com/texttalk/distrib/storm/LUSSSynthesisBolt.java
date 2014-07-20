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
import com.texttalk.core.synthesizer.LUSSSynthesizer;
import com.texttalk.core.synthesizer.PSOLASynthesizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

/**
 * Created by Andrew on 20/07/2014.
 */
public class LUSSSynthesisBolt extends BaseRichBolt {

    private static Logger logger = LoggerFactory.getLogger(LUSSSynthesisBolt.class);

    private OutputCollector collector;
    private String synthesizerURL = "";
    private String synthesizerProtocol = "";
    private Integer timeout = 0;
    private Integer bitrate = 0;
    private String voicePath = "";
    private LUSSSynthesizer synthesizer;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
        synthesizerURL = (String)config.get("synthesizers.luss.url");
        synthesizerProtocol = (String)config.get("synthesizers.luss.protocol");
        timeout = ((Long)config.get("synthesizers.luss.timeout")).intValue();
        bitrate = ((Long)config.get("synthesizers.luss.bitrate")).intValue();
        voicePath = (String)config.get("voicePath");
        synthesizer = new LUSSSynthesizer(synthesizerURL, synthesizerProtocol, timeout, bitrate);

    }

    public void execute(Tuple tuple) {

        String textChunk = tuple.getStringByField("textChunk");
        String hashCode = Hashing.md5().hashString(textChunk, Charsets.UTF_8).toString();
        String fileName = hashCode + ".mp3";
        File mp3File = new File(voicePath + "/" + fileName);

        logger.info("Running LUSS Synthesis bolt...");

        try {

            ByteArrayInputStream in = new ByteArrayInputStream(textChunk.getBytes("UTF-8"));

            synthesizer.setInputStream(in).setOutputFile(mp3File).process();

        } catch(Exception e) {
            // TODO: deal with exception
            e.printStackTrace();
        }

        this.collector.emit(new Values(hashCode, mp3File.getAbsoluteFile()));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("hashCode", "voicePath"));
    }
}
