package com.texttalk.common.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.texttalk.common.Utils;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Andrew on 20/07/2014.
 */
public class Message {

    private String id = "";
    private String channel = "";
    private int orderId = 0;
    private String hashCode = "";
    private String parentHashCode = "";
    private String text = "";
    private String transcript = "";
    private String voiceFile = "";
    private String synth = "";
    private String voice = "";
    private String tone = "0";
    private String speed = "0";
    private Integer totalChunks = 0;
    private String voiceTrack = "";

    public void setVoiceTrack(String v) {
        voiceTrack = v;
    }

    public void setVoiceTrack(byte[] bytes) {
        voiceTrack = org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

    public String getVoiceTrack() {
        return voiceTrack;
    }

    public void setTotalChunks(int total) {
        totalChunks = total;
    }

    public Integer getTotalChunks() {
        return totalChunks;
    }

    public String computeHashCode() {

        return Utils.getMD5(text + synth + voice + tone + speed);
    }

    public String computeParentHashCode(String allText) {

        return Utils.getMD5(allText + synth + voice + tone + speed);
    }

    public static Message getMessage(String json) {
        try {
            return new ObjectMapper().readValue(json, Message.class);
        } catch (IOException e) {
            return new Message();
        }
    }

    public static String getJSON(Message msg) {
        try {
            StringWriter json = new StringWriter();
            new ObjectMapper().writeValue(json, msg);
            return json.toString();
        } catch(IOException e) {
            return "";
        }
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }

    public void setParentHashCode(String parentHashCode) {
        this.parentHashCode = parentHashCode;
    }

    public String getParentHashCode() {
        return parentHashCode;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSynth() {
        return synth;
    }

    public void setSynth(String synth) {
        this.synth = synth;
    }

    public String getTone() {
        return tone;
    }

    public void setTone(String tone) {
        this.tone = tone;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getVoiceFile() {
        return voiceFile;
    }

    public void setVoiceFile(String voiceFile) {
        this.voiceFile = voiceFile;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }
}
