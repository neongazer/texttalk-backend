package com.texttalk.core;

import com.texttalk.common.JedisFactory;
import com.texttalk.common.model.Message;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Created by andrew on 08/03/15.
 * Use redis cache for storing and publishing  data
 */
public class Cache {

    private static Logger logger = LoggerFactory.getLogger(Cache.class);

    private Jedis jedis;

    public Cache(Map config) {

        JedisFactory.setConnectionSettings(
                config.get("redis.host").toString(),
                Integer.parseInt(config.get("redis.port").toString()),
                config.get("redis.password").toString()
        );

        jedis = JedisFactory.getRes();

    }

    public void storeSpeech(String key, String base64VoiceTrack, String txt) {

        jedis.set(
                key,
                base64VoiceTrack
        );

        jedis.set(
                key + ":txt",
                txt
        );

        logger.info("------> storing speech: " + key);
    }

    public void linkSpeech(String key, String value) {
        jedis.sadd(key, value);

        logger.info("------> linking speech: " + key);
    }

    public void notify(String channel, Message msg) {

        JSONObject json = new JSONObject();

        json.put("action", "synthNotify");
        json.put("id", msg.getId());
        json.put("parentHashCode", msg.getParentHashCode());
        json.put("hashCode", msg.getHashCode());
        json.put("orderId", msg.getOrderId());
        json.put("totalChunks", msg.getTotalChunks());
        json.put("voiceTrack", msg.getVoiceTrack());

        jedis.publish(channel, json.toJSONString());

        logger.info("------> publishing speech: " + channel + " <- " + json.toJSONString());
    }
}
