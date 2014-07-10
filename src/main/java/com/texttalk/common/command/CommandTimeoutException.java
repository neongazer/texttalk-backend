package com.texttalk.common.command;

/**
 * Created by Andrew on 16/06/2014.
 */
public class CommandTimeoutException extends RuntimeException {

    public CommandTimeoutException() { super(); }
    public CommandTimeoutException(String message) { super(message); }
    public CommandTimeoutException(String message, Throwable cause) { super(message, cause); }
    public CommandTimeoutException(Throwable cause) { super(cause); }
}
