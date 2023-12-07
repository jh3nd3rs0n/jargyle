package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.OidSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.Socks5GssapiMethodProtectionLevelsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.Socks5MethodsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.Socks5UserpassMethodEncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.Socks5UserpassMethodUsernameSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "Chaining SOCKS5 Settings"
)
public final class ChainingSocks5SettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that the client UDP "
					+ "address and port for sending UDP datagrams to the other "
					+ "SOCKS5 server is unknown (default is false)",
			name = "chaining.socks5.clientUdpAddressAndPortUnknown",
			syntax = "chaining.socks5.clientUdpAddressAndPortUnknown=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_CLIENT_UDP_ADDRESS_AND_PORT_UNKNOWN =
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.clientUdpAddressAndPortUnknown",
					Socks5PropertySpecConstants.SOCKS5_CLIENT_UDP_ADDRESS_AND_PORT_UNKNOWN.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The object ID for the GSS-API authentication "
					+ "mechanism to the other SOCKS5 server "
					+ "(default is 1.2.840.113554.1.2.2)",
			name = "chaining.socks5.gssapimethod.mechanismOid",
			syntax = "chaining.socks5.gssapimethod.mechanismOid=OID",
			valueType = Oid.class
	)
	public static final SettingSpec<Oid> CHAINING_SOCKS5_GSSAPIMETHOD_MECHANISM_OID = 
			SETTING_SPECS.addThenGet(new OidSettingSpec(
					"chaining.socks5.gssapimethod.mechanismOid", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_MECHANISM_OID.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the exchange of "
					+ "the GSS-API protection level negotiation must be "
					+ "unprotected should the other SOCKS5 server use the NEC "
					+ "reference implementation (default is false)",
			name = "chaining.socks5.gssapimethod.necReferenceImpl",
			syntax = "chaining.socks5.gssapimethod.necReferenceImpl=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.gssapimethod.necReferenceImpl", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protection "
					+ "levels after GSS-API authentication with the other "
					+ "SOCKS5 server (The first is preferred. The remaining "
					+ "are acceptable if the server does not accept the first.) "
					+ "(default is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)",
			name = "chaining.socks5.gssapimethod.protectionLevels",
			syntax = "chaining.socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS",
			valueType = ProtectionLevels.class
	)
	public static final SettingSpec<ProtectionLevels> CHAINING_SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS = 
			SETTING_SPECS.addThenGet(new Socks5GssapiMethodProtectionLevelsSettingSpec(
					"chaining.socks5.gssapimethod.protectionLevels", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The GSS-API service name for the other SOCKS5 server",
			name = "chaining.socks5.gssapimethod.serviceName",
			syntax = "chaining.socks5.gssapimethod.serviceName=SERVICE_NAME",
			valueType = String.class
	)
	public static final SettingSpec<String> CHAINING_SOCKS5_GSSAPIMETHOD_SERVICE_NAME = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.socks5.gssapimethod.serviceName", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable "
					+ "authentication methods to the other SOCKS5 server "
					+ "(default is NO_AUTHENTICATION_REQUIRED)",
			name = "chaining.socks5.methods",
			syntax = "chaining.socks5.methods=SOCKS5_METHODS",
			valueType = Methods.class
	)
	public static final SettingSpec<Methods> CHAINING_SOCKS5_METHODS = 
			SETTING_SPECS.addThenGet(new Socks5MethodsSettingSpec(
					"chaining.socks5.methods", 
					Socks5PropertySpecConstants.SOCKS5_METHODS.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that the RESOLVE "
					+ "command is to be used on the other SOCKS5 server for "
					+ "resolving host names (default is false)",
			name = "chaining.socks5.useResolveCommand",
			syntax = "chaining.socks5.useResolveCommand=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_USE_RESOLVE_COMMAND = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.useResolveCommand", 
					Socks5PropertySpecConstants.SOCKS5_USE_RESOLVE_COMMAND.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The password to be used to access the other SOCKS5 "
					+ "server",
			name = "chaining.socks5.userpassmethod.password",
			syntax = "chaining.socks5.userpassmethod.password=PASSWORD",
			valueType = String.class
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD =
			SETTING_SPECS.addThenGet(new Socks5UserpassMethodEncryptedPasswordSettingSpec(
					"chaining.socks5.userpassmethod.password",
					Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The username to be used to access the other SOCKS5 "
					+ "server",
			name = "chaining.socks5.userpassmethod.username",
			syntax = "chaining.socks5.userpassmethod.username=USERNAME",
			valueType = String.class
	)
	public static final SettingSpec<String> CHAINING_SOCKS5_USERPASSMETHOD_USERNAME =
			SETTING_SPECS.addThenGet(new Socks5UserpassMethodUsernameSettingSpec(
					"chaining.socks5.userpassmethod.username",
					Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME.getDefaultProperty().getValue()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingSocks5SettingSpecConstants() { }
	
}
