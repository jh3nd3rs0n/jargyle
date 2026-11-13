package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * A builder for the SOCKS server facing UDP socket.
 */
public final class SocksServerFacingDatagramSocketBuilder {

    /**
     * The {@code DtlsDatagramSocketFactory} of this
     * {@code SocksServerFacingDatagramSocketBuilder}.
     */
    private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;

    /**
     * Constructs a {@code SocksServerFacingDatagramSocketBuilder} with the
     * provided {@code DtlsDatagramSocketFactory}.
     *
     * @param dtlsDatagramSockFactory the provided
     *                                {@code DtlsDatagramSocketFactory}
     */
    SocksServerFacingDatagramSocketBuilder(
            final DtlsDatagramSocketFactory dtlsDatagramSockFactory) {
        this.dtlsDatagramSocketFactory = dtlsDatagramSockFactory;
    }

    /**
     * Returns from the provided SOCKS server facing {@code DatagramSocket} a
     * SOCKS server facing {@code DatagramSocket} connected to the provided
     * address and the provided port of the SOCKS server. If the property
     * {@code socksClient.dtls.enabled} is set to {@code true}, a SOCKS server
     * facing DTLS {@code DatagramSocket} layered over the SOCKS server facing
     * {@code DatagramSocket} connected to the provided address and the
     * provided port of the SOCKS server is returned.
     *
     * @param socksServerFacingDatagramSocket the provided SOCKS server facing
     *                                        {@code DatagramSocket}
     * @param address                         the provided address of the
     *                                        SOCKS server
     * @param port                            the provided port of the SOCKS
     *                                        server
     * @return a SOCKS server facing {@code DatagramSocket} connected to the
     * provided address and the provided port of the SOCKS server
     * @throws IOException if the property {@code socksClient.dtls.enabled} is
     *                     set to {@code true} and an I/O error occurs in
     *                     creating the SOCKS server facing DTLS
     *                     {@code DatagramSocket}
     */
    public DatagramSocket getConnectedSocksServerFacingDatagramSocket(
            final DatagramSocket socksServerFacingDatagramSocket,
            final InetAddress address,
            final int port) throws IOException {
        socksServerFacingDatagramSocket.connect(address, port);
        return this.dtlsDatagramSocketFactory.getDatagramSocket(
                socksServerFacingDatagramSocket);
    }

}
