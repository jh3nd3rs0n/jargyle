package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;

import java.util.Objects;

/**
 * An agent that acts in behalf of a {@code SocksClient}.
 */
public class SocksClientAgent {

    /**
     * The {@code DtlsDatagramSocketFactory} for the new
     * {@code ClientDatagramSocketBuilder}.
     */
    private final DtlsDatagramSocketFactory dtlsDatagramSocketFactory;

    /**
     * The {@code NetObjectFactory} for the new {@code ClientSocketBuilder}.
     */
    private final NetObjectFactory netObjectFactory;

    /**
     * The {@code Properties} of the {@code SocksClient} of this
     * {@code SocksClientAgent}.
     */
    private final Properties properties;

    /**
     * The {@code SocksClient} of this {@code SocksClientAgent}.
     */
    private final SocksClient socksClient;

    /**
     * The {@code SocksServerUri} of the {@code SocksClient} of this
     * {@code SocksClientAgent}.
     */
    private final SocksServerUri socksServerUri;

    /**
     * The {@code SslSocketFactory} for the new {@code ClientSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * Constructs a {@code SocksClientAgent} with the provided
     * {@code SocksClient}.
     *
     * @param client the provided {@code SocksClient}
     */
    public SocksClientAgent(final SocksClient client) {
        SocksClient c = Objects.requireNonNull(client);
        SocksClient chainedClient = c.getChainedSocksClient();
        Properties props = c.getProperties();
        this.dtlsDatagramSocketFactory =
                ConfiguredDtlsDatagramSocketFactory.newInstance(props);
        this.netObjectFactory = (chainedClient != null) ?
                chainedClient.newSocksNetObjectFactory()
                : NetObjectFactory.getDefault();
        this.properties = props;
        this.socksClient = c;
        this.socksServerUri = c.getSocksServerUri();
        this.sslSocketFactory = ConfiguredSslSocketFactory.newInstance(props);
    }

    /**
     * Returns the {@code Properties} of the {@code SocksClient} of this
     * {@code SocksClientAgent}.
     *
     * @return the {@code Properties} of the {@code SocksClient} of this
     * {@code SocksClientAgent}
     */
    public final Properties getProperties() {
        return this.properties;
    }

    /**
     * Returns the {@code SocksClient} of this {@code SocksServerAgent}.
     *
     * @return the {@code SocksClient} of this {@code SocksServerAgent}
     */
    public final SocksClient getSocksClient() {
        return this.socksClient;
    }

    /**
     * Returns the {@code SocksServerUri} of the {@code SocksClient} of this
     * {@code SocksClientAgent}.
     *
     * @return the {@code SocksServerUri} of the {@code SocksClient} of this
     * {@code SocksClientAgent}
     */
    public final SocksServerUri getSocksServerUri() {
        return this.socksServerUri;
    }

    /**
     * Returns a new {@code ClientDatagramSocketBuilder}.
     *
     * @return a new {@code ClientDatagramSocketBuilder}
     */
    public final ClientDatagramSocketBuilder newClientDatagramSocketBuilder() {
        return new ClientDatagramSocketBuilder(this.dtlsDatagramSocketFactory);
    }

    /**
     * Returns a new {@code ClientSocketBuilder}.
     *
     * @return a new {@code ClientSocketBuilder}
     */
    public final ClientSocketBuilder newClientSocketBuilder() {
        return new ClientSocketBuilder(
                this.socksServerUri,
                this.properties,
                this.netObjectFactory,
                this.sslSocketFactory);
    }

    /**
     * Returns one of the following: the provided cause as a
     * {@code SocksClientIOException} if the provided cause is an instance of
     * {@code SocksClientIOException}, a new {@code SocksClientIOException}
     * with the {@code SocksClient} and the cause from the provided cause if
     * the provided cause is an instance of {@code SocksClientSocketException},
     * or a new {@code SocksClientIOException} with the {@code SocksClient} of
     * this {@code SocksClientAgent} and the provided cause. This method is
     * used for converting {@code Throwable}s thrown from extended networking
     * objects to {@code SocksClientIOException}s in order to help determine
     * from a chain of {@code SocksClient}s which {@code SocksClient} is the
     * associated I/O error coming from.
     *
     * @param cause the provided cause
     * @return one of the following: the provided cause as a
     * {@code SocksClientIOException} if the provided cause is an instance of
     * {@code SocksClientIOException}, a new {@code SocksClientIOException}
     * with the {@code SocksClient} and the cause from the provided cause if
     * the provided cause is an instance of {@code SocksClientSocketException},
     * or a new {@code SocksClientIOException} with the {@code SocksClient} of
     * this {@code SocksClientAgent} and the provided cause
     */
    public final SocksClientIOException toSocksClientIOException(
            final Throwable cause) {
        if (cause instanceof SocksClientIOException) {
            return (SocksClientIOException) cause;
        }
        if (cause instanceof SocksClientSocketException) {
            SocksClientSocketException c = (SocksClientSocketException) cause;
            return new SocksClientIOException(c.getSocksClient(), c.getCause());
        }
        return new SocksClientIOException(this.socksClient, cause);
    }

    /**
     * Returns either the provided cause as a
     * {@code SocksClientSocketException} if the provided cause is an instance
     * of {@code SocksClientSocketException} or a new
     * {@code SocksClientSocketException} with the {@code SocksClient} of this
     * {@code SocksClientAgent} and the provided cause. This method is used
     * for converting {@code Throwable}s thrown from extended Sockets to
     * {@code SocksClientSocketException}s in order to help determine from a
     * chain of {@code SocksClient}s which {@code SocksClient} is the
     * associated error coming from.
     *
     * @param cause the provided cause
     * @return either the provided cause as a
     * {@code SocksClientSocketException} if the provided cause is an instance
     * of {@code SocksClientSocketException} or a new
     * {@code SocksClientSocketException} with the {@code SocksClient} of this
     * {@code SocksClientAgent} and the provided cause
     */
    public final SocksClientSocketException toSocksClientSocketException(
            final Throwable cause) {
        if (cause instanceof SocksClientSocketException) {
            return (SocksClientSocketException) cause;
        }
        return new SocksClientSocketException(this.socksClient, cause);
    }

}
