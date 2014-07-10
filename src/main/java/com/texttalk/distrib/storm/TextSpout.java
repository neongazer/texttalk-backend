package com.texttalk.distrib.storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.texttalk.common.Utils;

import java.util.Map;

/**
 * Created by Andrew on 24/06/2014.
 */
public class TextSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;

    private String[] texts = {
            "Žuvies patariama valgyti bent du kartus per savaitę. Dauguma žuvų yra liesos, jos yra sveikas baltymų ir naudingųjų riebalų rūgščių šaltinis.",
            "Į prie Rusijos Pietuose esančio Rostovo prie Dono miesto vykusias traktorių lenktynes sugūžėjo daugiau nei 30 tūkst. žmonių.",
            "Joninių dieną Vilniaus gyventojai miesto centre, prie Kempinski viešbučio, galėjo grožėtis ypatingai ekstravagantiškais automobiliais.",
            "Lietuvos jūrų muziejaus raginimas paskutinį kartą pamatyti senąją, Kopgalio tvirtovėje įrengtą akvariumo ekspoziciją suviliojo tūkstančius šeimų su vaikais, kurie nepabūgo sinoptikų žadamo lietaus su perkūnija."
    };

    private int index = 0;

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("text"));
    }

    public void open(Map config, TopologyContext context, SpoutOutputCollector collector) {

        this.collector = collector;
    }

    public void nextTuple() {
        this.collector.emit(new Values(texts[index]));
        index++;
        if (index >= texts.length) {
            index = 0;
        }
        Utils.waitForMillis(1000);
    }
}
