package com.texttalk.core.encoders;

import com.google.common.io.ByteStreams;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MP3LameEncoder extends EncoderBase implements Encoder {

    @Override
    public MP3LameEncoder process() throws IOException {

        if(isInputStreamSet() && isOutputStreamSet()) {
            ByteStreams.copy(inputStream, outputStream);
        }

        if(outputFile != null && !outputFile.exists()) {
            ByteStreams.copy(inputStream, new FileOutputStream(outputFile));
        }

        return this;
    }


}