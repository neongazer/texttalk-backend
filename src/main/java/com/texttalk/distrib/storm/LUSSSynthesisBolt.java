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
import com.texttalk.common.JedisFactory;
import com.texttalk.common.Utils;
import com.texttalk.common.io.CloseableByteArrayOutputStream;
import com.texttalk.common.model.Message;
import com.texttalk.core.Cache;
import com.texttalk.core.synthesizer.LUSSSynthesizer;
import com.texttalk.distrib.storm.queue.JedisQueue;
import org.apache.commons.codec.binary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;

/**
 * Created by Andrew on 20/07/2014.
 */
public class LUSSSynthesisBolt extends BaseRichBolt {

    private static Logger logger = LoggerFactory.getLogger(LUSSSynthesisBolt.class);

    private int id = 0;
    private OutputCollector collector;
    private String synthesizerURL = "";
    private String synthesizerProtocol = "";
    private Integer timeout = 0;
    private Integer bitrate = 0;
    private String voicePath = "";
    private LUSSSynthesizer synthesizer;
    private String outQueueName = "";
    private Cache cache = null;

    public void prepare(Map config, TopologyContext context, OutputCollector collector) {

        id = new Random().nextInt(1000);
        this.collector = collector;
        synthesizerURL = (String)config.get("synthesizers.luss.url");
        synthesizerProtocol = (String)config.get("synthesizers.luss.protocol");
        timeout = ((Long)config.get("synthesizers.luss.timeout")).intValue();
        bitrate = ((Long)config.get("synthesizers.luss.bitrate")).intValue();
        voicePath = (String)config.get("voicePath");
        synthesizer = new LUSSSynthesizer(synthesizerURL, synthesizerProtocol, timeout, bitrate);
        outQueueName = config.get("redis.outgoingQueue").toString();

        cache = new Cache(config);

    }

    public void execute(Tuple tuple) {

        Message msg = Message.getMessage(tuple.getStringByField("textChunk"));

        String fileName = msg.getHashCode() + ".mp3";
        File mp3File = new File(voicePath + "/" + fileName);

        msg.setVoiceFile(mp3File.getAbsolutePath());

        logger.info("Running LUSS Synthesis bolt on: " + id);

        try {

            ByteArrayInputStream in = new ByteArrayInputStream(msg.getText().getBytes("UTF-8"));
            CloseableByteArrayOutputStream out = new CloseableByteArrayOutputStream();

            synthesizer.setInputStream(in);
            synthesizer.setOutputStream(out);
            synthesizer.process();

            msg.setVoiceTrack(out.toByteArray());

            // Keep speech data inside the redis cache with the key: tts:out:${hashCode}
            cache.storeSpeech(outQueueName + ":" + msg.getHashCode(), msg.getVoiceTrack(), msg.getText());

            // Keep text chunks links as a set. Each text is split into chunks, which are saved with their order.  Key: tts:out:links:${originalTextHashCode}
            cache.linkSpeech(outQueueName + ":links:" + msg.getParentHashCode(), msg.getOrderId() + ":" + msg.getHashCode());

            // Let consumer send back the voice track for playback
            cache.notify(msg.getChannel(), msg);

        } catch(Exception e) {
            // TODO: deal with exception
            e.printStackTrace();
        }

        this.collector.emit(new Values(Message.getJSON(msg)));
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //declarer.declare(new Fields("hashCode", "voicePath"));
    }
}
