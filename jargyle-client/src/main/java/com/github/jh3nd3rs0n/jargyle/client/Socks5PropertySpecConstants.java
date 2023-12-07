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
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.Methods;
import com.github.jh3nd3rs0n.jargyle.protocolbase.socks5.gssapimethod.ProtectionLevels;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SOCKS5 Properties"
)
public final class Socks5PropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that the client UDP "
					+ "address and port for sending UDP datagrams to the "
					+ "SOCKS5 server is unknown (default is false)",
			name = "socksClient.socks5.clientUdpAddressAndPortUnknown",
			syntax = "socksClient.socks5.clientUdpAddressAndPortUnknown=true|false",
			valueType = Boolean.class
	)	
	public static final PropertySpec<Boolean> SOCKS5_CLIENT_UDP_ADDRESS_AND_PORT_UNKNOWN =
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.clientUdpAddressAndPortUnknown",
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The object ID for the GSS-API authentication "
					+ "mechanism to the SOCKS5 server "
					+ "(default is 1.2.840.113554.1.2.2)",
			name = "socksClient.socks5.gssapimethod.mechanismOid",
			syntax = "socksClient.socks5.gssapimethod.mechanismOid=OID",
			valueType = Oid.class
	)	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIMETHOD_MECHANISM_OID = 
			PROPERTY_SPECS.addThenGet(new OidPropertySpec(
					"socksClient.socks5.gssapimethod.mechanismOid",
					"1.2.840.113554.1.2.2"));

	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if the exchange of "
					+ "the GSS-API protection level negotiation must be "
					+ "unprotected should the SOCKS5 server use the NEC "
					+ "reference implementation (default is false)",
			name = "socksClient.socks5.gssapimethod.necReferenceImpl",
			syntax = "socksClient.socks5.gssapimethod.necReferenceImpl=true|false",
			valueType = Boolean.class
	)	
	public static final PropertySpec<Boolean> SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.gssapimethod.necReferenceImpl",
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protection "
					+ "levels after GSS-API authentication with the SOCKS5 "
					+ "server (The first is preferred. The remaining are "
					+ "acceptable if the server does not accept the first.) "
					+ "(default is REQUIRED_INTEG_AND_CONF,REQUIRED_INTEG,NONE)",
			name = "socksClient.socks5.gssapimethod.protectionLevels",
			syntax = "socksClient.socks5.gssapimethod.protectionLevels=SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS",
			valueType = ProtectionLevels.class
	)	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS = 
			PROPERTY_SPECS.addThenGet(new Socks5GssapiMethodProtectionLevelsPropertySpec(
					"socksClient.socks5.gssapimethod.protectionLevels",
					ProtectionLevels.getDefault()));
	
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
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable "
					+ "authentication methods to the SOCKS5 server (default is "
					+ "NO_AUTHENTICATION_REQUIRED)",
			name = "socksClient.socks5.methods",
			syntax = "socksClient.socks5.methods=SOCKS5_METHODS",
			valueType = Methods.class
	)	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PROPERTY_SPECS.addThenGet(new Socks5MethodsPropertySpec(
					"socksClient.socks5.methods",
					Methods.getDefault()));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that the RESOLVE "
					+ "command is to be used on the SOCKS5 server for "
					+ "resolving host names (default is false)",
			name = "socksClient.socks5.useResolveCommand",
			syntax = "socksClient.socks5.useResolveCommand=true|false",
			valueType = Boolean.class
	)
	public static final PropertySpec<Boolean> SOCKS5_USE_RESOLVE_COMMAND = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.useResolveCommand",
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The password to be used to access the SOCKS5 server",
			name = "socksClient.socks5.userpassmethod.password",
			syntax = "socksClient.socks5.userpassmethod.password=PASSWORD",
			valueType = String.class
	)	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSMETHOD_PASSWORD =
			PROPERTY_SPECS.addThenGet(new Socks5UserpassMethodEncryptedPasswordPropertySpec(
					"socksClient.socks5.userpassmethod.password",
					EncryptedPassword.newInstance(new char[] { })));
	
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
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	public static Map<String, PropertySpec<Object>> valuesMap() {
		return PROPERTY_SPECS.toMap();
	}
	
	private Socks5PropertySpecConstants() { }
}
