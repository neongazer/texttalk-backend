package com.texttalk.core.encoders;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by Andrew on 21/06/2014.
 */
public class EncoderBase<T> {

    protected BufferedInputStream inputStream;
    protected File inputFile;
    protected BufferedOutputStream outputStream;
    protected File outputFile;

    public Encoder<T> setInputStream(BufferedInputStream stream) {

        inputStream = stream;

        return (Encoder<T>)this;
    }

    public Encoder<T> setInputFile(File file) {

        inputFile = file;

        return (Encoder<T>)this;
    }

    public Encoder<T> setOutputStream(BufferedOutputStream stream) {

        outputStream = stream;

        return (Encoder<T>)this;
    }

    public Encoder<T> setOutputFile(File file) {

        outputFile = file;

        return (Encoder<T>)this;
    }

    public boolean isInputStreamSet() throws IOException {

        return (inputStream != null && inputStream.available() > 0);
    }

    public boolean isOutputStreamSet() throws IOException {

        return (outputStream != null);
    }
}
