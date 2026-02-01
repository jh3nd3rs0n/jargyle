package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;
import org.ietf.jgss.Oid;

import java.util.List;
import java.util.Map;

/**
 * SOCKS5 specific {@code PropertySpec} constants.
 */
@NameValuePairValueSpecsDoc(
        description = "SOCKS5 specific properties",
        name = "SOCKS5 Properties"
)
public final class Socks5PropertySpecConstants {

    /**
     * The {@code PropertySpecs} of the {@code PropertySpec} constants of
     * this class.
     */
    private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapiauthmethod.mechanismOid}: the {@code Oid}
     * for the object ID for the GSS-API authentication mechanism to the
     * SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The object ID for the GSS-API authentication "
                    + "mechanism to the SOCKS5 server",
            name = "socksClient.socks5.gssapiauthmethod.mechanismOid",
            syntax = "socksClient.socks5.gssapiauthmethod.mechanismOid=OID",
            valueType = Oid.class
    )
    public static final PropertySpec<Oid> SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID =
            PROPERTY_SPECS.addThenGet(new OidPropertySpec(
                    "socksClient.socks5.gssapiauthmethod.mechanismOid",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapiauthmethod.necReferenceImpl}: the
     * {@code Boolean} value to indicate if the exchange of the GSS-API
     * protection level negotiation must be unprotected should the SOCKS5
     * server use the NEC reference implementation.
     */
    @NameValuePairValueSpecDoc(
            description = "The boolean value to indicate if the exchange of "
                    + "the GSS-API protection level negotiation must be "
                    + "unprotected should the SOCKS5 server use the NEC "
                    + "reference implementation",
            name = "socksClient.socks5.gssapiauthmethod.necReferenceImpl",
            syntax = "socksClient.socks5.gssapiauthmethod.necReferenceImpl=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks5.gssapiauthmethod.necReferenceImpl",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapiauthmethod.protectionLevels}: the
     * {@code ProtectionLevels} for acceptable protection levels after GSS-API
     * authentication with the SOCKS5 server (The first is preferred while the
     * remaining are acceptable if the server does not accept the first).
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable protection "
                    + "levels after GSS-API authentication with the SOCKS5 "
                    + "server (The first is preferred. The remaining are "
                    + "acceptable if the server does not accept the first.)",
            name = "socksClient.socks5.gssapiauthmethod.protectionLevels",
            syntax = "socksClient.socks5.gssapiauthmethod.protectionLevels=SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS",
            valueType = ProtectionLevels.class
    )
    public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS =
            PROPERTY_SPECS.addThenGet(new Socks5GssapiAuthMethodProtectionLevelsPropertySpec(
                    "socksClient.socks5.gssapiauthmethod.protectionLevels",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapiauthmethod.serviceName}: the GSS-API
     * service name for the SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The GSS-API service name for the SOCKS5 server",
            name = "socksClient.socks5.gssapiauthmethod.serviceName",
            syntax = "socksClient.socks5.gssapiauthmethod.serviceName=SERVICE_NAME",
            valueType = String.class
    )
    public static final PropertySpec<String> SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.socks5.gssapiauthmethod.serviceName",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapiauthmethod.suggestedConf}: the
     * {@code Boolean} value for the suggested privacy (or confidentiality)
     * state for GSS-API messages sent after GSS-API authentication with the
     * SOCKS5 server (applicable if the negotiated protection level is
     * {@code SELECTIVE_INTEG_OR_CONF}).
     */
    @NameValuePairValueSpecDoc(
            description = "The suggested privacy (i.e. confidentiality) state "
                    + "for GSS-API messages sent after GSS-API authentication "
                    + "with the SOCKS5 server (applicable if the negotiated "
                    + "protection level is SELECTIVE_INTEG_OR_CONF)",
            name = "socksClient.socks5.gssapiauthmethod.suggestedConf",
            syntax = "socksClient.socks5.gssapiauthmethod.suggestedConf=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks5.gssapiauthmethod.suggestedConf",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapiauthmethod.suggestedInteg}: the
     * {@code Integer} for the suggested quality-of-protection (or integrity)
     * value for GSS-API messages sent after GSS-API authentication with the
     * SOCKS5 server (applicable if the negotiated protection level is
     * {@code SELECTIVE_INTEG_OR_CONF}).
     */
    @NameValuePairValueSpecDoc(
            description = "The suggested quality-of-protection (i.e. "
                    + "integrity) value for GSS-API messages sent after "
                    + "GSS-API authentication with the SOCKS5 server "
                    + "(applicable if the negotiated protection level is "
                    + "SELECTIVE_INTEG_OR_CONF)",
            name = "socksClient.socks5.gssapiauthmethod.suggestedInteg",
            syntax = "socksClient.socks5.gssapiauthmethod.suggestedInteg=-2147483648-2147483647",
            valueType = Integer.class
    )
    public static final PropertySpec<Integer> SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG =
            PROPERTY_SPECS.addThenGet(new IntegerPropertySpec(
                    "socksClient.socks5.gssapiauthmethod.suggestedInteg",
                    null));

    /**
     * {@code PropertySpec} constant for {@code socksClient.socks5.methods}:
     * the {@code Methods} for acceptable authentication methods to the
     * SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The comma separated list of acceptable "
                    + "authentication methods to the SOCKS5 server",
            name = "socksClient.socks5.methods",
            syntax = "socksClient.socks5.methods=SOCKS5_METHODS",
            valueType = Methods.class
    )
    public static final PropertySpec<Methods> SOCKS5_METHODS =
            PROPERTY_SPECS.addThenGet(new Socks5MethodsPropertySpec(
                    "socksClient.socks5.methods",
                    Methods.of()));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.socks5DatagramSocket.clientInfoUnavailable}:
     * the {@code Boolean} value to indicate if the client information
     * expected to be used to send UDP datagrams (address and port) is
     * unavailable to be sent to the SOCKS5 server (an address and port of all
     * zeros is sent instead).
     */
    @NameValuePairValueSpecDoc(
            description = "The boolean value to indicate if the client "
                    + "information expected to be used to send UDP datagrams "
                    + "(address and port) is unavailable to be sent to the "
                    + "SOCKS5 server (an address and port of all zeros is "
                    + "sent instead)",
            name = "socksClient.socks5.socks5DatagramSocket.clientInfoUnavailable",
            syntax = "socksClient.socks5.socks5DatagramSocket.clientInfoUnavailable=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks5.socks5DatagramSocket.clientInfoUnavailable",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.socks5HostResolver.resolveFromSocks5Server}:
     * the {@code Boolean} value to indicate if host names are to be
     * resolved from the SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The boolean value to indicate if host names "
                    + "are to be resolved from the SOCKS5 server",
            name = "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server",
            syntax = "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.userpassauthmethod.password}: the
     * {@code EncryptedPassword} for the password to be used to access the
     * SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The password to be used to access the SOCKS5 server",
            name = "socksClient.socks5.userpassauthmethod.password",
            syntax = "socksClient.socks5.userpassauthmethod.password=PASSWORD",
            valueType = String.class
    )
    public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSAUTHMETHOD_PASSWORD =
            PROPERTY_SPECS.addThenGet(new Socks5UserpassAuthMethodEncryptedPasswordPropertySpec(
                    "socksClient.socks5.userpassauthmethod.password",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.userpassauthmethod.username}: the username to be
     * used to access the SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The username to be used to access the SOCKS5 server",
            name = "socksClient.socks5.userpassauthmethod.username",
            syntax = "socksClient.socks5.userpassauthmethod.username=USERNAME",
            valueType = String.class
    )
    public static final PropertySpec<String> SOCKS5_USERPASSAUTHMETHOD_USERNAME =
            PROPERTY_SPECS.addThenGet(new Socks5UserpassAuthMethodUsernamePropertySpec(
                    "socksClient.socks5.userpassauthmethod.username",
                    null));

    /**
     * Prevents the construction of unnecessary instances.
     */
    private Socks5PropertySpecConstants() {
    }

    /**
     * Returns an unmodifiable {@code List} of the {@code PropertySpec}
     * constants.
     *
     * @return an unmodifiable {@code List} of the {@code PropertySpec}
     * constants
     */
    public static List<PropertySpec<Object>> values() {
        return PROPERTY_SPECS.toList();
    }

    /**
     * Returns an unmodifiable {@code Map} of the {@code PropertySpec}
     * constants each associated by the name they specify for their
     * {@code Property}.
     *
     * @return an unmodifiable {@code Map} of the {@code PropertySpec}
     * constants each associated by the name they specify for their
     * {@code Property}
     */
    public static Map<String, PropertySpec<Object>> valuesMap() {
        return PROPERTY_SPECS.toMap();
    }

}
