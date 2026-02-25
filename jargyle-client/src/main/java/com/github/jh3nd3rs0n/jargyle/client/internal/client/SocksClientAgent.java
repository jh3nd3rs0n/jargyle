package com.github.jh3nd3rs0n.jargyle.client.internal.client;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.common.net.Host;
import com.github.jh3nd3rs0n.jargyle.common.net.HostAddressTypes;
import com.github.jh3nd3rs0n.jargyle.common.net.HostIpv4Address;
import com.github.jh3nd3rs0n.jargyle.common.net.NetInterface;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import org.ietf.jgss.Oid;

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
     * The {@code HostResolverFactory} for the new {@code ClientSocketBuilder}.
     */
    private final HostResolverFactory hostResolverFactory;

    /**
     * The {@code Properties} of the {@code SocksClient} of this
     * {@code SocksClientAgent}.
     */
    private final Properties properties;

    /**
     * The {@code SocketFactory} for the new {@code ClientSocketBuilder}.
     */
    private final SocketFactory socketFactory;

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
        this.hostResolverFactory = (chainedClient != null) ?
                chainedClient.getSocksHostResolverFactory()
                : HostResolverFactory.getDefault();
        this.properties = props;
        this.socketFactory = (chainedClient != null) ?
                chainedClient.getSocksSocketFactory()
                : SocketFactory.getDefault();
        this.socksClient = c;
        this.socksServerUri = c.getSocksServerUri();
        this.sslSocketFactory = ConfiguredSslSocketFactory.newInstance(props);
    }

    /**
     * Returns the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER}.
     *
     * @return the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER}
     */
    public boolean canSocksHostResolverResolveFromSocksServer() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER);
    }

    /**
     * Returns the {@code Host} value from one of the properties defined by
     * the {@code PropertySpec} constants:
     * {@link GeneralPropertySpecConstants#CLIENT_BIND_HOST} and
     * {@link GeneralPropertySpecConstants#CLIENT_NET_INTERFACE}.
     *
     * @return the {@code Host} value from one of the properties defined by
     * the {@code PropertySpec} constants:
     * {@link GeneralPropertySpecConstants#CLIENT_BIND_HOST} and
     * {@link GeneralPropertySpecConstants#CLIENT_NET_INTERFACE}
     */
    public final Host getClientBindHost() {
        Host host = this.properties.getValue(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST);
        if (host != null) {
            return host;
        }
        NetInterface netInterface = getClientNetInterface();
        if (netInterface != null) {
            return netInterface.getHostAddresses(
                    getClientBindHostAddressTypes())
                    .stream()
                    .findFirst()
                    .orElse(HostIpv4Address.getAllZerosInstance());
        }
        return HostIpv4Address.getAllZerosInstance();
    }

    /**
     * Returns the {@code HostAddressTypes} value from the property defined by
     * the {@code PropertySpec} constant
     * {@link GeneralPropertySpecConstants#CLIENT_BIND_HOST_ADDRESS_TYPES}.
     *
     * @return the {@code HostAddressTypes} value from the property defined by
     * the {@code PropertySpec} constant
     * {@link GeneralPropertySpecConstants#CLIENT_BIND_HOST_ADDRESS_TYPES}
     */
    private HostAddressTypes getClientBindHostAddressTypes() {
        return this.properties.getValue(
                GeneralPropertySpecConstants.CLIENT_BIND_HOST_ADDRESS_TYPES);
    }

    /**
     * Returns the {@code NetInterface} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link GeneralPropertySpecConstants#CLIENT_NET_INTERFACE}.
     *
     * @return the {@code NetInterface} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link GeneralPropertySpecConstants#CLIENT_NET_INTERFACE}
     */
    private NetInterface getClientNetInterface() {
        return this.properties.getValue(
                GeneralPropertySpecConstants.CLIENT_NET_INTERFACE);
    }

    /**
     * Returns the {@code Oid} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID}.
     *
     * @return the {@code Oid} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID}
     */
    public Oid getGssapiAuthMethodMechanismOid() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID);
    }

    /**
     * Returns the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL}.
     *
     * @return the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL}
     */
    public boolean getGssapiAuthMethodNecReferenceImpl() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL);
    }

    /**
     * Returns the {@code CommaSeparatedValues} value from the property
     * defined by the {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS}.
     *
     * @return the {@code CommaSeparatedValues} value from the property
     * defined by the {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS}
     */
    public CommaSeparatedValues getGssapiAuthMethodProtectionLevels() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS);
    }

    /**
     * Returns the {@code String} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME}.
     *
     * @return the {@code String} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME}
     */
    public String getGssapiAuthMethodServiceName() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME);
    }

    /**
     * Returns the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF}.
     *
     * @return the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF}.
     */
    public boolean getGssapiAuthMethodSuggestedConf() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF);
    }

    /**
     * Returns the {@code int} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG}.
     *
     * @return the {@code int} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG}
     */
    public int getGssapiAuthMethodSuggestedInteg() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG);
    }

    /**
     * Returns the {@code CommaSeparatedValues} value from the property
     * defined by the {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_METHODS}.
     *
     * @return the {@code CommaSeparatedValues} value from the property
     * defined by the {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_METHODS}
     */
    public CommaSeparatedValues getMethods() {
        return this.properties.getValue(SocksPropertySpecConstants.SOCKS_METHODS);
    }

    /**
     * Returns the {@code EncryptedPassword} value from the property defined
     * by the {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_USERPASSAUTHMETHOD_PASSWORD}.
     *
     * @return the {@code EncryptedPassword} value from the property defined
     * by the {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_USERPASSAUTHMETHOD_PASSWORD}
     */
    public EncryptedPassword getUserpassAuthMethodPassword() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_PASSWORD);
    }

    /**
     * Returns the {@code String} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_USERPASSAUTHMETHOD_USERNAME}.
     *
     * @return the {@code String} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_USERPASSAUTHMETHOD_USERNAME}
     */
    public String getUserpassAuthMethodUsername() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_USERNAME);
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
     * Returns the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE}.
     *
     * @return the {@code boolean} value from the property defined by the
     * {@code PropertySpec} constant
     * {@link SocksPropertySpecConstants#SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE}
     */
    public boolean isSocksDatagramSocketClientInfoUnavailable() {
        return this.properties.getValue(
                SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE);
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
                this,
                this.hostResolverFactory,
                this.socketFactory,
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
