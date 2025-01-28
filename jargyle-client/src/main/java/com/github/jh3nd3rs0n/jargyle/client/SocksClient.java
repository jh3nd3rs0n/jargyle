package com.github.jh3nd3rs0n.jargyle.client;

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
     * The {@code Properties} of this {@code SocksClient}.
     */
    private final Properties properties;

    /**
     * The {@code SocksServerUri} of this {@code SocksClient}.
     */
    private final SocksServerUri socksServerUri;

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
        this.chainedSocksClient = chainedClient;
        this.properties = props;
        this.socksServerUri = serverUri;
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
     * Returns a new {@code SocksNetObjectFactory}.
     *
     * @return a new {@code SocksNetObjectFactory}
     */
    public abstract SocksNetObjectFactory newSocksNetObjectFactory();

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
