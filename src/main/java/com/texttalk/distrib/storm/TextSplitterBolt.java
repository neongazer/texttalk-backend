package com.texttalk.distrib.storm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.texttalk.common.Utils;
import com.texttalk.common.model.Message;
import com.texttalk.core.TextSplitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 24/06/2014.
 */
public class TextSplitterBolt extends BaseRichBolt {

    private static Logger logger = LoggerFactory.getLogger(TextSplitterBolt.class);

    private OutputCollector collector;
    // TODO: pass this value via configuration or include in the actual tuple
    private int maxTextLength = 50;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {

        this.collector = collector;
        maxTextLength = ((Long)config.get("splitTextLength")).intValue();
    }

    public void execute(Tuple tuple) {

        Message msg = Message.getMessage(tuple.getStringByField("message"));

        List<String> splitText = TextSplitter.splitText( msg.getText(), maxTextLength );
        int orderId = 0;

        for(String text : splitText){

            Message newMsg = Message.getMessage(tuple.getStringByField("message"));
            newMsg.setText(text);
            newMsg.setParentHashCode(newMsg.computeParentHashCode(msg.getText()));
            newMsg.setHashCode(newMsg.computeHashCode());
            newMsg.setOrderId(orderId);

            orderId++;

            this.collector.emit( newMsg.getSynth(), new Values(Message.getJSON(newMsg)) );
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declareStream("psola", new Fields("textChunk"));
        declarer.declareStream("luss", new Fields("textChunk"));
    }
}
