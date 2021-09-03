package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import java.util.List;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.MethodsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.OidPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.ProtectionLevelsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.UserEncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.UsernamePropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Method;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;

public final class Socks5PropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIAUTH_MECHANISM_OID = 
			PROPERTY_SPECS.add(new OidPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.gssapiauth.mechanismOid",
					"1.2.840.113554.1.2.2"));

	public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			PROPERTY_SPECS.add(new BooleanPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.gssapiauth.necReferenceImpl",
					Boolean.FALSE));
	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			PROPERTY_SPECS.add(new ProtectionLevelsPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.gssapiauth.protectionLevels",
					ProtectionLevels.getDefault()));
	
	public static final PropertySpec<String> SOCKS5_GSSAPIAUTH_SERVICE_NAME = 
			PROPERTY_SPECS.add(new StringPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.gssapiauth.serviceName",
					null));
	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PROPERTY_SPECS.add(new MethodsPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.methods",
					Methods.newInstance(Method.NO_AUTHENTICATION_REQUIRED)));
	
	public static final PropertySpec<Boolean> SOCKS5_RESOLVE_USE_RESOLVE_COMMAND = 
			PROPERTY_SPECS.add(new BooleanPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.resolve.useResolveCommand",
					Boolean.FALSE));
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSAUTH_PASSWORD =
			PROPERTY_SPECS.add(new UserEncryptedPasswordPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.userpathauth.password",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> SOCKS5_USERPASSAUTH_USERNAME = 
			PROPERTY_SPECS.add(new UsernamePropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.socks5.userpathauth.username",
					System.getProperty("user.name")));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	private Socks5PropertySpecConstants() { }
}
