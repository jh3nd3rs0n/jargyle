package com.github.jh3nd3rs0n.jargyle.protocolbase.socks5;

import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Objects;

/**
 * A {@code MethodEncapsulation} that provides plain networking objects.
 */
final class NullMethodEncapsulation extends MethodEncapsulation {

    /**
     * The plain {@code Socket}.
     */
    private final Socket socket;

    /**
     * Constructs a {@code NullMethodEncapsulation} with the provided plain
     * {@code Socket}.
     *
     * @param sock the provided plain {@code Socket}
     */
    public NullMethodEncapsulation(final Socket sock) {
        this.socket = Objects.requireNonNull(sock);
    }

    @Override
    public DatagramSocket getDatagramSocket(
            final DatagramSocket datagramSocket) {
        return datagramSocket;
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " [socket=" +
                this.socket +
                "]";
    }

}
