package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.OidPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5GssapiauthProtectionLevelsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5MethodsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5UserpassauthEncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5UserpassauthUsernamePropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapiauth.ProtectionLevels;

public final class Socks5PropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Boolean> SOCKS5_CLIENT_UDP_ADDRESS_AND_PORT_UNKNOWN =
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.clientUdpAddressAndPortUnknown",
					Boolean.FALSE));
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIAUTH_MECHANISM_OID = 
			PROPERTY_SPECS.addThenGet(new OidPropertySpec(
					"socksClient.socks5.gssapiauth.mechanismOid",
					"1.2.840.113554.1.2.2"));

	public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.gssapiauth.necReferenceImpl",
					Boolean.FALSE));
	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			PROPERTY_SPECS.addThenGet(new Socks5GssapiauthProtectionLevelsPropertySpec(
					"socksClient.socks5.gssapiauth.protectionLevels",
					ProtectionLevels.getDefault()));
	
	public static final PropertySpec<String> SOCKS5_GSSAPIAUTH_SERVICE_NAME = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.socks5.gssapiauth.serviceName",
					null));
	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PROPERTY_SPECS.addThenGet(new Socks5MethodsPropertySpec(
					"socksClient.socks5.methods",
					Methods.getDefault()));
	
	public static final PropertySpec<Boolean> SOCKS5_USE_RESOLVE_COMMAND = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.useResolveCommand",
					Boolean.FALSE));
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSAUTH_PASSWORD =
			PROPERTY_SPECS.addThenGet(new Socks5UserpassauthEncryptedPasswordPropertySpec(
					"socksClient.socks5.userpathauth.password",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> SOCKS5_USERPASSAUTH_USERNAME = 
			PROPERTY_SPECS.addThenGet(new Socks5UserpassauthUsernamePropertySpec(
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
