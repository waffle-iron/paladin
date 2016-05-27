package com.paladin.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author cluttered.code@gmail.com
 */
public class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final Logger LOG = LoggerFactory.getLogger(ConnectionHandler.class);

    @Override
    public void completed(final AsynchronousSocketChannel clientChannel, final AsynchronousServerSocketChannel serverChannel) {
        final HttpSession httpSession = new HttpSession(serverChannel, clientChannel);
        serverChannel.accept(serverChannel, this);
        final HttpSessionHandler httpSessionHandler = new HttpSessionHandler();
        clientChannel.read(httpSession.getBuffer(), httpSession, httpSessionHandler);
    }

    @Override
    public void failed(final Throwable throwable, final AsynchronousServerSocketChannel serverSocket) {
        LOG.error("Failed to accept connection", throwable);
    }
}