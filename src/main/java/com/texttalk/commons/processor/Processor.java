package com.texttalk.commons.processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Andrew on 22/06/2014.
 * This is a base interface used by encoders, synthesizors, transcribers or any other stream/file producer/consumer
 */
public interface Processor<T> {

    Processor<T> setInputStream(InputStream stream);
    Processor<T> setInputFile(File file);

    Processor<T> setOutputStream(OutputStream stream);
    Processor<T> setOutputFile(File file);

    boolean isInputStreamSet() throws IOException;
    boolean isOutputStreamSet() throws IOException;

    boolean isInputFileSet() throws IOException;
    boolean isOutputFileSet() throws IOException;

    Processor<T> process() throws IOException;
}
