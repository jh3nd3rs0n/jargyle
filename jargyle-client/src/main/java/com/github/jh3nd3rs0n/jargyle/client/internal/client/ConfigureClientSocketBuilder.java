package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.common.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;

import java.net.Socket;
import java.net.SocketException;
import java.util.Objects;

public final class ConfigureClientSocketBuilder {

    private final Socket clientSocket;
    private final NetObjectFactory netObjectFactory;
    private SocketSettings socketSettings;
    private final SocksClient socksClient;
    private final SslSocketFactory sslSocketFactory;

    ConfigureClientSocketBuilder(
            final SocksClient client,
            final Socket clientSock,
            final NetObjectFactory netObjFactory,
            final SslSocketFactory sslSockFactory) {
        this.clientSocket = clientSock;
        this.netObjectFactory = netObjFactory;
        this.socketSettings = SocketSettings.of();
        this.socksClient = client;
        this.sslSocketFactory = sslSockFactory;
    }

    public ConfigureClientSocketBuilder configure() throws SocketException {
        this.socketSettings.applyTo(this.clientSocket);
        return this;
    }

    public ConnectClientSocketBuilder proceedToConnect() {
        return new ConnectClientSocketBuilder(
                this.socksClient,
                this.clientSocket,
                this.netObjectFactory,
                this.sslSocketFactory);
    }

    public ConfigureClientSocketBuilder setSocketSettings(
            final SocketSettings socketSttngs) {
        this.socketSettings = Objects.requireNonNull(socketSttngs);
        return this;
    }

}
