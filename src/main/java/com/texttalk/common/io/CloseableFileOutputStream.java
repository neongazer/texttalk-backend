package com.texttalk.common.io;

import java.io.*;

/**
 * Created by Andrew on 19/07/2014.
 */
public class CloseableFileOutputStream extends FileOutputStream {

    private boolean closed = false;

    public CloseableFileOutputStream(String name) throws FileNotFoundException {
        super(name);
    }

    public CloseableFileOutputStream(String name, boolean append) throws FileNotFoundException {
        super(name, append);
    }

    public CloseableFileOutputStream(File file) throws FileNotFoundException {
        super(file);
    }

    public CloseableFileOutputStream(File file, boolean append) throws FileNotFoundException {
        super(file, append);
    }

    public CloseableFileOutputStream(FileDescriptor fdObj) {
        super(fdObj);
    }

    public void close() throws IOException {
        super.close();
        closed = true;
    }

    public boolean isClosed() {

        return closed;
    }
}
