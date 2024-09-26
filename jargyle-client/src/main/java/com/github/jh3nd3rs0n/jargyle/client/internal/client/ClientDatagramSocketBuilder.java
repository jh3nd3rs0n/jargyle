package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

public final class ClientDatagramSocketBuilder {

    private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;

    public ClientDatagramSocketBuilder(final Properties props) {
        this.dtlsDatagramSocketFactory =
                DtlsDatagramSocketFactoryImpl.isDtlsEnabled(props) ?
                        new DtlsDatagramSocketFactoryImpl(props) : null;
    }

    public DatagramSocket getConnectedClientDatagramSocket(
            final DatagramSocket clientDatagramSocket,
            final InetAddress address,
            final int port) throws IOException {
        DatagramSocket clientDatagramSock = clientDatagramSocket;
        clientDatagramSock.connect(address, port);
        if (this.dtlsDatagramSocketFactory == null) {
            return clientDatagramSock;
        }
        clientDatagramSock = this.dtlsDatagramSocketFactory.newDatagramSocket(
                clientDatagramSock);
        return clientDatagramSock;
    }

}
