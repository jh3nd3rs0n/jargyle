package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Provides based on the negotiated authentication method networking objects
 * that may encapsulate data to be sent and may de-encapsulate data received.
 */
public abstract class MethodEncapsulation {

    /**
     * Allows the construction of subclass instances.
     */
    public MethodEncapsulation() {
    }

    /**
     * Returns a new instance of {@code MethodEncapsulation} that provides
     * plain networking objects such as the provided plain {@code Socket}.
     *
     * @param sock the provided plain {@code Socket}
     * @return a new instance of {@code MethodEncapsulation} that provides
     * plain networking objects such as the provided plain {@code Socket}
     */
    public static MethodEncapsulation newNullInstance(final Socket sock) {
        return new NullMethodEncapsulation(sock);
    }

    /**
     * Takes the provided plain {@code DatagramSocket} and returns based on
     * the negotiated authentication method a {@code DatagramSocket} that
     * may encapsulate data to be sent and may de-encapsulate data received.
     *
     * @param datagramSocket the provided plain {@code DatagramSocket}
     * @return based on the negotiated authentication method a
     * {@code DatagramSocket} that may encapsulate data to be sent and
     * may de-encapsulate data received
     * @throws IOException if an I/O error occurs
     */
    public abstract DatagramSocket getDatagramSocket(
            final DatagramSocket datagramSocket) throws IOException;

    /**
     * Returns based on the negotiated authentication method a {@code Socket}
     * that may encapsulate data to be sent and may de-encapsulate data
     * received.
     *
     * @return based on the negotiated authentication method a {@code Socket}
     * that may encapsulate data to be sent and may de-encapsulate data
     * received
     */
    public abstract Socket getSocket();

    /**
     * Returns the {@code String} representation of this
     * {@code MethodEncapsulation}.
     *
     * @return the {@code String} representation of this
     * {@code MethodEncapsulation}
     */
    @Override
    public abstract String toString();

}
