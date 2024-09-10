package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
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

public final class ClientSocketBuilder {

    private final NetObjectFactory netObjectFactory;
    private final SocksClient socksClient;
    private final SslSocketFactory sslSocketFactory;

    public ClientSocketBuilder(final SocksClient client) {
        SocksClient chainedClient = client.getChainedSocksClient();
        Properties properties = client.getProperties();
        this.socksClient = client;
        this.netObjectFactory = (chainedClient != null) ?
                chainedClient.newSocksNetObjectFactory()
                : NetObjectFactory.getInstance();
        this.sslSocketFactory =
                (SslSocketFactoryImpl.isSslEnabled(properties)) ?
                        new SslSocketFactoryImpl(properties) : null;
    }

    public Socket newClientSocket() {
        return this.netObjectFactory.newSocket();
    }

    public Socket newConnectedClientSocket(
            final InetAddress localAddr,
            final PortRanges localPortRanges) throws IOException {
        Socket clientSocket = null;
        boolean clientSocketBound = false;
        for (Iterator<PortRange> iterator = localPortRanges.toList().iterator();
             !clientSocketBound && iterator.hasNext();) {
            PortRange lcoalPortRange = iterator.next();
            for (Iterator<Port> iter = lcoalPortRange.iterator();
                 !clientSocketBound && iter.hasNext();) {
                Port localPort = iter.next();
                try {
                    clientSocket = this.newConnectedClientSocket(
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

    public Socket newConnectedClientSocket(
            final InetAddress localAddr,
            final int localPort) throws IOException {
        SocksServerUri socksServerUri = this.socksClient.getSocksServerUri();
        String socksServerUriHost = socksServerUri.getHost().toString();
        int socksServerUriPort = socksServerUri.getPortOrDefault().intValue();
        Socket clientSocket = this.netObjectFactory.newSocket(
                socksServerUriHost,
                socksServerUriPort,
                localAddr,
                localPort);
        if (this.sslSocketFactory == null) {
            return clientSocket;
        }
        clientSocket = this.sslSocketFactory.newSocket(
                clientSocket,
                socksServerUriHost,
                socksServerUriPort,
                true);
        return clientSocket;
    }

    public ConfigureClientSocketBuilder proceedToConfigure(
            final Socket clientSocket) {
        return new ConfigureClientSocketBuilder(
                this.socksClient,
                clientSocket,
                this.netObjectFactory,
                this.sslSocketFactory);
    }

}
