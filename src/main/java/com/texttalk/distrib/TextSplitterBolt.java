package com.texttalk.distrib;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.texttalk.core.TextSplitter;

import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 24/06/2014.
 */
public class TextSplitterBolt extends BaseRichBolt {

    private OutputCollector collector;
    // TODO: pass this value via configuration or include in the actual tuple
    private int maxTextLength = 50;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple tuple) {

        List<String> splitText = TextSplitter.splitText( tuple.getStringByField("text"), maxTextLength );

        for(String text : splitText){
            this.collector.emit( new Values(text) );
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("textChunk"));
    }
}
