package com.texttalk.core.encoders;

import java.io.*;

/**
 * Created by Andrew on 21/06/2014.
 */
interface Encoder<T> {

    Encoder<T> setInputStream(BufferedInputStream stream);
    Encoder<T> setInputFile(File file);

    Encoder<T> setOutputStream(BufferedOutputStream stream);
    Encoder<T> setOutputFile(File file);

    boolean isInputStreamSet() throws IOException;
    boolean isOutputStreamSet() throws IOException;

    Encoder<T> process() throws IOException;

}
