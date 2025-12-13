package com.github.jh3nd3rs0n.jargyle.client;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.*;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import org.ietf.jgss.Oid;

import java.util.List;
import java.util.Map;

/**
 * SOCKS specific {@code PropertySpec} constants.
 */
@NameValuePairValueSpecsDoc(
        description = "SOCKS specific properties",
        name = "SOCKS Properties"
)
public final class SocksPropertySpecConstants {

    /**
     * The {@code PropertySpecs} of the {@code PropertySpec} constants of
     * this class.
     */
    private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.gssapiauthmethod.mechanismOid}: the {@code Oid}
     * for the object ID for the GSS-API authentication mechanism to the
     * SOCKS server (default value is {@code 1.2.840.113554.1.2.2}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "1.2.840.113554.1.2.2",
            description = "The object ID for the GSS-API authentication "
                    + "mechanism to the SOCKS server",
            name = "socksClient.socks.gssapiauthmethod.mechanismOid",
            syntax = "socksClient.socks.gssapiauthmethod.mechanismOid=OID",
            valueType = Oid.class
    )
    public static final PropertySpec<Oid> SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID =
            PROPERTY_SPECS.addThenGet(new OidPropertySpec(
                    "socksClient.socks.gssapiauthmethod.mechanismOid",
                    "1.2.840.113554.1.2.2"));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.gssapiauthmethod.necReferenceImpl}: the
     * {@code Boolean} value to indicate if the exchange of the GSS-API
     * protection level negotiation must be unprotected should the SOCKS
     * server use the NEC reference implementation (default value is
     * {@code false}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the exchange of "
                    + "the GSS-API protection level negotiation must be "
                    + "unprotected should the SOCKS server use the NEC "
                    + "reference implementation",
            name = "socksClient.socks.gssapiauthmethod.necReferenceImpl",
            syntax = "socksClient.socks.gssapiauthmethod.necReferenceImpl=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks.gssapiauthmethod.necReferenceImpl",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.gssapiauthmethod.protectionLevels}: the
     * {@code CommaSeparatedValues} for acceptable protection levels after
     * GSS-API authentication with the SOCKS server (The first is preferred
     * while the remaining are acceptable if the server does not accept the
     * first) (default value is
     * {@code REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE",
            description = "The comma separated list of acceptable protection "
                    + "levels after GSS-API authentication with the SOCKS "
                    + "server (The first is preferred. The remaining are "
                    + "acceptable if the server does not accept the first.)",
            name = "socksClient.socks.gssapiauthmethod.protectionLevels",
            syntax = "socksClient.socks.gssapiauthmethod.protectionLevels=SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS",
            valueType = CommaSeparatedValues.class
    )
    public static final PropertySpec<CommaSeparatedValues> SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS =
            PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
                    "socksClient.socks.gssapiauthmethod.protectionLevels",
                    CommaSeparatedValues.of(
                            "REQUIRED_INTEG_AND_CONF",
                            "REQUIRED_INTEG",
                            "NONE")));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.gssapiauthmethod.serviceName}: the GSS-API
     * service name for the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The GSS-API service name for the SOCKS server",
            name = "socksClient.socks.gssapiauthmethod.serviceName",
            syntax = "socksClient.socks.gssapiauthmethod.serviceName=SERVICE_NAME",
            valueType = String.class
    )
    public static final PropertySpec<String> SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.socks.gssapiauthmethod.serviceName",
                    null));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.gssapiauthmethod.suggestedConf}: the
     * {@code Boolean} value for the suggested privacy (or confidentiality)
     * state for GSS-API messages sent after GSS-API authentication with the
     * SOCKS server (applicable if the negotiated protection level is
     * {@code SELECTIVE_INTEG_OR_CONF}) (default value is {@code true}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "true",
            description = "The suggested privacy (i.e. confidentiality) state "
                    + "for GSS-API messages sent after GSS-API authentication "
                    + "with the SOCKS server (applicable if the negotiated "
                    + "protection level is SELECTIVE_INTEG_OR_CONF)",
            name = "socksClient.socks.gssapiauthmethod.suggestedConf",
            syntax = "socksClient.socks.gssapiauthmethod.suggestedConf=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks.gssapiauthmethod.suggestedConf",
                    Boolean.TRUE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.gssapiauthmethod.suggestedInteg}: the
     * {@code Integer} for the suggested quality-of-protection (or integrity)
     * value for GSS-API messages sent after GSS-API authentication with the
     * SOCKS server (applicable if the negotiated protection level is
     * {@code SELECTIVE_INTEG_OR_CONF}) (default value is {@code 0}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "0",
            description = "The suggested quality-of-protection (i.e. "
                    + "integrity) value for GSS-API messages sent after "
                    + "GSS-API authentication with the SOCKS server "
                    + "(applicable if the negotiated protection level is "
                    + "SELECTIVE_INTEG_OR_CONF)",
            name = "socksClient.socks.gssapiauthmethod.suggestedInteg",
            syntax = "socksClient.socks.gssapiauthmethod.suggestedInteg=-2147483648-2147483647",
            valueType = Integer.class
    )
    public static final PropertySpec<Integer> SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG =
            PROPERTY_SPECS.addThenGet(new IntegerPropertySpec(
                    "socksClient.socks.gssapiauthmethod.suggestedInteg",
                    0));

    /**
     * {@code PropertySpec} constant for {@code socksClient.socks.methods}:
     * the {@code CommaSeparatedValues} for acceptable authentication methods
     * to the SOCKS server (default value is
     * {@code NO_AUTHENTICATION_REQUIRED}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "NO_AUTHENTICATION_REQUIRED",
            description = "The comma separated list of acceptable "
                    + "authentication methods to the SOCKS server",
            name = "socksClient.socks.methods",
            syntax = "socksClient.socks.methods=SOCKS_METHODS",
            valueType = CommaSeparatedValues.class
    )
    public static final PropertySpec<CommaSeparatedValues> SOCKS_METHODS =
            PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
                    "socksClient.socks.methods",
                    CommaSeparatedValues.of("NO_AUTHENTICATION_REQUIRED")));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.socksDatagramSocket.clientInfoUnavailable}:
     * the {@code Boolean} value to indicate if the client information
     * expected to be used to send UDP datagrams (address and port) is
     * unavailable to be sent to the SOCKS server (an address and port of all
     * zeros is sent instead) (default value is {@code false}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the client "
                    + "information expected to be used to send UDP datagrams "
                    + "(address and port) is unavailable to be sent to the "
                    + "SOCKS server (an address and port of all zeros is "
                    + "sent instead)",
            name = "socksClient.socks.socksDatagramSocket.clientInfoUnavailable",
            syntax = "socksClient.socks.socksDatagramSocket.clientInfoUnavailable=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks.socksDatagramSocket.clientInfoUnavailable",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.socksHostResolver.resolveFromSocks5Server}:
     * the {@code Boolean} value to indicate if host names are to be
     * resolved from the SOCKS server (default value is {@code false}).
     */
    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if host names "
                    + "are to be resolved from the SOCKS server",
            name = "socksClient.socks.socksHostResolver.resolveFromSocks5Server",
            syntax = "socksClient.socks.socksHostResolver.resolveFromSocks5Server=true|false",
            valueType = Boolean.class
    )
    public static final PropertySpec<Boolean> SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER =
            PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
                    "socksClient.socks.socksHostResolver.resolveFromSocks5Server",
                    Boolean.FALSE));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.userpassauthmethod.password}: the
     * {@code EncryptedPassword} for the password to be used to access the
     * SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The password to be used to access the SOCKS server",
            name = "socksClient.socks.userpassauthmethod.password",
            syntax = "socksClient.socks.userpassauthmethod.password=PASSWORD",
            valueType = String.class
    )
    public static final PropertySpec<EncryptedPassword> SOCKS_USERPASSAUTHMETHOD_PASSWORD =
            PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
                    "socksClient.socks.userpassauthmethod.password",
                    EncryptedPassword.newInstance(new char[]{})));

    /**
     * {@code PropertySpec} constant for
     * {@code socksClient.socks.userpassauthmethod.username}: the username to be
     * used to access the SOCKS server.
     */
    @NameValuePairValueSpecDoc(
            description = "The username to be used to access the SOCKS server",
            name = "socksClient.socks.userpassauthmethod.username",
            syntax = "socksClient.socks.userpassauthmethod.username=USERNAME",
            valueType = String.class
    )
    public static final PropertySpec<String> SOCKS_USERPASSAUTHMETHOD_USERNAME =
            PROPERTY_SPECS.addThenGet(new StringPropertySpec(
                    "socksClient.socks.userpassauthmethod.username",
                    System.getProperty("user.name")));

    /**
     * Prevents the construction of unnecessary instances.
     */
    private SocksPropertySpecConstants() { 
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
