package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
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
     * {@code socksClient.socks5.gssapimethod.mechanismOid}: the {@code Oid}
     * for the object ID for the GSS-API authentication mechanism to the
     * SOCKS5 server (default value is {@code 1.2.840.113554.1.2.2}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "1.2.840.113554.1.2.2",
            description = "The object ID for the GSS-API authentication "
                    + "mechanism to the SOCKS5 server",
            name = "socksClient.socks5.gssapimethod.mechanismOid",
            syntax = "socksClient.socks5.gssapimethod.mechanismOid=OID",
            valueType = Oid.class
    )
    public static final PropertySpec<Oid> SOCKS5_GSSAPIMETHOD_MECHANISM_OID =
            PROPERTY_SPECS.addThenGet(new OidPropertySpec(
                    "socksClient.socks5.gssapimethod.mechanismOid",
                    "1.2.840.113554.1.2.2"));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapimethod.necReferenceImpl}: the
     * {@code Boolean} value to indicate if the exchange of the GSS-API
     * protection level negotiation must be unprotected should the SOCKS5
     * server use the NEC reference implementation (default value is
     * {@code false}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the exchange of "
                    + "the GSS-API protection level negotiation must be "
                    + "unprotected should the SOCKS5 server use the NEC "
                    + "reference implementation",
            name = "socksClient.socks5.gssapimethod.necReferenceImpl",
            syntax = "socksClient.socks5.gssapimethod.necReferenceImpl=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks5.gssapimethod.necReferenceImpl",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapimethod.protectionLevels}: the
     * {@code ProtectionLevels} for acceptable protection levels after GSS-API
     * authentication with the SOCKS5 server (The first is preferred while the
     * remaining are acceptable if the server does not accept the first)
     * (default value is {@code REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE",
            description = "The comma separated list of acceptable protection "
                    + "levels after GSS-API authentication with the SOCKS5 "
                    + "server (The first is preferred. The remaining are "
                    + "acceptable if the server does not accept the first.)",
            name = "socksClient.socks5.gssapimethod.protectionLevels",
            syntax = "socksClient.socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS",
            valueType = ProtectionLevels.class
    )
    public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS =
            PROPERTY_SPECS.addThenGet(new Socks5GssapiMethodProtectionLevelsPropertySpec(
                    "socksClient.socks5.gssapimethod.protectionLevels",
                    ProtectionLevels.getDefault()));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapimethod.serviceName}: the GSS-API
     * service name for the SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The GSS-API service name for the SOCKS5 server",
            name = "socksClient.socks5.gssapimethod.serviceName",
            syntax = "socksClient.socks5.gssapimethod.serviceName=SERVICE_NAME",
            valueType = String.class
    )
    public static final PropertySpec<String> SOCKS5_GSSAPIMETHOD_SERVICE_NAME =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.socks5.gssapimethod.serviceName",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapimethod.suggestedConf}: the
     * {@code Boolean} value for the suggested privacy (or confidentiality)
     * state for GSS-API messages sent after GSS-API authentication with the
     * SOCKS5 server (applicable if the negotiated protection level is
     * {@code SELECTIVE_INTEG_OR_CONF}) (default value is {@code true}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "true",
            description = "The suggested privacy (i.e. confidentiality) state "
                    + "for GSS-API messages sent after GSS-API authentication "
                    + "with the SOCKS5 server (applicable if the negotiated "
                    + "protection level is SELECTIVE_INTEG_OR_CONF)",
            name = "socksClient.socks5.gssapimethod.suggestedConf",
            syntax = "socksClient.socks5.gssapimethod.suggestedConf=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks5.gssapimethod.suggestedConf",
                    Boolean.TRUE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.gssapimethod.suggestedInteg}: the
     * {@code Integer} for the suggested quality-of-protection (or integrity)
     * value for GSS-API messages sent after GSS-API authentication with the
     * SOCKS5 server (applicable if the negotiated protection level is
     * {@code SELECTIVE_INTEG_OR_CONF}) (default value is {@code 0}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "0",
            description = "The suggested quality-of-protection (i.e. "
                    + "integrity) value for GSS-API messages sent after "
                    + "GSS-API authentication with the SOCKS5 server "
                    + "(applicable if the negotiated protection level is "
                    + "SELECTIVE_INTEG_OR_CONF)",
            name = "socksClient.socks5.gssapimethod.suggestedInteg",
            syntax = "socksClient.socks5.gssapimethod.suggestedInteg=-2147483648-2147483647",
            valueType = Integer.class
    )
    public static final PropertySpec<Integer> SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG =
            PROPERTY_SPECS.addThenGet(new IntegerPropertySpec(
                    "socksClient.socks5.gssapimethod.suggestedInteg",
                    0));

    /**
     * {@code PropertySpec} constant for {@code socksClient.socks5.methods}:
     * the {@code Methods} for acceptable authentication methods to the
     * SOCKS5 server (default value is {@code NO_AUTHENTICATION_REQUIRED}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "NO_AUTHENTICATION_REQUIRED",
            description = "The comma separated list of acceptable "
                    + "authentication methods to the SOCKS5 server",
            name = "socksClient.socks5.methods",
            syntax = "socksClient.socks5.methods=SOCKS5_METHODS",
            valueType = Methods.class
    )
    public static final PropertySpec<Methods> SOCKS5_METHODS =
            PROPERTY_SPECS.addThenGet(new Socks5MethodsPropertySpec(
                    "socksClient.socks5.methods",
                    Methods.getDefault()));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.socks5DatagramSocket.clientInfoUnavailable}:
     * the {@code Boolean} value to indicate if the client information
     * expected to be used to send UDP datagrams (address and port) is
     * unavailable to be sent to the SOCKS5 server (an address and port of all
     * zeros is sent instead) (default value is {@code false}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "false",
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
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.socks5HostResolver.resolveFromSocks5Server}:
     * the {@code Boolean} value to indicate if host names are to be
     * resolved from the SOCKS5 server (default value is {@code false}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if host names "
                    + "are to be resolved from the SOCKS5 server",
            name = "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server",
            syntax = "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.userpassmethod.password}: the
     * {@code EncryptedPassword} for the password to be used to access the
     * SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The password to be used to access the SOCKS5 server",
            name = "socksClient.socks5.userpassmethod.password",
            syntax = "socksClient.socks5.userpassmethod.password=PASSWORD",
            valueType = String.class
    )
    public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSMETHOD_PASSWORD =
            PROPERTY_SPECS.addThenGet(new Socks5UserpassMethodEncryptedPasswordPropertySpec(
                    "socksClient.socks5.userpassmethod.password",
                    EncryptedPassword.newInstance(new char[]{})));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks5.userpassmethod.username}: the username to be
     * used to access the SOCKS5 server.
     */
    @NameValuePairValueSpecDoc(
            description = "The username to be used to access the SOCKS5 server",
            name = "socksClient.socks5.userpassmethod.username",
            syntax = "socksClient.socks5.userpassmethod.username=USERNAME",
            valueType = String.class
    )
    public static final PropertySpec<String> SOCKS5_USERPASSMETHOD_USERNAME =
            PROPERTY_SPECS.addThenGet(new Socks5UserpassMethodUsernamePropertySpec(
                    "socksClient.socks5.userpassmethod.username",
                    System.getProperty("user.name")));

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
