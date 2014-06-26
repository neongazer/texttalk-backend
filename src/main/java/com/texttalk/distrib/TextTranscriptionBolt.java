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

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
    }

    public void execute(Tuple tuple) {

        String textChunk = tuple.getStringByField("textChunk");
        String transcribedText = "";

        try {

            ByteArrayInputStream in = new ByteArrayInputStream(textChunk.getBytes("UTF-8"));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayOutputStream error = new ByteArrayOutputStream();
            // TODO: pass path through configuration or part of the actual tuple
            String dummyTranscribPath = "/Users/Andrew/Documents/projects/text-talk.com/texttalk-backend/vm/vagrant/apps/transcriber1.0/dummy_transcribe";

            System.out.println("Input text" + in.available());

            new PSOLATranscriber()
                    .setCmd(new CommandExecutor().setTimeoutSecs(5).setErrorStream(error))
                    .setPSOLATranscribeCmd(dummyTranscribPath)
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
