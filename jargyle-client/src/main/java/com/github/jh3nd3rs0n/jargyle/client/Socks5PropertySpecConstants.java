package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.OidPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5GssapiMethodProtectionLevelsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5MethodsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5UserpassMethodEncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.Socks5UserpassMethodUsernamePropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;

public final class Socks5PropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Boolean> SOCKS5_CLIENT_UDP_ADDRESS_AND_PORT_UNKNOWN =
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.clientUdpAddressAndPortUnknown",
					Boolean.FALSE));
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIMETHOD_MECHANISM_OID = 
			PROPERTY_SPECS.addThenGet(new OidPropertySpec(
					"socksClient.socks5.gssapimethod.mechanismOid",
					"1.2.840.113554.1.2.2"));

	public static final PropertySpec<Boolean> SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.gssapimethod.necReferenceImpl",
					Boolean.FALSE));
	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS = 
			PROPERTY_SPECS.addThenGet(new Socks5GssapiMethodProtectionLevelsPropertySpec(
					"socksClient.socks5.gssapimethod.protectionLevels",
					ProtectionLevels.getDefault()));
	
	public static final PropertySpec<String> SOCKS5_GSSAPIMETHOD_SERVICE_NAME = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.socks5.gssapimethod.serviceName",
					null));
	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PROPERTY_SPECS.addThenGet(new Socks5MethodsPropertySpec(
					"socksClient.socks5.methods",
					Methods.getDefault()));
	
	public static final PropertySpec<Boolean> SOCKS5_USE_RESOLVE_COMMAND = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.useResolveCommand",
					Boolean.FALSE));
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSMETHOD_PASSWORD =
			PROPERTY_SPECS.addThenGet(new Socks5UserpassMethodEncryptedPasswordPropertySpec(
					"socksClient.socks5.userpathauth.password",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> SOCKS5_USERPASSMETHOD_USERNAME = 
			PROPERTY_SPECS.addThenGet(new Socks5UserpassMethodUsernamePropertySpec(
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
