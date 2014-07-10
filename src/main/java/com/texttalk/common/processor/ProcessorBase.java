package com.texttalk.common.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Andrew on 22/06/2014.
 */
public class ProcessorBase<T> {

    protected InputStream inputStream = null;
    protected File inputFile = null;
    protected OutputStream outputStream = null;
    protected File outputFile = null;

    public Processor<T> setInputStream(InputStream stream) {

        inputStream = stream;

        return (Processor<T>)this;
    }

    public Processor<T> setInputFile(File file) {

        inputFile = file;

        return (Processor<T>)this;
    }

    public Processor<T> setOutputStream(OutputStream stream) {

        outputStream = stream;

        return (Processor<T>)this;
    }

    public Processor<T> setOutputFile(File file) {

        outputFile = file;

        return (Processor<T>)this;
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

    public void ensureInputIsProvided() throws IOException {

        if(!isInputStreamSet() && !isInputFileSet()) {
            throw new IOException("Input stream or file is not provided");
        }
    }
}
