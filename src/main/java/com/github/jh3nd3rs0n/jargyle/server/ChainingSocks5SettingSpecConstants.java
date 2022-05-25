package com.github.jh3nd3rs0n.jargyle.server;

import java.util.List;
import java.util.Map;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.MethodsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.OidSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.ProtectionLevelsSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.UserEncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.UsernameSettingSpec;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class ChainingSocks5SettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The object ID for the GSS-API authentication mechanism to "
					+ "the other SOCKS5 server (default is 1.2.840.113554.1.2.2)", 
			usage = "chaining.socks5.gssapiauth.mechanismOid=SOCKS5_GSSAPIAUTH_MECHANISM_OID"
	)
	public static final SettingSpec<Oid> CHAINING_SOCKS5_GSSAPIAUTH_MECHANISM_OID = 
			SETTING_SPECS.addThenGet(new OidSettingSpec(
					"chaining.socks5.gssapiauth.mechanismOid", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_MECHANISM_OID.getDefaultProperty().getValue()));

	@HelpText(
			doc = "The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected should the other SOCKS5 server use the NEC "
					+ "reference implementation (default is false)", 
			usage = "chaining.socks5.gssapiauth.necReferenceImpl=true|false"
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.gssapiauth.necReferenceImpl", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The space separated list of acceptable protection levels "
					+ "after GSS-API authentication with the other SOCKS5 "
					+ "server (The first is preferred. The remaining are "
					+ "acceptable if the server does not accept the first.) "
					+ "(default is "
					+ "REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)", 
			usage = "chaining.socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[...]]"
	)
	public static final SettingSpec<ProtectionLevels> CHAINING_SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			SETTING_SPECS.addThenGet(new ProtectionLevelsSettingSpec(
					"chaining.socks5.gssapiauth.protectionLevels", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The GSS-API service name for the other SOCKS5 server", 
			usage = "chaining.socks5.gssapiauth.serviceName=SOCKS5_GSSAPIAUTH_SERVICE_NAME"
	)
	public static final SettingSpec<String> CHAINING_SOCKS5_GSSAPIAUTH_SERVICE_NAME = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.socks5.gssapiauth.serviceName", 
					Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTH_SERVICE_NAME.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The space separated list of acceptable authentication "
					+ "methods to the other SOCKS5 server (default is "
					+ "NO_AUTHENTICATION_REQUIRED)", 
			usage = "chaining.socks5.methods=[SOCKS5_METHOD1[ SOCKS5_METHOD2[...]]]"
	)
	public static final SettingSpec<Methods> CHAINING_SOCKS5_METHODS = 
			SETTING_SPECS.addThenGet(new MethodsSettingSpec(
					"chaining.socks5.methods", 
					Socks5PropertySpecConstants.SOCKS5_METHODS.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The boolean value to indicate that the RESOLVE command is "
					+ "to be used on the other SOCKS5 server for resolving "
					+ "host names (default is false)", 
			usage = "chaining.socks5.resolve.useResolveCommand=true|false"
	)	
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_RESOLVE_USE_RESOLVE_COMMAND = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.socks5.resolve.useResolveCommand", 
					Socks5PropertySpecConstants.SOCKS5_RESOLVE_USE_RESOLVE_COMMAND.getDefaultProperty().getValue()));

	@HelpText(
			doc = "The password to be used to access the other SOCKS5 server", 
			usage = "chaining.socks5.userpassauth.password=PASSWORD"
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_SOCKS5_USERPASSAUTH_PASSWORD =
			SETTING_SPECS.addThenGet(new UserEncryptedPasswordSettingSpec(
					"chaining.socks5.userpassauth.password",
					Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_PASSWORD.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The username to be used to access the other SOCKS5 server", 
			usage = "chaining.socks5.userpassauth.username=USERNAME"
	)
	public static final SettingSpec<String> CHAINING_SOCKS5_USERPASSAUTH_USERNAME =
			SETTING_SPECS.addThenGet(new UsernameSettingSpec(
					"chaining.socks5.userpassauth.username",
					Socks5PropertySpecConstants.SOCKS5_USERPASSAUTH_USERNAME.getDefaultProperty().getValue()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingSocks5SettingSpecConstants() { }
	
}
