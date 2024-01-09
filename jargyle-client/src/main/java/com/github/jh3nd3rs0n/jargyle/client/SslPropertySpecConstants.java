package com.github.jh3nd3rs0n.jargyle.client;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.CommaSeparatedValuesPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.EncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.FilePropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SSL/TLS Properties"
)
public final class SslPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if SSL/TLS connections "
					+ "to the SOCKS server are enabled (default is false)",
			name = "socksClient.ssl.enabled",
			syntax = "socksClient.ssl.enabled=true|false",
			valueType = Boolean.class
	)
	public static final PropertySpec<Boolean> SSL_ENABLED = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.ssl.enabled",
					Boolean.FALSE));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable cipher "
					+ "suites enabled for SSL/TLS connections to the SOCKS "
					+ "server",
			name = "socksClient.ssl.enabledCipherSuites",
			syntax = "socksClient.ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)
	public static final PropertySpec<CommaSeparatedValues> SSL_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
					"socksClient.ssl.enabledCipherSuites",
					CommaSeparatedValues.newInstance(new String[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protocol "
					+ "versions enabled for SSL/TLS connections to the SOCKS "
					+ "server",
			name = "socksClient.ssl.enabledProtocols",
			syntax = "socksClient.ssl.enabledProtocols=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)
	public static final PropertySpec<CommaSeparatedValues> SSL_ENABLED_PROTOCOLS = 
			PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
					"socksClient.ssl.enabledProtocols",
					CommaSeparatedValues.newInstance(new String[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The key store file for the SSL/TLS connections to "
					+ "the SOCKS server",
			name = "socksClient.ssl.keyStoreFile",
			syntax = "socksClient.ssl.keyStoreFile=FILE",
			valueType = File.class
	)	
	public static final PropertySpec<File> SSL_KEY_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.ssl.keyStoreFile",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The password for the key store for the SSL/TLS "
					+ "connections to the SOCKS server",
			name = "socksClient.ssl.keyStorePassword",
			syntax = "socksClient.ssl.keyStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.ssl.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The type of key store file for the SSL/TLS "
					+ "connections to the SOCKS server (default is PKCS12)",
			name = "socksClient.ssl.keyStoreType",
			syntax = "socksClient.ssl.keyStoreType=TYPE",
			valueType = String.class
	)
	public static final PropertySpec<String> SSL_KEY_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.ssl.keyStoreType",
					"PKCS12"));
	
	@NameValuePairValueSpecDoc(
			description = "The protocol version for the SSL/TLS connections to "
					+ "the SOCKS server (default is TLSv1.2)",
			name = "socksClient.ssl.protocol",
			syntax = "socksClient.ssl.protocol=PROTOCOL",
			valueType = String.class
	)
	public static final PropertySpec<String> SSL_PROTOCOL = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.ssl.protocol",
					"TLSv1.2"));
	
	@NameValuePairValueSpecDoc(
			description = "The trust store file for the SSL/TLS connections to "
					+ "the SOCKS server",
			name = "socksClient.ssl.trustStoreFile",
			syntax = "socksClient.ssl.trustStoreFile=FILE",
			valueType = File.class
	)
	public static final PropertySpec<File> SSL_TRUST_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.ssl.trustStoreFile",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The password for the trust store for the SSL/TLS "
					+ "connections to the SOCKS server",
			name = "socksClient.ssl.trustStorePassword",
			syntax = "socksClient.ssl.trustStorePassword=PASSWORD",
			valueType = String.class
	)
	public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.ssl.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The type of trust store file for the SSL/TLS "
					+ "connections to the SOCKS server (default is PKCS12)",
			name = "socksClient.ssl.trustStoreType",
			syntax = "socksClient.ssl.trustStoreType=TYPE",
			valueType = String.class
	)	
	public static final PropertySpec<String> SSL_TRUST_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.ssl.trustStoreType",
					"PKCS12"));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	public static Map<String, PropertySpec<Object>> valuesMap() {
		return PROPERTY_SPECS.toMap();
	}
	
	private SslPropertySpecConstants() { }
}
