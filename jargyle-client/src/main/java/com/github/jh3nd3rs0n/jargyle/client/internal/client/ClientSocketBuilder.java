package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRange;
import com.github.jh3nd3rs0n.jargyle.common.net.PortRanges;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.throwable.ThrowableHelper;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;

/**
 * A builder for the client socket.
 */
public final class ClientSocketBuilder {

    /**
     * The {@code HostResolverFactory} for this {@code ClientSocketBuilder}.
     */
    private final HostResolverFactory hostResolverFactory;

    /**
     * The {@code SocketFactory} for this {@code ClientSocketBuilder}.
     */
    private final SocketFactory socketFactory;

    /**
     * The {@code SocksClientAgent} for this {@code ClientSocketBuilder}.
     */
    private final SocksClientAgent socksClientAgent;

    /**
     * The {@code SslSocketFactory} for this {@code ClientSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * Constructs a {@code ClientSocketBuilder} with the provided
     * {@code SocksClientAgent}, the provided {@code HostResolverFactory}, the
     * provided {@code SocketFactory}, and the provided
     * {@code SslSocketFactory}.
     *
     * @param agent              the provided {@code SocksClientAgent}
     * @param hstResolverFactory the provided {@code HostResolverFactory}
     * @param sockFactory        the provided {@code SocketFactory}
     * @param sslSockFactory     the provided {@code SslSocketFactory}
     */
    ClientSocketBuilder(
            final SocksClientAgent agent,
            final HostResolverFactory hstResolverFactory,
            final SocketFactory sockFactory,
            final SslSocketFactory sslSockFactory) {
        this.hostResolverFactory = hstResolverFactory;
        this.socketFactory = sockFactory;
        this.socksClientAgent = agent;
        this.sslSocketFactory = sslSockFactory;
    }

    /**
     * Returns a new bound and connected client socket based on the properties
     * {@code socksClient.clientBindHost},
     * {@code socksClient.clientBindHostAddressTypes},
     * {@code socksClient.clientBindPortRanges}, and
     * {@code socksClient.clientNetInterface}. If the property
     * {@code socksClient.ssl.enabled} is set to {@code true}, a new bound and
     * connected client {@code SSLSocket} is returned.
     *
     * @return a new bound and connected client socket based on the properties
     * {@code socksClient.clientBindHost},
     * {@code socksClient.clientBindHostAddressTypes},
     * {@code socksClient.clientBindPortRanges}, and
     * {@code socksClient.clientNetInterface}
     * @throws IOException if an I/O error occurs in creating the client socket
     */
    public Socket newBoundConnectedClientSocket() throws IOException {
        Properties properties = this.socksClientAgent.getProperties();
        InetAddress localAddr =
                GeneralValueDerivationHelper.getClientBindHostFrom(
                        properties).toInetAddress();
        PortRanges localPortRanges = properties.getValue(
                GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES);
        Socket clientSocket = null;
        boolean clientSocketBound = false;
        for (Iterator<PortRange> iterator = localPortRanges.toList().iterator();
             !clientSocketBound && iterator.hasNext(); ) {
            PortRange localPortRange = iterator.next();
            for (Iterator<Port> iter = localPortRange.iterator();
                 !clientSocketBound && iter.hasNext(); ) {
                Port localPort = iter.next();
                try {
                    clientSocket = this.newBoundConnectedClientSocket(
                            localAddr,
                            localPort.intValue());
                } catch (IOException e) {
                    if (ThrowableHelper.isOrHasInstanceOf(
                            e, BindException.class)) {
                        continue;
                    }
                    throw e;
                }
                clientSocketBound = true;
            }
        }
        if (!clientSocketBound) {
            throw new BindException(String.format(
                    "unable to bind to the following address and port "
                            + "(range(s)): %s %s",
                    localAddr,
                    localPortRanges));
        }
        return clientSocket;
    }

    /**
     * Returns a new connected client socket bound to the provided local
     * {@code InetAddress} and the provided local port. If the property
     * {@code socksClient.ssl.enabled} is set to {@code true}, a new bound and
     * connected client {@code SSLSocket} is returned.
     *
     * @param localAddr the provided local {@code InetAddress}
     * @param localPort the provided local port
     * @return a new connected client socket bound to the provided local
     * {@code InetAddress} and the provided local port
     * @throws IOException if an I/O error occurs in creating the client socket
     */
    public Socket newBoundConnectedClientSocket(
            final InetAddress localAddr,
            final int localPort) throws IOException {
        SocksServerUri socksServerUri =
                this.socksClientAgent.getSocksServerUri();
        String socksServerUriHost = socksServerUri.getHost().toString();
        int socksServerUriPort =
                socksServerUri.getPortOrDefault().intValue();
        Socket clientSocket = this.socketFactory.newSocket(
                socksServerUriHost,
                socksServerUriPort,
                localAddr,
                localPort);
        return this.sslSocketFactory.getSocket(
                clientSocket,
                socksServerUriHost,
                socksServerUriPort,
                true);
    }

    /**
     * Returns a new unbound and unconnected client socket.
     *
     * @return a new unbound and unconnected client socket
     */
    public Socket newClientSocket() {
        return this.socketFactory.newSocket();
    }

    /**
     * Proceeds to configure the provided client socket by deferring to a
     * returning new {@code ConfiguringClientSocketBuilder} to handle
     * configuring the provided client socket.
     *
     * @param clientSocket the provided client socket
     * @return a new {@code ConfiguringClientSocketBuilder} to handle
     * configuring the provided client socket
     */
    public ConfiguringClientSocketBuilder proceedToConfigure(
            final Socket clientSocket) {
        return new ConfiguringClientSocketBuilder(
                clientSocket,
                this.socksClientAgent,
                this.hostResolverFactory,
                this.socketFactory,
                this.sslSocketFactory);
    }

}
