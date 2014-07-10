package com.texttalk.core.synthesizer;

import com.google.common.io.ByteStreams;
import com.texttalk.common.command.CommandExecutor;
import com.texttalk.common.processor.Processor;
import com.texttalk.common.processor.ProcessorBase;

import java.io.IOException;

/**
 * Created by Andrew on 22/06/2014.
 */
public class PSOLASynthesizer extends ProcessorBase implements Processor {

    private String psolaSynthCmd = "/vagrant/apps/synthesizer1.0/sint_psola";
    private String voiceDBPath = "/vagrant/apps/synthesizer1.0/PSOLADB.DAT";
    private CommandExecutor cmd = new CommandExecutor();

    public String getVoiceDBPath() {
        return voiceDBPath;
    }

    public PSOLASynthesizer setVoiceDBPath(String voiceDBPath) {
        this.voiceDBPath = voiceDBPath;
        return this;
    }

    public String getPSOLASynthCmd() {
        return psolaSynthCmd;
    }

    public PSOLASynthesizer setPSOLASynthCmd(String psolaSynthCmd) {
        this.psolaSynthCmd = psolaSynthCmd;
        return this;
    }

    public CommandExecutor getCmd() {
        return cmd;
    }

    public PSOLASynthesizer setCmd(CommandExecutor cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public PSOLASynthesizer process() throws IOException {

        Long inputSize = 0L;
        String input = "-";
        String output = "-";

        // check if input is provided or throw exception
        ensureInputIsProvided();

        if(isInputFileSet()) {
            input = inputFile.getAbsolutePath();
            inputSize = inputFile.length();
        }

        if(isInputStreamSet()) {
            cmd.setInputStream(inputStream);
            // TODO: Find more efficient way to determine the input size
            inputSize = new Long(ByteStreams.toByteArray(inputStream).length);
        }

        if(isOutputFileSet()) {
            output = outputFile.getAbsolutePath();
        }

        if(isOutputStreamSet()) {
            cmd.setOutputStream(outputStream);
        }

        cmd.execute(
                String.format("%s %s %d %s %s", psolaSynthCmd, voiceDBPath, inputSize, input, output)
        );

        return this;
    }
}
