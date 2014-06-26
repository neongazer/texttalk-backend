package com.texttalk.distrib;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.*;

/**
 * Created by Andrew on 15/06/2014.
 */
public class SpeechSynthesisBolt extends BaseRichBolt {

    private HashMap<String, String> transcriptions = null;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {
        this.transcriptions = new HashMap<String, String>();
    }

    public void execute(Tuple tuple) {
        String textChunk = tuple.getStringByField("textChunk");
        String transcribedText = tuple.getStringByField("transcribedText");
        this.transcriptions.put(textChunk, transcribedText);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // this bolt does not emit anything
    }

    @Override
    public void cleanup() {

        System.out.println("------ TEXT TRANSCRIPTIONS ------");

        for (String key : this.transcriptions.keySet()) {
            System.out.println(key + " : " + this.transcriptions.get(key));
        }

        System.out.println("----------------------------");
    }
}
