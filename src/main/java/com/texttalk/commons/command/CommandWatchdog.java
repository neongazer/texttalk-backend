package com.texttalk.commons.command;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Watchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Andrew on 16/06/2014.
 */
public class CommandWatchdog extends ExecuteWatchdog {

    private static Logger logger = LoggerFactory.getLogger(CommandWatchdog.class);

    private Exception caught = null;
    private String executeAfterExceptionMainCommand = "";

    public CommandWatchdog(long timeout) {
        super(1000L * timeout);
    }

    public CommandWatchdog(long timeout, String executeAfterExceptionMainCommand) {
        super(1000L * timeout);
        this.executeAfterExceptionMainCommand = executeAfterExceptionMainCommand;
    }

    @Override
    public void checkException() throws Exception {
        if (caught != null) {
            throw caught;
        }
    }

    @Override
    public synchronized void timeoutOccured(Watchdog w) {

        super.timeoutOccured(w);
        logger.trace("Watchdog, timeout occured!");

        if (executeAfterExceptionMainCommand != null && executeAfterExceptionMainCommand.length() > 0) {
            try {
                CommandLine commandLineAfterException = CommandLine.parse(executeAfterExceptionMainCommand);
                DefaultExecutor executorAfterException = new DefaultExecutor();
                logger.trace("Executing killing command (executeAfterExceptionMainCommand): " + executeAfterExceptionMainCommand);
                executorAfterException.execute(commandLineAfterException);
            } catch (Exception exAfter) {
                String msgTmp = "Error executing killing command: " + exAfter.getMessage();
                logger.trace(msgTmp);
                caught = new CommandTimeoutException(msgTmp);
            }
        } else {
            logger.debug("No killing batch/command provided! Will try to exit but may hang...");
        }

        if (caught == null) {
            caught = new CommandTimeoutException("Command line timeout exception: " + executeAfterExceptionMainCommand);
        }
        logger.trace("Caught: " + caught.toString());
    }
}
