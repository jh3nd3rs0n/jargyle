package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.DatagramSocket;

/**
 * A factory that creates a {@code DtlsDatagramSocket}.
 */
public abstract class DtlsDatagramSocketFactory {

    /**
     * Returns a new default {@code DtlsDatagramSocketFactory} with the
     * provided {@code SSLContext} as DTLS context.
     *
     * @param dtlsContext the provided {@code SSLContext} as DTLS context.
     * @return a new default {@code DtlsDatagramSocketFactory} with the
     * provided {@code SSLContext} as DTLS context
     */
    public static DtlsDatagramSocketFactory newInstance(
            final SSLContext dtlsContext) {
        return new DefaultDtlsDatagramSocketFactory(dtlsContext);
    }

    /**
     * Returns a new {@code DatagramSocket} layered over the existing
     * {@code DatagramSocket}.
     *
     * @param datagramSocket the existing {@code DatagramSocket}
     * @return a new {@code DatagramSocket} layered over the existing
     * {@code DatagramSocket}
     * @throws IOException if an I/O error occurs when creating the
     *                     {@code DatagramSocket}
     */
    public abstract DatagramSocket newDatagramSocket(
            final DatagramSocket datagramSocket) throws IOException;

}
