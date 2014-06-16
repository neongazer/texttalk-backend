package com.texttalk.commons.command;

import java.util.Calendar;
import org.apache.commons.exec.OS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import static org.testng.Assert.assertTrue;

public class CommandExecutorTest {

    private final static Logger logger = LoggerFactory.getLogger(CommandExecutorTest.class);

    private String delayCmd;
    private String returnCmd;
    private String killDelayedCmd;
    private String listProcsCmd;

    @BeforeMethod
    public void setUp() throws Exception {

        if(OS.isFamilyMac()) { // Mac OSX 10.7+
            delayCmd = "/sbin/ping -c 5 127.0.0.1";
            returnCmd = "/sbin/ping -c 1 127.0.0.1";
            killDelayedCmd = "/usr/bin/killall -v ping";
            listProcsCmd = "/bin/ps ax";
        } else if (OS.isFamilyUnix()) { // Debian, Ubuntu
            delayCmd = "/bin/ping 127.0.0.1 -c 5";
            returnCmd = "/bin/ping 127.0.0.1 -c 1";
            killDelayedCmd = "/usr/bin/killall -v ping";
            listProcsCmd = "/bin/ps ax";
        } else if (OS.isFamilyWindows()) { // Windows 7+, Windows Server 2008+
            delayCmd = "C:/Windows/System32/PING.EXE -n 5 127.0.0.1";
            returnCmd = "C:/Windows/System32/PING.EXE -n 1 127.0.0.1";
            //returnCmd = "mshta \"javascript:alert('Bravo. Looks like its working! Wait for timeout to occur...');close();\"";
            killDelayedCmd = "C:/Windows/System32/taskkill.exe /IM PING.EXE /F";
            listProcsCmd = "C:/Windows/System32/tasklist.exe";
        } else {
            throw new RuntimeException("OS is not supported!");
        }

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testCommand() {

        logger.debug("Executing: " + returnCmd);
        String output = new CommandExecutor().execute(returnCmd).toString();
        assertTrue(output.toLowerCase().contains("from 127.0.0.1"), "Should execute ping successfully to ping once localhost");
    }

    @Test(expectedExceptions = CommandTimeoutException.class)
    public void testCommandTimeout() throws CommandTimeoutException {

        logger.debug("Executing: " + delayCmd);
        // Should throw time out exception
        new CommandExecutor().execute(delayCmd, 1);
    }

    @Test
    public void testCommandTimeoutLength() {

        logger.debug("Executing: " + delayCmd);
        // Should timeout after 2 secs
        Calendar startTime = Calendar.getInstance();
        long commandTimeDiff = 0;

        try {
            new CommandExecutor().execute(delayCmd, 2);
        } catch(Exception e) {
            commandTimeDiff = (Calendar.getInstance().getTimeInMillis() - startTime.getTimeInMillis()) / 1000;
        }

        assertTrue(commandTimeDiff >= 2 && commandTimeDiff < 3, "Command should have been running for 2 secs and then timed out");
    }

    @Test
    public void testTimeoutCommandWithKilling() throws CommandException {

        logger.debug("Executing: " + delayCmd);
        // Should throw time out exception and kill command
        try {
            new CommandExecutor().execute(delayCmd, 1, 1, killDelayedCmd);
        } catch(CommandTimeoutException e) {
            // Check if ping is still running
            String output = new CommandExecutor().execute(listProcsCmd).toString();
            assertTrue(!output.toLowerCase().contains("ping"), "Ping should have been killed");
        }
    }

    @Test(expectedExceptions = CommandException.class)
    public void testInvalidCommand() throws CommandException {
        // Should throw CommandException
        new CommandExecutor().execute("blablah");
    }
}