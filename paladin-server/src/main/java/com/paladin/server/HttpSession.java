package com.paladin.server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * @author cluttered.code@gmail.com
 */
public class HttpSession {

    private final AsynchronousServerSocketChannel serverChannel;
    private final AsynchronousSocketChannel clientChannel;
    private final ByteBuffer buffer;

    private boolean isReadMode;

    public HttpSession(final AsynchronousServerSocketChannel serverChannel,
                       final AsynchronousSocketChannel clientChannel) {
        this.serverChannel = serverChannel;
        this.clientChannel = clientChannel;
        buffer = ByteBuffer.allocate(2048);
        isReadMode = true;
    }

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public AsynchronousSocketChannel getClientChannel() {
        return clientChannel;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public boolean isReadMode() {
        return isReadMode;
    }

    public void isReadMode(final boolean isReadMode) {
        this.isReadMode = isReadMode;
    }
}