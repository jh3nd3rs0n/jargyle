package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A builder for the client UDP socket.
 */
public final class ClientDatagramSocketBuilder {

    /**
     * The {@code DtlsDatagramSocketFactory} of this
     * {@code ClientDatagramSocketBuilder}.
     */
    private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;

    /**
     * Constructs a {@code ClientDatagramSocketBuilder} with the provided
     * {@code DtlsDatagramSocketFactory}.
     *
     * @param dtlsDatagramSockFactory the provided
     * {@code DtlsDatagramSocketFactory}
     */
    ClientDatagramSocketBuilder(
            final DtlsDatagramSocketFactory dtlsDatagramSockFactory) {
        this.dtlsDatagramSocketFactory = dtlsDatagramSockFactory;
    }

    /**
     * Returns from the provided client {@code DatagramSocket} a client
     * {@code DatagramSocket} connected to the provided address and the
     * provided port of the SOCKS server. If the property
     * {@code socksClient.dtls.enabled} is set to {@code true}, a client DTLS
     * {@code DatagramSocket} layered over the client {@code DatagramSocket}
     * connected to the provided address and the provided port of the SOCKS
     * server is returned.
     *
     * @param clientDatagramSocket the provided client {@code DatagramSocket}
     * @param address              the provided address of the SOCKS server
     * @param port                 the provided port of the SOCKS server
     * @return a client {@code DatagramSocket} connected to the provided
     * address and the provided port of the SOCKS server
     * @throws IOException if the property {@code socksClient.dtls.enabled} is
     *                     set to {@code true} and an I/O error occurs in
     *                     creating the client DTLS {@code DatagramSocket}
     */
    public DatagramSocket getConnectedClientDatagramSocket(
            final DatagramSocket clientDatagramSocket,
            final InetAddress address,
            final int port) throws IOException {
        clientDatagramSocket.connect(address, port);
        return this.dtlsDatagramSocketFactory.getDatagramSocket(
                clientDatagramSocket);
    }

}
