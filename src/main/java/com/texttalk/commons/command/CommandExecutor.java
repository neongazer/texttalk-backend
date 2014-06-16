package com.texttalk.commons.command;

import org.apache.commons.exec.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by Andrew on 15/06/2014.
 */
public class CommandExecutor {

    private final static Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    private final int defaultTimeoutSecs = 10;

    public ByteArrayOutputStream execute(String executeMainCommandIn) throws CommandException {

        return execute(executeMainCommandIn, defaultTimeoutSecs, null, null);
    }

    public ByteArrayOutputStream execute(String executeMainCommandIn, Integer timeout) throws CommandException {

        return execute(executeMainCommandIn, timeout, null, null);
    }

    public ByteArrayOutputStream execute(String executeMainCommandIn, Integer timeout, Integer expectedExitValue) throws CommandException {

        return execute(executeMainCommandIn, timeout, expectedExitValue, null);
    }

    public ByteArrayOutputStream execute(String executeMainCommandIn, Integer timeout, Integer expectedExitValue, String executeAfterExceptionMainCommand) throws CommandException {

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ExecuteWatchdog watchdog = null;

        try {
            CommandLine commandLine = CommandLine.parse(executeMainCommandIn);
            DefaultExecutor executor = new DefaultExecutor();

            if(expectedExitValue != null) {
                executor.setExitValue(expectedExitValue);
            }

            // set process output
            stdout = new ByteArrayOutputStream();
            PumpStreamHandler psh = new PumpStreamHandler(stdout);
            executor.setStreamHandler(psh);

            watchdog = new CommandWatchdog(timeout, executeAfterExceptionMainCommand);
            executor.setWatchdog(watchdog);
            executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());

            Calendar startTime = Calendar.getInstance();

            executor.execute(commandLine);

            Calendar nowTime = Calendar.getInstance();
            Long commandTimeDiff = (nowTime.getTimeInMillis() - startTime.getTimeInMillis()) / 1000;

            logger.debug("It took {} seconds to perform the command", commandTimeDiff.toString());

        } catch (CommandException e) {
            logger.error("Command: " + executeMainCommandIn);
            logger.error("Command output: " + stdout);
            throw e;
        } catch (CommandTimeoutException e) {
            logger.error("Command: " + executeMainCommandIn);
            logger.error("Command output: " + stdout);
            throw e;
        } catch (Exception e) {
            logger.error("Command: " + executeMainCommandIn);
            logger.error("Command output: " + stdout);
            // check if the cause of this exception was because of time out or watchdog killed the actual command
            if(watchdog != null) {
                try {
                    watchdog.checkException();
                } catch(Exception wExp) {
                    throw new CommandTimeoutException(e);
                }
            }
            throw new CommandException(e);
        }

        return stdout;
    }
}