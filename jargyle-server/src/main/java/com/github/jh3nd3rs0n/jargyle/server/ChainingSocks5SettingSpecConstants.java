package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;
import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauthmethod.ProtectionLevels;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "Chaining SOCKS5 Settings"
)
public final class ChainingSocks5SettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();

	@NameValuePairValueSpecDoc(
			description = "The object ID for the GSS-API authentication "
					+ "mechanism to the other SOCKS5 server",
			name = "chaining.socks5.gssapiauthmethod.mechanismOid",
			syntax = "chaining.socks5.gssapiauthmethod.mechanismOid=OID",
			valueType = Oid.class
	)
	public static final SettingSpec<Oid> CHAINING_SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID = 
			SETTING_SPECS.addThenGet(new OidSettingSpec(
					"chaining.socks5.gssapiauthmethod.mechanismOid", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the exchange of "
					+ "the GSS-API protection level negotiation must be "
					+ "unprotected should the other SOCKS5 server use the NEC "
					+ "reference implementation",
			name = "chaining.socks5.gssapiauthmethod.necReferenceImpl",
			syntax = "chaining.socks5.gssapiauthmethod.necReferenceImpl=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.gssapiauthmethod.necReferenceImpl", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protection "
					+ "levels after GSS-API authentication with the other "
					+ "SOCKS5 server (The first is preferred. The remaining "
					+ "are acceptable if the server does not accept the first.)",
			name = "chaining.socks5.gssapiauthmethod.protectionLevels",
			syntax = "chaining.socks5.gssapiauthmethod.protectionLevels=SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS",
			valueType = ProtectionLevels.class
	)
	public static final SettingSpec<ProtectionLevels> CHAINING_SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS = 
			SETTING_SPECS.addThenGet(new Socks5GssapiAuthMethodProtectionLevelsSettingSpec(
					"chaining.socks5.gssapiauthmethod.protectionLevels", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The GSS-API service name for the other SOCKS5 server",
			name = "chaining.socks5.gssapiauthmethod.serviceName",
			syntax = "chaining.socks5.gssapiauthmethod.serviceName=SERVICE_NAME",
			valueType = String.class
	)
	public static final SettingSpec<String> CHAINING_SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.socks5.gssapiauthmethod.serviceName", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The suggested privacy (i.e. confidentiality) state "
					+ "for GSS-API messages sent after GSS-API authentication "
					+ "with the other SOCKS5 server (applicable if the "
					+ "negotiated protection level is SELECTIVE_INTEG_OR_CONF)",
			name = "chaining.socks5.gssapiauthmethod.suggestedConf",
			syntax = "chaining.socks5.gssapiauthmethod.suggestedConf=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF =
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.gssapiauthmethod.suggestedConf",
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The suggested quality-of-protection "
					+ "(i.e. integrity) value for GSS-API messages sent after "
					+ "GSS-API authentication with the other SOCKS5 server "
					+ "(applicable if the negotiated protection level is "
					+ "SELECTIVE_INTEG_OR_CONF)",
			name = "chaining.socks5.gssapiauthmethod.suggestedInteg",
			syntax = "chaining.socks5.gssapiauthmethod.suggestedInteg=-2147483648-2147483647",
			valueType = Integer.class
	)
	public static final SettingSpec<Integer> CHAINING_SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG =
			SETTING_SPECS.addThenGet(new IntegerSettingSpec(
					"chaining.socks5.gssapiauthmethod.suggestedInteg",
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable "
					+ "authentication methods to the other SOCKS5 server",
			name = "chaining.socks5.methods",
			syntax = "chaining.socks5.methods=SOCKS5_METHODS",
			valueType = Methods.class
	)
	public static final SettingSpec<Methods> CHAINING_SOCKS5_METHODS = 
			SETTING_SPECS.addThenGet(new Socks5MethodsSettingSpec(
					"chaining.socks5.methods", 
					Socks5PropertySpecConstants.SOCKS5_METHODS.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the client "
					+ "information expected to be used to send UDP datagrams "
					+ "(address and port) is unavailable to be sent to the "
					+ "other SOCKS5 server (an address and port of all zeros "
					+ "is sent instead)",
			name = "chaining.socks5.socks5DatagramSocket.clientInfoUnavailable",
			syntax = "chaining.socks5.socks5DatagramSocket.clientInfoUnavailable=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE =
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.socks5DatagramSocket.clientInfoUnavailable",
					Socks5PropertySpecConstants.SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE.getDefaultProperty().getValue()));

    @NameValuePairValueSpecDoc(
			description = "The password to be used to access the other SOCKS5 "
					+ "server",
			name = "chaining.socks5.userpassauthmethod.password",
			syntax = "chaining.socks5.userpassauthmethod.password=PASSWORD",
			valueType = String.class
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_SOCKS5_USERPASSAUTHMETHOD_PASSWORD =
			SETTING_SPECS.addThenGet(new Socks5UserpassAuthMethodEncryptedPasswordSettingSpec(
					"chaining.socks5.userpassauthmethod.password",
					Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The username to be used to access the other SOCKS5 "
					+ "server",
			name = "chaining.socks5.userpassauthmethod.username",
			syntax = "chaining.socks5.userpassauthmethod.username=USERNAME",
			valueType = String.class
	)
	public static final SettingSpec<String> CHAINING_SOCKS5_USERPASSAUTHMETHOD_USERNAME =
			SETTING_SPECS.addThenGet(new Socks5UserpassAuthMethodUsernameSettingSpec(
					"chaining.socks5.userpassauthmethod.username",
					Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME.getDefaultProperty().getValue()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingSocks5SettingSpecConstants() { }
	
}
