package com.texttalk.core.transcriber;

import com.google.common.io.ByteStreams;
import com.texttalk.common.Utils;
import com.texttalk.common.command.CommandExecutor;
import com.texttalk.common.processor.Processor;
import com.texttalk.common.processor.ProcessorBase;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Andrew on 22/06/2014.
 */
public class PSOLATranscriber extends ProcessorBase implements Processor {

    private static Logger logger = LoggerFactory.getLogger(PSOLATranscriber.class);

    private String psolaTranscribeCmd = "/vagrant/apps/transcriber1.0/transcribe";
    private int speed = 0;
    private int tone = 0;
    private CommandExecutor cmd = new CommandExecutor();

    public int getSpeed() {
        return speed;
    }

    public PSOLATranscriber setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public int getTone() {
        return tone;
    }

    public PSOLATranscriber setCmd(CommandExecutor cmd) {
        this.cmd = cmd;
        return this;
    }

    public PSOLATranscriber setTone(int tone) {
        this.tone = tone;
        return this;
    }

    public String getPSOLATranscribeCmd() {
        return psolaTranscribeCmd;
    }

    public PSOLATranscriber setPSOLATranscribeCmd(String psolaTranscribeCmd) {
        this.psolaTranscribeCmd = psolaTranscribeCmd;
        return this;
    }

    @Override
    public PSOLATranscriber process() throws IOException {

        Long inputSize = 0L;
        String input = "-";
        String output = "-";

        logger.info("Running PSOLA Transcriber...");

        // check if input is provided or throw exception
        ensureInputIsProvided();

        if(isInputFileSet()) {
            input = inputFile.getAbsolutePath();
            inputSize = inputFile.length() * 50; // each diphone consists of mutiple variables, on average ~20, so 50 per char should be safe
        }

        if(isInputStreamSet()) {

            cmd.setInputStream(inputStream);
            // TODO: Find more efficient way to determine the input size
            //inputSize = new Long(ByteStreams.toByteArray(inputStream).length) * 50; // each diphone consists of mutiple variables, on average ~20, so 50 per should be safe
            inputSize = 32768L;
        }

        if(isOutputFileSet()) {
            output = outputFile.getAbsolutePath();
        }

        if(isOutputStreamSet()) {
            cmd.setOutputStream(outputStream);
        }

        cmd.execute(
                String.format("%s %d %d %d %s %s", psolaTranscribeCmd, inputSize, speed, tone, input, output)
        );

        return this;
    }
}
