package com.texttalk.core.synthesizer;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.websocket.*;
import com.texttalk.common.Utils;
import com.texttalk.common.io.CloseAwareFileOutputStream;
import com.texttalk.common.io.FilePath;
import com.texttalk.common.processor.Processor;
import com.texttalk.common.processor.ProcessorBase;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew on 19/07/2014.
 */
public class LUSSSynthesizer extends ProcessorBase implements Processor {

    private static Logger logger = LoggerFactory.getLogger(LUSSSynthesizer.class);

    private AsyncHttpClient client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().build());
    private WebSocket websocket;
    final private String url;
    final private String protocol;
    final private long timeout;
    private int mp3BufferSize = 0;
    private CloseAwareFileOutputStream fileOutputStream;

    public LUSSSynthesizer(String url, String protocol, long timeout, int kbps) {

        this.url = url;
        this.protocol = protocol;
        this.timeout = timeout * 1000;
        this.mp3BufferSize = kbps * 1024 / 8;
    }

    @Override
    public LUSSSynthesizer setOutputFile(File file) {

        try {
            super.setOutputFile(file);
            fileOutputStream = new CloseAwareFileOutputStream(file);
            websocket = getWebsocket();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }

        return this;
    }

    //TODO: This synthesizer currently only supports byte input stream and file output stream
    @Override
    public Processor process() throws IOException {

        logger.info("Running LUSS Synthesizer...");

        // check if input is provided or throw exception
        ensureInputIsProvided();

        getWebsocket().sendMessage(IOUtils.toByteArray(inputStream));

        int fileCheckCounts = 0;

        logger.debug("Waiting for file to be created...");

        //TODO: find a better way to retrieve output
        //Check until enough data has been written to the file: outputFile.length() > bufferSize
        while(fileCheckCounts < timeout) {
            if(fileOutputStream.isClosed()) break;
            fileCheckCounts += 50;
            Utils.waitForMillis(50);
        }

        logger.debug("File created...");

        return this;
    }

    public WebSocket getWebsocket() {

        if(client == null || client.isClosed()) {
            client = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().build());
            websocket = createWebsocket();
        }

        if(websocket == null || !websocket.isOpen()) {
            websocket = createWebsocket();
        }

        return websocket;
    }

    public WebSocket createWebsocket() {

        WebSocket websocket = null;

        try {
            websocket = client.prepareGet(url).setHeader("Sec-WebSocket-Protocol", protocol)
                    .execute(
                            new WebSocketUpgradeHandler
                                    .Builder()
                                    .addWebSocketListener(
                                            new WebSocketByteListener() {

                                                @Override
                                                public void onMessage(byte[] message) {

                                                    logger.debug("Received message from: " + url);

                                                    try {
                                                        fileOutputStream.write(message);
                                                        fileOutputStream.close();
                                                    } catch (Exception e) {
                                                        logger.error("Error while writing data stream to a file from: " + url);
                                                    }
                                                }

                                                @Override
                                                public void onFragment(byte[] s, boolean b) {

                                                    logger.debug("Received fragment " + s.toString() + "; at: " + url);
                                                }

                                                @Override
                                                public void onOpen(WebSocket websocket) {
                                                    logger.debug("Opened websocket connection at: " + url);
                                                }

                                                @Override
                                                public void onClose(WebSocket websocket) {

                                                    logger.debug("Closed websocket connection at: " + url);
                                                }

                                                @Override
                                                public void onError(Throwable t) {
                                                    logger.error("Websocket error at: " + url);
                                                    logger.error(t.toString());
                                                }
                                            }
                                    ).build()
                    ).get(timeout, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            logger.error("Couldn't connect to: " + url);
            logger.error("Could not return WebSocket object for LUSSSynthesizer: " + e.getMessage());
        }

        return websocket;
    }

    public void close() {

        byte[] closeMessage = {(byte)0xFF, (byte)0x00};

        websocket.sendMessage(closeMessage);
        websocket.close();
        client.close();
    }
}
