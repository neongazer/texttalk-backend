package com.texttalk.core.encoders;

import java.io.*;

/**
 * Created by Andrew on 21/06/2014.
 */
public class EncoderBase<T> {

    protected InputStream inputStream = null;
    protected File inputFile = null;
    protected OutputStream outputStream = null;
    protected File outputFile = null;

    public Encoder<T> setInputStream(InputStream stream) {

        inputStream = stream;

        return (Encoder<T>)this;
    }

    public Encoder<T> setInputFile(File file) {

        inputFile = file;

        return (Encoder<T>)this;
    }

    public Encoder<T> setOutputStream(OutputStream stream) {

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

    public boolean isInputFileSet() throws IOException {

        return (inputFile != null);
    }

    public boolean isOutputFileSet() throws IOException {

        return (outputFile != null);
    }
}
