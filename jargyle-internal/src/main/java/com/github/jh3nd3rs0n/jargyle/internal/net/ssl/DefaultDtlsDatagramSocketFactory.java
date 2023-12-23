package com.github.jh3nd3rs0n.jargyle.internal.net.ssl;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Objects;

/**
 * Default implementation of {@code DtlsDatagramSocketFactory}.
 */
final class DefaultDtlsDatagramSocketFactory extends DtlsDatagramSocketFactory {

    /**
     * The {@code SSLContext} as DTLS context.
     */
    private final SSLContext dtlsContext;

    /**
     * Constructs a {@code DefaultDtlsDatagramSocketFactory} with the provided
     * {@code SSLContext} as DTLS context.
     *
     * @param dtlsCntxt the provided {@code SSLContext} as DTLS context.
     */
    public DefaultDtlsDatagramSocketFactory(final SSLContext dtlsCntxt) {
        this.dtlsContext = Objects.requireNonNull(dtlsCntxt);
    }

    @Override
    public DatagramSocket newDatagramSocket(
            final DatagramSocket datagramSocket) throws IOException {
        return new DtlsDatagramSocket(datagramSocket, this.dtlsContext);
    }

}
