package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.net.*;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.GeneralValueDerivationHelper;
import com.github.jh3nd3rs0n.jargyle.server.internal.server.Listener;

import java.io.IOException;
import java.net.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class SocksServer {

    public static final int DEFAULT_PORT_INT_VALUE = 1080;
    public static final Port DEFAULT_PORT = Port.valueOf(
            DEFAULT_PORT_INT_VALUE);

    private final Configuration configuration;
    private ServerSocket clientFacingServerSocket;
    private ExecutorService executor;
    private Host host;
    private Port port;
    private State state;

    public SocksServer(final Configuration config) {
		this.configuration = config;
        this.clientFacingServerSocket = null;
        this.executor = null;
        this.host = null;
        this.port = null;
        this.state = State.STOPPED;
    }

    private int getBacklog() {
        return this.configuration.getSettings().getLastValue(
                GeneralSettingSpecConstants.BACKLOG).intValue();
    }

    private Host getClientFacingServerSocketBindHost() {
        return GeneralValueDerivationHelper.getClientFacingServerSocketBindHostFrom(
                this.configuration.getSettings());
    }

    private PortRanges getClientFacingServerSocketBindPortRanges() {
        return GeneralValueDerivationHelper.getClientFacingServerSocketBindPortRangesFrom(
                this.configuration.getSettings());
    }

    private SocketSettings getClientFacingServerSocketSocketSettings() {
        return GeneralValueDerivationHelper.getClientFacingServerSocketSocketSettingsFrom(
                this.configuration.getSettings());
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public Host getHost() {
        return this.host;
    }

    public Port getPort() {
        return this.port;
    }

    public State getState() {
        return this.state;
    }

    private ServerSocket newClientFacingServerSocket(
            final InetAddress bindInetAddress,
            final PortRanges bindPortRanges,
            final SocketSettings socketSettings,
            final int backlog) throws IOException {
        ServerSocket clientFacingServerSock = null;
        boolean clientFacingServerSockBound = false;
        for (Iterator<PortRange> iterator = bindPortRanges.toList().iterator();
             !clientFacingServerSockBound && iterator.hasNext(); ) {
            PortRange bindPortRange = iterator.next();
            for (Iterator<Port> iter = bindPortRange.iterator();
                 !clientFacingServerSockBound && iter.hasNext(); ) {
                Port bindPort = iter.next();
                clientFacingServerSock = new ServerSocket();
                try {
                    socketSettings.applyTo(clientFacingServerSock);
                } catch (UnsupportedOperationException e) {
                    clientFacingServerSock.close();
                    throw e;
                } catch (SocketException e) {
                    clientFacingServerSock.close();
                    throw e;
                }
                try {
                    clientFacingServerSock.bind(
                            new InetSocketAddress(
                                    bindInetAddress,
                                    bindPort.intValue()),
                            backlog);
                } catch (BindException e) {
                    clientFacingServerSock.close();
                    continue;
                } catch (IOException e) {
                    clientFacingServerSock.close();
                    throw e;
                }
                clientFacingServerSockBound = true;
            }
        }
        if (!clientFacingServerSockBound) {
            throw new BindException(String.format(
                    "unable to bind following address and port (range(s)): "
                            + "%s %s",
                    bindInetAddress,
                    bindPortRanges));
        }
        return clientFacingServerSock;
    }

    public void start() throws IOException {
        if (this.state.equals(State.STARTED)) {
            throw new IllegalStateException("SocksServer already started");
        }
        Host bindHost = this.getClientFacingServerSocketBindHost();
        PortRanges bindPortRanges = this.getClientFacingServerSocketBindPortRanges();
        SocketSettings socketSettings = this.getClientFacingServerSocketSocketSettings();
        int backlog = this.getBacklog();
        this.clientFacingServerSocket = newClientFacingServerSocket(
                bindHost.toInetAddress(),
                bindPortRanges,
                socketSettings,
                backlog);
        this.host = bindHost;
        this.port = Port.valueOf(this.clientFacingServerSocket.getLocalPort());
        this.executor = Executors.newSingleThreadExecutor();
        this.executor.execute(new Listener(
                this.clientFacingServerSocket, this.configuration));
        this.state = State.STARTED;
    }

    public void stop() throws IOException {
        if (this.state.equals(State.STOPPED)) {
            throw new IllegalStateException("SocksServer already stopped");
        }
        this.host = null;
        this.port = null;
        this.clientFacingServerSocket.close();
        this.clientFacingServerSocket = null;
        this.executor.shutdownNow();
        this.executor = null;
        this.state = State.STOPPED;
    }

    public static enum State {

        STARTED,

        STOPPED

    }

}
