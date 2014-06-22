package com.texttalk.core.transcribers;

import com.google.common.io.ByteStreams;
import com.texttalk.commons.command.CommandExecutor;
import com.texttalk.commons.processor.Processor;
import com.texttalk.commons.processor.ProcessorBase;

import java.io.IOException;

/**
 * Created by Andrew on 22/06/2014.
 */
public class PSOLATranscriber extends ProcessorBase implements Processor {

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

        // check if input is provided or throw exception
        ensureInputIsProvided();

        if(isInputFileSet()) {
            input = inputFile.getAbsolutePath();
            inputSize = inputFile.length() * 50; // each diphone consists of mutiple variables, on average ~20, so 50 per char should be safe
        }

        if(isInputStreamSet()) {
            cmd.setInputStream(inputStream);
            // TODO: Find more efficient way to determine the input size
            inputSize = new Long(ByteStreams.toByteArray(inputStream).length) * 50; // each diphone consists of mutiple variables, on average ~20, so 50 per should be safe
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
