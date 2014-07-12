package com.texttalk.distrib.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.texttalk.common.command.CommandExecutor;
import com.texttalk.core.synthesizer.PSOLASynthesizer;
import com.texttalk.core.transcriber.PSOLATranscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * Created by Andrew on 15/06/2014.
 */
public class SpeechSynthesisBolt extends BaseRichBolt {

    private static Logger logger = LoggerFactory.getLogger(SpeechSynthesisBolt.class);

    private OutputCollector collector;
    private String synthesizerPath = "";
    private String voiceDbPath = "";
    private Integer timeout = 0;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
        synthesizerPath = (String)config.get("synthesizers.psola.execPath");
        voiceDbPath = (String)config.get("synthesizers.psola.voiceDbPath");
        timeout = ((Long)config.get("synthesizers.psola.timeout")).intValue();
    }

    public void execute(Tuple tuple) {
        String textChunk = tuple.getStringByField("textChunk");
        String transcribedText = tuple.getStringByField("transcribedText");


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream error = new ByteArrayOutputStream();

        logger.info("Running Synthesis bolt...");
        logger.info("Input text: " + transcribedText);

        try {

            ByteArrayInputStream in = new ByteArrayInputStream(transcribedText.getBytes("UTF-8"));

            new PSOLASynthesizer()
                    .setCmd(new CommandExecutor().setTimeoutSecs(timeout).setErrorStream(error))
                    .setPSOLASynthCmd(synthesizerPath)
                    .setVoiceDBPath(voiceDbPath)
                    .setInputStream(in)
                    .setOutputStream(out)
                    .process();

        } catch(Exception e) {
            // TODO: deal with exception
            e.printStackTrace();
        }

        this.collector.emit(new Values(textChunk, transcribedText, out.toByteArray()));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("textChunk", "transcribedText", "voice"));
    }


}
