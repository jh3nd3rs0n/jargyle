package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.socks5.Socks5ServerUri;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.Port;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.EnumValueTypeDoc;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Specifies what SOCKS protocol is to be used to access the SOCKS server.
 */
@EnumValueTypeDoc(
        description = "Specifies what SOCKS protocol is to be used to access "
                + "the SOCKS server",
        name = "SOCKS Server URI Scheme",
        syntax = "socks5",
        syntaxName = "SOCKS_SERVER_URI_SCHEME"
)
public enum SocksServerUriScheme {

    /**
     * SOCKS protocol version 5.
     */
    @EnumValueDoc(description = "SOCKS protocol version 5", value = "socks5")
    SOCKS5("socks5") {
        @Override
        public SocksServerUri newSocksServerUri(final String host) {
            return new Socks5ServerUri(null, Host.newInstance(host), null);
        }

        @Override
        public SocksServerUri newSocksServerUri(
                final String userInfo, final String host) {
            return new Socks5ServerUri(
                    UserInfo.newInstance(userInfo),
                    Host.newInstance(host),
                    null);
        }

        @Override
        public SocksServerUri newSocksServerUri(
                final String userInfo, final String host, final int port) {
            return new Socks5ServerUri(
                    UserInfo.newInstance(userInfo),
                    Host.newInstance(host),
                    Port.valueOf(port));
        }

        @Override
        public SocksServerUri newSocksServerUri(
                final String host, final int port) {
            return new Socks5ServerUri(
                    null, Host.newInstance(host), Port.valueOf(port));
        }

        @Override
        public SocksServerUri newSocksServerUri(
                final UserInfo userInfo, final Host host, final Port port) {
            return new Socks5ServerUri(userInfo, host, port);
        }

    };

    /**
     * The {@code String} representation of this {@code SocksServerUriScheme}.
     */
    private final String string;

    /**
     * Constructs a {@code SocksServerUriScheme} of the provided SOCKS server
     * URI scheme.
     *
     * @param str the provided SOCKS server URI scheme
     */
    SocksServerUriScheme(final String str) {
        this.string = str;
    }

    /**
     * Returns the enum constant of the provided {@code String}
     * representation. An {@code IllegalArgumentException} is thrown if there
     * is no enum constant of the provided {@code String} representation.
     *
     * @param string the provided {@code String} representation
     * @return the enum constant of the provided {@code String}
     * representation
     */
    public static SocksServerUriScheme valueOfString(final String string) {
        for (SocksServerUriScheme socksServerUriScheme
                : SocksServerUriScheme.values()) {
            if (socksServerUriScheme.toString().equals(string)) {
                return socksServerUriScheme;
            }
        }
        String str = Arrays.stream(SocksServerUriScheme.values())
                .map(SocksServerUriScheme::toString)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format(
                "expected SOCKS server URI scheme must be one of the "
                        + "following values: %s. "
                        + "actual value is %s",
                str,
                string));
    }

    /**
     * Returns a new {@code SocksServerUri} with the provided host. An
     * {@code IllegalArgumentException} is thrown if the provided host is
     * invalid.
     *
     * @param host the provided host
     * @return a new {@code SocksServerUri} with the provided host
     */
    public abstract SocksServerUri newSocksServerUri(final String host);

    /**
     * Returns a new {@code SocksServerUri} with the provided user information
     * and the provided host. An {@code IllegalArgumentException} is thrown if
     * the provided user information is invalid or if the provided host is
     * invalid.
     *
     * @param userInfo the provided user information
     * @param host     the provided host
     * @return a new {@code SocksServerUri} with the provided user information
     * and the provided host
     */
    public abstract SocksServerUri newSocksServerUri(
            final String userInfo, final String host);

    /**
     * Returns a new {@code SocksServerUri} with the provided user
     * information, the provided host, and the provided port. An
     * {@code IllegalArgumentException} is thrown if the provided user
     * information is invalid, if the provided host is invalid, or if the
     * provided port is less than {@code 0} or greater than {@code 65535}.
     *
     * @param userInfo the provided user information
     * @param host     the provided host
     * @param port     the provided port
     * @return a new {@code SocksServerUri} with the provided user
     * information, the provided host, and the provided port
     */
    public abstract SocksServerUri newSocksServerUri(
            final String userInfo, final String host, final int port);

    /**
     * Returns a new {@code SocksServerUri} with the provided host and the
     * provided port. An {@code IllegalArgumentException} is thrown if the
     * provided host is invalid or if the provided port is less than {@code 0}
     * or greater than {@code 65535}.
     *
     * @param host the provided host
     * @param port the provided port
     * @return a new {@code SocksServerUri} with the provided host and the
     * provided port
     */
    public abstract SocksServerUri newSocksServerUri(
            final String host, final int port);

    /**
     * Returns a new {@code SocksServerUri} with the optionally provided
     * {@code UserInfo}, the provided {@code Host}, and the optionally
     * provided {@code Port}.
     *
     * @param userInfo the optionally provided {@code UserInfo} (can be
     *                 {@code null})
     * @param host     the provided {@code Host}
     * @param port     the optionally provided {@code Port} (can be
     *                 {@code null})
     * @return a new {@code SocksServerUri} with the optionally provided
     * {@code UserInfo}, the provided {@code Host}, and the optionally
     * provided {@code Port}
     */
    public abstract SocksServerUri newSocksServerUri(
            final UserInfo userInfo, final Host host, final Port port);

    /**
     * Returns the {@code String} representation of this
     * {@code SocksServerUriScheme}.
     *
     * @return the {@code String} representation of this
     * {@code SocksServerUriScheme}
     */
    @Override
    public String toString() {
        return this.string;
    }

}