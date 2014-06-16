package com.texttalk.core;

/**
 * Created by Andrew on 15/06/2014.
 */
public class SpeechSynthesizer {

    private String cmd;
    private String killCmd;
    private int exitCode;
    private int timeout;

    public SpeechSynthesizer(String cmd, String killCmd, int exitCode, int timeout) {

    }

    public String synthesize(String text) {

        return "sample.wav";
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getKillCmd() {
        return killCmd;
    }

    public void setKillCmd(String killCmd) {
        this.killCmd = killCmd;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
