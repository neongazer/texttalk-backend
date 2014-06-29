package com.texttalk.distrib;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.texttalk.commons.command.CommandExecutor;
import com.texttalk.commons.io.FilePath;
import com.texttalk.core.transcribers.PSOLATranscriber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by Andrew on 16/06/2014.
 */
public class TextTranscriptionBolt extends BaseRichBolt {

    private OutputCollector collector;
    private String transcriberPath = "";
    private Integer timeout = 0;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
        transcriberPath = (String)config.get("transcribers.psola.execPath");
        timeout = ((Long)config.get("transcribers.psola.timeout")).intValue();
    }

    public void execute(Tuple tuple) {

        String textChunk = tuple.getStringByField("textChunk");
        String transcribedText = "";

        try {

            ByteArrayInputStream in = new ByteArrayInputStream(textChunk.getBytes("UTF-8"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream error = new ByteArrayOutputStream();

            System.out.println("Input text" + in.available());

            new PSOLATranscriber()
                    .setCmd(new CommandExecutor().setTimeoutSecs(timeout).setErrorStream(error))
                    .setPSOLATranscribeCmd(transcriberPath)
                    .setInputStream(in)
                    .setOutputStream(out)
                    .process();


            transcribedText = out.toString("UTF-8");

        } catch(Exception e) {
            // TODO: deal with exception
            e.printStackTrace();
        }

        this.collector.emit(new Values(textChunk, transcribedText));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("textChunk", "transcribedText"));
    }
}
