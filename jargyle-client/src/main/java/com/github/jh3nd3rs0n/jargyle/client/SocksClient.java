package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A SOCKS client.
 */
public abstract class SocksClient {

    /**
     * The {@code SocksClient} chained to this {@code SocksClient}.
     */
    private final SocksClient chainedSocksClient;

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
     * The {@code Properties} of this {@code SocksClient}.
     */
    private final Properties properties;

    /**
     * The {@code SocksServerUri} of this {@code SocksClient}.
     */
    private final SocksServerUri socksServerUri;

    /**
     * The {@code SslSocketFactory} for the new {@code ClientSocketBuilder}.
     */
    private final SslSocketFactory sslSocketFactory;

    /**
     * Constructs a {@code SocksClient} with the provided
     * {@code SocksServerUri} and the provided {@code Properties}.
     *
     * @param serverUri the provided {@code SocksServerUri}
     * @param props     the provided {@code Properties}
     */
    public SocksClient(
            final SocksServerUri serverUri, final Properties props) {
        this(serverUri, props, null);
    }

    /**
     * Constructs a {@code SocksClient} with the provided
     * {@code SocksServerUri}, the provided {@code Properties}, and the
     * optionally provided {@code SocksClient} to be chained to the
     * constructed {@code SocksClient}.
     *
     * @param serverUri     the provided {@code SocksServerUri}
     * @param props         the provided {@code Properties}
     * @param chainedClient the optionally provided {@code SocksClient} to be
     *                      chained to the constructed {@code SocksClient}
     *                      (can be {@code null})
     */
    public SocksClient(
            final SocksServerUri serverUri,
            final Properties props,
            final SocksClient chainedClient) {
        Objects.requireNonNull(
                serverUri, "SOCKS server URI must not be null");
        Objects.requireNonNull(props, "Properties must not be null");
        NetObjectFactory netObjFactory = (chainedClient != null) ?
                chainedClient.newSocksNetObjectFactory()
                : NetObjectFactory.getDefault();
        this.chainedSocksClient = chainedClient;
        this.dtlsDatagramSocketFactory = new DtlsDatagramSocketFactoryImpl(
                props);
        this.netObjectFactory = netObjFactory;
        this.properties = props;
        this.socksServerUri = serverUri;
        this.sslSocketFactory = new SslSocketFactoryImpl(props);
    }

    /**
     * Returns a new {@code SocksClient} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     *
     * @return a new {@code SocksClient} from the system property
     * {@code socksClient.socksServerUri} and system properties defined by
     * other {@code PropertySpec} constants classes. {@code null} is returned
     * if the system property {@code socksClient.socksServerUri} is not set or
     * the value is invalid.
     */
    public static SocksClient newInstance() {
        SocksServerUri socksServerUri = SocksServerUri.newInstance();
        if (socksServerUri == null) {
            return null;
        }
        List<Property<?>> properties = new ArrayList<>();
        for (PropertySpec<Object> propertySpec
                : PropertySpecConstants.values()) {
            if (propertySpec.equals(
                    GeneralPropertySpecConstants.SOCKS_SERVER_URI)) {
                continue;
            }
            String property = System.getProperty(propertySpec.getName());
            if (property != null) {
                properties.add(propertySpec.newPropertyWithParsedValue(
                        property));
            }
        }
        return socksServerUri.newSocksClient(
                Properties.of(properties));
    }

    /**
     * Returns the {@code SocksClient} chained to this {@code SocksClient}.
     *
     * @return the {@code SocksClient} chained to this {@code SocksClient}
     */
    public final SocksClient getChainedSocksClient() {
        return this.chainedSocksClient;
    }

    /**
     * Returns the {@code Properties} of this {@code SocksClient}.
     *
     * @return the {@code Properties} of this {@code SocksClient}
     */
    public final Properties getProperties() {
        return this.properties;
    }

    /**
     * Returns the {@code SocksServerUri} of this {@code SocksClient}.
     *
     * @return the {@code SocksServerUri} of this {@code SocksClient}
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
     * Returns a new {@code SocksNetObjectFactory}.
     *
     * @return a new {@code SocksNetObjectFactory}
     */
    public abstract SocksNetObjectFactory newSocksNetObjectFactory();

    /**
     * Returns one of the following: the provided cause as a
     * {@code SocksClientIOException} if the provided cause is an instance of
     * {@code SocksClientIOException}, a new {@code SocksClientIOException}
     * with the {@code SocksClient} and the cause from the provided cause if
     * the provided cause is an instance of {@code SocksClientSocketException},
     * or a new {@code SocksClientIOException} with this {@code SocksClient}
     * and the provided cause. This method is used for converting
     * {@code Throwable}s thrown from extended networking objects to
     * {@code SocksClientIOException}s in order to help determine from a chain
     * of {@code SocksClient}s which {@code SocksClient} is the associated I/O
     * error coming from.
     *
     * @param cause the provided cause
     * @return one of the following: the provided cause as a
     * {@code SocksClientIOException} if the provided cause is an instance of
     * {@code SocksClientIOException}, a new {@code SocksClientIOException}
     * with the {@code SocksClient} and the cause from the provided cause if
     * the provided cause is an instance of {@code SocksClientSocketException},
     * or a new {@code SocksClientIOException} with this {@code SocksClient}
     * and the provided cause
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
        return new SocksClientIOException(this, cause);
    }

    /**
     * Returns either the provided cause as a
     * {@code SocksClientSocketException} if the provided cause is an instance
     * of {@code SocksClientSocketException} or a new
     * {@code SocksClientSocketException} with this {@code SocksClient} and the
     * provided cause. This method is used for converting {@code Throwable}s
     * thrown from extended Sockets to {@code SocksClientSocketException}s in
     * order to help determine from a chain of {@code SocksClient}s which
     * {@code SocksClient} is the associated error coming from.
     *
     * @param cause the provided cause
     * @return either the provided cause as a
     * {@code SocksClientSocketException} if the provided cause is an instance
     * of {@code SocksClientSocketException} or a new
     * {@code SocksClientSocketException} with this {@code SocksClient} and the
     * provided cause
     */
    public final SocksClientSocketException toSocksClientSocketException(
            final Throwable cause) {
        if (cause instanceof SocksClientSocketException) {
            return (SocksClientSocketException) cause;
        }
        return new SocksClientSocketException(this, cause);
    }

    /**
     * Returns the {@code String} representation of this {@code SocksClient}.
     *
     * @return the {@code String} representation of this {@code SocksClient}
     */
    @Override
    public final String toString() {
        return this.getClass().getSimpleName() +
                " [chainedSocksClient=" +
                this.chainedSocksClient +
                ", socksServerUri=" +
                this.socksServerUri +
                "]";
    }

}
