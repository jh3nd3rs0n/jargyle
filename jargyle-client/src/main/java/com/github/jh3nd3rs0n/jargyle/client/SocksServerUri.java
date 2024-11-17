package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.SingleValueTypeDoc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * The URI of the SOCKS server.
 */
@SingleValueTypeDoc(
        description = "The URI of the SOCKS server",
        name = "SOCKS Server URI",
        syntax = "SCHEME://[USER_INFO@]HOST[:PORT]",
        syntaxName = "SOCKS_SERVER_URI"
)
public abstract class SocksServerUri {

    /**
     * The {@code int} value of the default port.
     */
    public static final int DEFAULT_PORT_INT_VALUE = 1080;

    /**
     * The {@code Port} of the default port.
     */
    public static final Port DEFAULT_PORT = Port.valueOf(
            DEFAULT_PORT_INT_VALUE);

    /**
     * The {@code int} value of an undefined port.
     */
    private static final int UNDEFINED_PORT_INT_VALUE = -1;

    /**
     * The {@code Host} of this {@code SocksServerUri}.
     */
    private final Host host;

    /**
     * The {@code Port} of this {@code SocksServerUri}.
     */
    private final Port port;

    /**
     * The {@code Scheme} of this {@code SocksServerUri}.
     */
    private final Scheme scheme;

    /**
     * The {@code URI} of this {@code SocksServerUri}.
     */
    private final URI uri;

    /**
     * The {@code UserInfo} of this {@code SocksServerUri}.
     */
    private final UserInfo userInfo;

    /**
     * Constructs a {@code SocksServerUri} with the provided {@code Scheme},
     * the optionally provided {@code UserInfo}, the provided {@code Host},
     * and the optionally provided {@code Port}.
     *
     * @param schm    the provided {@code Scheme}
     * @param usrInfo the optionally provided {@code UserInfo} (can be
     *                {@code null})
     * @param hst     the provided {@code Host}
     * @param prt     the optionally provided {@code Port} (can be
     *                {@code null})
     */
    public SocksServerUri(
            final Scheme schm,
            final UserInfo usrInfo,
            final Host hst,
            final Port prt) {
        Objects.requireNonNull(schm, "scheme must not be null");
        Objects.requireNonNull(hst, "host must not be null");
        URI u;
        try {
            u = new URI(
                    schm.toString(),
                    (usrInfo != null) ? usrInfo.toString() : null,
                    hst.toString(),
                    (prt != null) ? prt.intValue() : UNDEFINED_PORT_INT_VALUE,
                    null,
                    null,
                    null);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        this.host = hst;
        this.port = prt;
        this.scheme = schm;
        this.uri = u;
        this.userInfo = usrInfo;
    }

    /**
     * Returns a new {@code SocksServerUri} from the system property
     * {@code socksClient.socksServerUri}. {@code null} is returned if the
     * system property is not set or the value is invalid.
     *
     * @return a new {@code SocksServerUri} from the system property
     * {@code socksClient.socksServerUri}. {@code null} is returned if the
     * system property is not set or the value is invalid.
     */
    public static SocksServerUri newInstance() {
        PropertySpec<SocksServerUri> socksServerUriPropertySpec =
                GeneralPropertySpecConstants.SOCKS_SERVER_URI;
        String socksServerUriProperty = System.getProperty(
                socksServerUriPropertySpec.getName());
        if (socksServerUriProperty == null) {
            return null;
        }
        try {
            return newInstanceFrom(socksServerUriProperty);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns a new {@code SocksServerUri} from the provided {@code String}
     * of a URI. The path, query, and fragment components of the provided
     * {@code String} of a URI are ignored. An
     * {@code IllegalArgumentException} is thrown if the provided
     * {@code String} of a URI is invalid or if the provided {@code String} of
     * a URI does not have a scheme component or a host component.
     *
     * @param s the provided {@code String}
     * @return a new {@code SocksServerUri} from the provided {@code String}
     * of a URI
     */
    public static SocksServerUri newInstanceFrom(final String s) {
        URI uri;
        try {
            uri = new URI(s);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        return newInstanceFrom(uri);
    }

    /**
     * Returns a new {@code SocksServerUri} from the provided {@code URI}. The
     * path, query, fragment components of the provided {@code URI} are
     * ignored. An {@code IllegalArgumentException} is thrown if the provided
     * {@code URI} does not have a scheme component or a host component.
     *
     * @param uri the provided {@code URI}
     * @return a new {@code SocksServerUri} from the provided {@code URI}
     */
    public static SocksServerUri newInstanceFrom(final URI uri) {
        String message = "URI must be in the following format: "
                + "SCHEME://[USER_INFO@]HOST[:PORT]";
        String scheme = uri.getScheme();
        if (scheme == null) {
            throw new IllegalArgumentException(message);
        }
        Scheme schm = Scheme.valueOfString(scheme);
        String host = uri.getHost();
        if (host == null) {
            throw new IllegalArgumentException(message);
        }
        if (host.startsWith("[") && host.endsWith("]")) {
            host = host.substring(1, host.length() - 1);
        }
        String userInfo = uri.getRawUserInfo();
        int port = uri.getPort();
        return schm.newSocksServerUri(
                (userInfo != null) ? UserInfo.newInstance(userInfo) : null,
                Host.newInstance(host),
                (port != UNDEFINED_PORT_INT_VALUE) ? Port.valueOf(port) : null);
    }

    @Override
    public abstract boolean equals(final Object obj);

    /**
     * Returns the {@code Host} of this {@code SocksServerUri}.
     *
     * @return the {@code Host} of this {@code SocksServerUri}
     */
    public final Host getHost() {
        return this.host;
    }

    /**
     * Returns the {@code Port} of this {@code SocksServerUri} or {@code null}
     * if the {@code Port} was not provided.
     *
     * @return the {@code Port} of this {@code SocksServerUri} or {@code null}
     * if the {@code Port} was not provided
     */
    public final Port getPort() {
        return this.port;
    }

    /**
     * Returns the {@code Port} of this {@code SocksServerUri} or the default
     * {@code Port} if the {@code Port} was not provided.
     *
     * @return the {@code Port} of this {@code SocksServerUri} or the default
     * {@code Port} if the {@code Port} was not provided
     */
    public final Port getPortOrDefault() {
        return (this.port != null) ? this.port : DEFAULT_PORT;
    }

    /**
     * Returns the {@code Scheme} of this {@code SocksServerUri}.
     *
     * @return the {@code Scheme} of this {@code SocksServerUri}
     */
    public final Scheme getScheme() {
        return this.scheme;
    }

    /**
     * Returns the {@code UserInfo} of this {@code SocksServerUri} or
     * {@code null} if the {@code UserInfo} was not provided.
     *
     * @return the {@code UserInfo} of this {@code SocksServerUri} or
     * {@code null} if the {@code UserInfo} was not provided
     */
    public final UserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public abstract int hashCode();

    /**
     * Returns a new {@code SocksClient} with the provided {@code Properties}.
     *
     * @param properties the provided {@code Properties}
     * @return a new {@code SocksClient} with the provided {@code Properties}
     */
    public abstract SocksClient newSocksClient(final Properties properties);

    /**
     * Returns a new {@code SocksClient} with the provided {@code Properties}
     * and the optionally provided {@code SocksClient} to be chained to the
     * new {@code SocksClient}.
     *
     * @param properties         the provided {@code Properties}
     * @param chainedSocksClient the optionally provided {@code SocksClient}
     *                           to be chained to the new {@code SocksClient}
     *                           (can be {@code null})
     * @return a new {@code SocksClient} with the provided {@code Properties}
     * and the optionally provided {@code SocksClient} to be chained to the
     * new {@code SocksClient}
     */
    public abstract SocksClient newSocksClient(
            final Properties properties, final SocksClient chainedSocksClient);

    /**
     * Returns the {@code String} representation of this
     * {@code SocksServerUri}. The {@code String} representation consists of
     * this {@code SocksServerUri} as a URI.
     *
     * @return the {@code String} representation of this
     * {@code SocksServerUri}
     */
    @Override
    public final String toString() {
        return this.uri.toString();
    }

    /**
     * Returns the {@code URI} of this {@code SocksServerUri}.
     *
     * @return the {@code URI} of this {@code SocksServerUri}
     */
    public final URI toURI() {
        return this.uri;
    }

}
