package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.client.SocksPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;
import org.ietf.jgss.Oid;

import java.util.List;
import java.util.Map;

@NameValuePairValueSpecsDoc(
        description = "",
        name = "Chaining SOCKS Settings"
)
public final class ChainingSocksSettingSpecConstants {

    private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

    @NameValuePairValueSpecDoc(
            defaultValue = "1.2.840.113554.1.2.2",
            description = "The object ID for the GSS-API authentication "
                    + "mechanism to the other SOCKS server",
            name = "chaining.socks.gssapiauthmethod.mechanismOid",
            syntax = "chaining.socks.gssapiauthmethod.mechanismOid=OID",
            valueType = Oid.class
    )
    public static final SettingSpec<Oid> CHAINING_SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID =
            SETTING_SPECS.addThenGet(new OidSettingSpec(
                    "chaining.socks.gssapiauthmethod.mechanismOid",
                    SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the exchange of "
                    + "the GSS-API protection level negotiation must be "
                    + "unprotected should the other SOCKS server use the NEC "
                    + "reference implementation",
            name = "chaining.socks.gssapiauthmethod.necReferenceImpl",
            syntax = "chaining.socks.gssapiauthmethod.necReferenceImpl=true|false",
            valueType = Boolean.class
    )
    public static final SettingSpec<Boolean> CHAINING_SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL =
            SETTING_SPECS.addThenGet(new BooleanSettingSpec(
                    "chaining.socks.gssapiauthmethod.necReferenceImpl",
                    SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            defaultValue = "REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE",
            description = "The comma separated list of acceptable protection "
                    + "levels after GSS-API authentication with the other "
                    + "SOCKS server (The first is preferred. The remaining "
                    + "are acceptable if the server does not accept the "
                    + "first.)",
            name = "chaining.socks.gssapiauthmethod.protectionLevels",
            syntax = "chaining.socks.gssapiauthmethod.protectionLevels=SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS",
            valueType = CommaSeparatedValues.class
    )
    public static final SettingSpec<CommaSeparatedValues> CHAINING_SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS =
            SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
                    "chaining.socks.gssapiauthmethod.protectionLevels",
                    SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            description = "The GSS-API service name for the other SOCKS server",
            name = "chaining.socks.gssapiauthmethod.serviceName",
            syntax = "chaining.socks.gssapiauthmethod.serviceName=SERVICE_NAME",
            valueType = String.class
    )
    public static final SettingSpec<String> CHAINING_SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME =
            SETTING_SPECS.addThenGet(new StringSettingSpec(
                    "chaining.socks.gssapiauthmethod.serviceName",
                    SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            defaultValue = "true",
            description = "The suggested privacy (i.e. confidentiality) state "
                    + "for GSS-API messages sent after GSS-API authentication "
                    + "with the other SOCKS server (applicable if the "
                    + "negotiated protection level is SELECTIVE_INTEG_OR_CONF)",
            name = "chaining.socks.gssapiauthmethod.suggestedConf",
            syntax = "chaining.socks.gssapiauthmethod.suggestedConf=true|false",
            valueType = Boolean.class
    )
    public static final SettingSpec<Boolean> CHAINING_SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF =
            SETTING_SPECS.addThenGet(new BooleanSettingSpec(
                    "chaining.socks.gssapiauthmethod.suggestedConf",
                    SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            defaultValue = "0",
            description = "The suggested quality-of-protection "
                    + "(i.e. integrity) value for GSS-API messages sent after "
                    + "GSS-API authentication with the other SOCKS server "
                    + "(applicable if the negotiated protection level is "
                    + "SELECTIVE_INTEG_OR_CONF)",
            name = "chaining.socks.gssapiauthmethod.suggestedInteg",
            syntax = "chaining.socks.gssapiauthmethod.suggestedInteg=-2147483648-2147483647",
            valueType = Integer.class
    )
    public static final SettingSpec<Integer> CHAINING_SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG =
            SETTING_SPECS.addThenGet(new IntegerSettingSpec(
                    "chaining.socks.gssapiauthmethod.suggestedInteg",
                    SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            defaultValue = "NO_AUTHENTICATION_REQUIRED",
            description = "The comma separated list of acceptable "
                    + "authentication methods to the other SOCKS server",
            name = "chaining.socks.methods",
            syntax = "chaining.socks.methods=SOCKS_METHODS",
            valueType = CommaSeparatedValues.class
    )
    public static final SettingSpec<CommaSeparatedValues> CHAINING_SOCKS_METHODS =
            SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
                    "chaining.socks.methods",
                    SocksPropertySpecConstants.SOCKS_METHODS.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            defaultValue = "false",
            description = "The boolean value to indicate if the client "
                    + "information expected to be used to send UDP datagrams "
                    + "(address and port) is unavailable to be sent to the "
                    + "other SOCKS server (an address and port of all zeros "
                    + "is sent instead)",
            name = "chaining.socks.socksDatagramSocket.clientInfoUnavailable",
            syntax = "chaining.socks.socksDatagramSocket.clientInfoUnavailable=true|false",
            valueType = Boolean.class
    )
    public static final SettingSpec<Boolean> CHAINING_SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE =
            SETTING_SPECS.addThenGet(new BooleanSettingSpec(
                    "chaining.socks.socksDatagramSocket.clientInfoUnavailable",
                    SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            description = "The password to be used to access the other SOCKS "
                    + "server",
            name = "chaining.socks.userpassauthmethod.password",
            syntax = "chaining.socks.userpassauthmethod.password=PASSWORD",
            valueType = String.class
    )
    public static final SettingSpec<EncryptedPassword> CHAINING_SOCKS_USERPASSAUTHMETHOD_PASSWORD =
            SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
                    "chaining.socks.userpassauthmethod.password",
                    SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_PASSWORD.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
            description = "The username to be used to access the other SOCKS "
                    + "server",
            name = "chaining.socks.userpassauthmethod.username",
            syntax = "chaining.socks.userpassauthmethod.username=USERNAME",
            valueType = String.class
    )
    public static final SettingSpec<String> CHAINING_SOCKS_USERPASSAUTHMETHOD_USERNAME =
            SETTING_SPECS.addThenGet(new StringSettingSpec(
                    "chaining.socks.userpassauthmethod.username",
                    SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_USERNAME.getDefaultProperty().getValue()));

    public static List<SettingSpec<Object>> values() {
        return SETTING_SPECS.toList();
    }

    public static Map<String, SettingSpec<Object>> valuesMap() {
        return SETTING_SPECS.toMap();
    }
    
    private ChainingSocksSettingSpecConstants() { }
    
}
