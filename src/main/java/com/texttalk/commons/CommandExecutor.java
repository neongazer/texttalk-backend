package com.texttalk.commons;

/**
 * Created by Andrew on 15/06/2014.
 */
public class CommandExecutor {

    private String cmd;
    private String killCmd;
    private int timeout;

    public CommandExecutor(String cmd, String killCmd, int timeout) {
        setCommand(cmd);
        setKillCommand(killCmd);
        setTimeout(timeout);
    }

    public void setCommand(String cmd) {
        this.cmd = cmd;
    }

    public String getCommand() {
        return cmd;
    }

    public void setKillCommand(String killCmd) {
        this.killCmd = killCmd;
    }

    public String getKillCommand() {
        return killCmd;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public String exec() {

        return "";
    }
}
