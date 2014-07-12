package com.texttalk.core.synthesizer;

import com.google.common.io.ByteStreams;
import com.texttalk.common.Utils;
import com.texttalk.common.command.CommandExecutor;
import com.texttalk.common.processor.Processor;
import com.texttalk.common.processor.ProcessorBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Andrew on 22/06/2014.
 */
public class PSOLASynthesizer extends ProcessorBase implements Processor {

    private static Logger logger = LoggerFactory.getLogger(PSOLASynthesizer.class);

    private String psolaSynthCmd = "/vagrant/apps/psola_synthesizer/sint_psola";
    private String voiceDBPath = "/vagrant/apps/psola_synthesizer/PSOLADB.DAT";
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

        logger.info("Running PSOLA Synthesizer...");

        // check if input is provided or throw exception
        ensureInputIsProvided();

        if(isInputFileSet()) {
            input = inputFile.getAbsolutePath();
            inputSize = inputFile.length();
        }

        if(isInputStreamSet()) {
            cmd.setInputStream(inputStream);
            // TODO: Find more efficient way to determine the input size
            //inputSize = new Long(ByteStreams.toByteArray(inputStream).length);
            inputSize = 32768L;
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
