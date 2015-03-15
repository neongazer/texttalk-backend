package com.texttalk.common.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Andrew on 27/12/2014.
 */
public class CloseableByteArrayOutputStream extends ByteArrayOutputStream {

    private boolean closed = false;

    public void close() throws IOException {
        super.close();
        closed = true;
    }

    public boolean isClosed() {

        return closed;
    }
}
