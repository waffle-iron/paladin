package com.paladin.server;

import com.paladin.http.common.HttpRequest;
import com.paladin.http.common.HttpRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * @author cluttered.code@gmail.com
 */
public class HttpSessionHandler implements CompletionHandler<Integer, HttpSession> {

    private static final Logger LOG = LoggerFactory.getLogger(HttpSessionHandler.class);

    @Override
    public void completed(final Integer result, final HttpSession httpSession) {
        SocketAddress clientAddress = null;
        try {
            clientAddress = httpSession.getClientChannel().getRemoteAddress();
        } catch (final IOException ioe) {
            // TODO: Determine action if even possible
        }

        if (result == -1) {
            try {
                httpSession.getClientChannel().close();
                LOG.info("Stopped listening to client {}", clientAddress);
            } catch (final IOException ioe) {
                LOG.error("", ioe);
            }
            return;
        }

        if(httpSession.isReadMode()) {
            // Read
            final ByteBuffer buffer = httpSession.getBuffer();
            buffer.flip();
            final String message = new String(buffer.array(), StandardCharsets.UTF_8);
            LOG.info("Received message from {}\n{}", clientAddress, message);
            final HttpRequest request = HttpRequestBuilder.fromString(message);
            LOG.info("Received Request: {}", request);
            httpSession.isReadMode(false);

            // TODO: Process http message


            // Write
            final String response = "HTTP/1.1 200 OK\r\nConnection: close\r\n";
            final byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            final ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
            buffer.rewind();
            buffer.put(responseBuffer);
            buffer.flip();
            httpSession.getClientChannel().write(buffer, httpSession, this);

            // Close Channel
            try {
                httpSession.getClientChannel().close();
                LOG.info("Closed client channel: {}", clientAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void failed(final Throwable exc, final HttpSession httpSession) {
        LOG.warn("Connection with client broken");
    }
}