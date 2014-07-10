package com.texttalk.core.encoder;

import com.texttalk.common.command.CommandExecutor;
import com.texttalk.common.processor.Processor;
import com.texttalk.common.processor.ProcessorBase;

import java.io.IOException;

public class MP3LameEncoder extends ProcessorBase implements Processor {

    private String lameEncoderCmd = "lame -r -m m --resample 22 -b 32";
    private CommandExecutor cmd = new CommandExecutor();

    public CommandExecutor getCmd() {
        return cmd;
    }

    public MP3LameEncoder setCmd(CommandExecutor cmd) {
        this.cmd = cmd;
        return this;
    }

    public MP3LameEncoder setLameEncoderCmd(String cmd) {
        lameEncoderCmd = cmd;
        return this;
    }

    public String getLameEncoderCmd() {
        return lameEncoderCmd;
    }

    @Override
    public MP3LameEncoder process() throws IOException {

        String input = "-";
        String output = "-";

        // check if input is provided or throw exception
        ensureInputIsProvided();

        if(isInputFileSet()) {
            input = inputFile.getAbsolutePath();
        }

        if(isOutputFileSet()) {
            output = outputFile.getAbsolutePath();
        }

        if(isInputStreamSet()) {
            cmd.setInputStream(inputStream);
        }

        if(isOutputStreamSet()) {
            cmd.setOutputStream(outputStream);
        }

        cmd.execute(lameEncoderCmd + " " + input + " " + output);

        return this;
    }
}