package com.paladin.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

/**
 * @author cluttered.code@gmail.com
 */
public class PaladinServer {

    private static final Logger LOG = LoggerFactory.getLogger(PaladinServer.class);

    public static final String DEFAULT_HOSTNAME = "localhost";
    public static final int DEFAULT_PORT = 9090;

    private final String hostname;
    private final int port;

    public PaladinServer() {
        this(DEFAULT_HOSTNAME, DEFAULT_PORT);
    }

    protected PaladinServer(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void start() {
        AsynchronousServerSocketChannel serverChannel;
        try {
            serverChannel = AsynchronousServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress(hostname, port));
            LOG.info("PaladinServer listening at {}", serverChannel.getLocalAddress());
        } catch (final IOException ioe) {
            LOG.error("Unable to open or bind PaladinServer socket channel");
            throw new RuntimeException(ioe);
        }
        serverChannel.accept(serverChannel, new ConnectionHandler());
        try {
            Thread.currentThread().join();
        } catch (final InterruptedException ie) {
            LOG.info("PaladinServer shutdown");
        }
    }

    public static void main(final String[] args) {
        final PaladinServer paladinServer = new PaladinServer();
        paladinServer.start();
    }
}