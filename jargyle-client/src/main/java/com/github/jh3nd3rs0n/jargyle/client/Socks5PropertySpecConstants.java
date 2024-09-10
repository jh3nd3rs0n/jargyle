package com.github.jh3nd3rs0n.jargyle.client;

import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.*;
import org.ietf.jgss.Oid;

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
			description = "The suggested privacy (i.e. confidentiality) state "
					+ "for GSS-API messages sent after GSS-API authentication "
					+ "with the SOCKS5 server (applicable if the negotiated "
					+ "protection level is SELECTIVE_INTEG_OR_CONF) (default "
					+ "is true)",
			name = "socksClient.socks5.gssapimethod.suggestedConf",
			syntax = "socksClient.socks5.gssapimethod.suggestedConf=true|false",
			valueType = Boolean.class
	)
	public static final PropertySpec<Boolean> SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF =
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.gssapimethod.suggestedConf",
					Boolean.TRUE));

	@NameValuePairValueSpecDoc(
			description = "The suggested quality-of-protection (i.e. "
					+ "integrity) value for GSS-API messages sent after "
					+ "GSS-API authentication with the SOCKS5 server "
					+ "(applicable if the negotiated protection level is "
					+ "SELECTIVE_INTEG_OR_CONF) (default is 0)",
			name = "socksClient.socks5.gssapimethod.suggestedInteg",
			syntax = "socksClient.socks5.gssapimethod.suggestedInteg=-2147483648-2147483647",
			valueType = Integer.class
	)
	public static final PropertySpec<Integer> SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG =
			PROPERTY_SPECS.addThenGet(new IntegerPropertySpec(
					"socksClient.socks5.gssapimethod.suggestedInteg",
					Integer.valueOf(0)));

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
			description = "The boolean value to indicate that the actual "
					+ "address and port for sending UDP datagrams to the "
					+ "SOCKS5 server is unknown (default is false)",
			name = "socksClient.socks5.socks5DatagramSocket.actualAddressAndPortUnknown",
			syntax = "socksClient.socks5.socks5DatagramSocket.actualAddressAndPortUnknown=true|false",
			valueType = Boolean.class
	)
	public static final PropertySpec<Boolean> SOCKS5_SOCKS5_DATAGRAM_SOCKET_ACTUAL_ADDRESS_AND_PORT_UNKNOWN =
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.socks5DatagramSocket.actualAddressAndPortUnknown",
					Boolean.FALSE));

	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that host names "
					+ "are to be resolved from the SOCKS5 server "
					+ "(default is false)",
			name = "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server",
			syntax = "socksClient.socks5.socks5HostResolver.resolveFromSocks5Server=true|false",
			valueType = Boolean.class
	)
	public static final PropertySpec<Boolean> SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER =
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.socks5.socks5HostResolver.resolveFromSocks5Server",
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
