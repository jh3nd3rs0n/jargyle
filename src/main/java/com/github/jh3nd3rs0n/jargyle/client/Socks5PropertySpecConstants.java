package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.MethodsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.OidPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.ProtectionLevelsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.UserEncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.UsernamePropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.transport.socks5.gssapiauth.ProtectionLevels;

public final class Socks5PropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIAUTH_MECHANISM_OID = 
			PROPERTY_SPECS.addThenGet(new OidPropertySpec(
					"socksClient.socks5.gssapiauth.mechanismOid",
					"1.2.840.113554.1.2.2"));

	public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.gssapiauth.necReferenceImpl",
					Boolean.FALSE));
	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			PROPERTY_SPECS.addThenGet(new ProtectionLevelsPropertySpec(
					"socksClient.socks5.gssapiauth.protectionLevels",
					ProtectionLevels.getDefault()));
	
	public static final PropertySpec<String> SOCKS5_GSSAPIAUTH_SERVICE_NAME = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.socks5.gssapiauth.serviceName",
					null));
	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PROPERTY_SPECS.addThenGet(new MethodsPropertySpec(
					"socksClient.socks5.methods",
					Methods.getDefault()));
	
	public static final PropertySpec<Boolean> SOCKS5_RESOLVE_USE_RESOLVE_COMMAND = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.resolve.useResolveCommand",
					Boolean.FALSE));
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSAUTH_PASSWORD =
			PROPERTY_SPECS.addThenGet(new UserEncryptedPasswordPropertySpec(
					"socksClient.socks5.userpathauth.password",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> SOCKS5_USERPASSAUTH_USERNAME = 
			PROPERTY_SPECS.addThenGet(new UsernamePropertySpec(
					"socksClient.socks5.userpathauth.username",
					System.getProperty("user.name")));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	public static Map<String, PropertySpec<Object>> valuesMap() {
		return PROPERTY_SPECS.toMap();
	}
	
	private Socks5PropertySpecConstants() { }
}
