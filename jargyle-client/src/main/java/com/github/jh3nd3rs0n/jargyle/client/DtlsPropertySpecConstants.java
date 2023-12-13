package com.github.jh3nd3rs0n.jargyle.client;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.CommaSeparatedValuesPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.EncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.FilePropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.PositiveIntegerPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "DTLS Properties"
)
public final class DtlsPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();

	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if DTLS connections "
					+ "to the SOCKS server are enabled (default is false)",
			name = "socksClient.dtls.enabled",
			syntax = "socksClient.dtls.enabled=true|false",
			valueType = Boolean.class
	)	
	public static final PropertySpec<Boolean> DTLS_ENABLED = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.dtls.enabled",
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable cipher "
					+ "suites enabled for DTLS connections to the SOCKS server",
			name = "socksClient.dtls.enabledCipherSuites",
			syntax = "socksClient.dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)
	public static final PropertySpec<CommaSeparatedValues> DTLS_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
					"socksClient.dtls.enabledCipherSuites",
					CommaSeparatedValues.of(new String[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protocol "
					+ "versions enabled for DTLS connections to the SOCKS "
					+ "server",
			name = "socksClient.dtls.enabledProtocols",
			syntax = "socksClient.dtls.enabledProtocols=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)	
	public static final PropertySpec<CommaSeparatedValues> DTLS_ENABLED_PROTOCOLS = 
			PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
					"socksClient.dtls.enabledProtocols",
					CommaSeparatedValues.of(new String[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The key store file for the DTLS connections to the "
					+ "SOCKS server",
			name = "socksClient.dtls.keyStoreFile",
			syntax = "socksClient.dtls.keyStoreFile=FILE",
			valueType = File.class
	)	
	public static final PropertySpec<File> DTLS_KEY_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.dtls.keyStoreFile",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The password for the key store for the DTLS "
					+ "connections to the SOCKS server",
			name = "socksClient.dtls.keyStorePassword",
			syntax = "socksClient.dtls.keyStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final PropertySpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.dtls.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The type of key store file for the DTLS connections "
					+ "to the SOCKS server (default is PKCS12)",
			name = "socksClient.dtls.keyStoreType",
			syntax = "socksClient.dtls.keyStoreType=TYPE",
			valueType = String.class
	)	
	public static final PropertySpec<String> DTLS_KEY_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.dtls.keyStoreType",
					"PKCS12"));
	
	@NameValuePairValueSpecDoc(
			description = "The maximum packet size for the DTLS connections to "
					+ "the SOCKS server (default is 32768)",
			name = "socksClient.dtls.maxPacketSize",
			syntax = "socksClient.dtls.maxPacketSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final PropertySpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
					"socksClient.dtls.maxPacketSize",
					PositiveInteger.newInstance(32768)));
	
	@NameValuePairValueSpecDoc(
			description = "The protocol version for the DTLS connections to "
					+ "the SOCKS server (default is DTLSv1.2)",
			name = "socksClient.dtls.protocol",
			syntax = "socksClient.dtls.protocol=PROTOCOL",
			valueType = String.class
	)	
	public static final PropertySpec<String> DTLS_PROTOCOL = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.dtls.protocol",
					"DTLSv1.2"));
	
	@NameValuePairValueSpecDoc(
			description = "The trust store file for the DTLS connections to "
					+ "the SOCKS server",
			name = "socksClient.dtls.trustStoreFile",
			syntax = "socksClient.dtls.trustStoreFile=FILE",
			valueType = File.class
	)	
	public static final PropertySpec<File> DTLS_TRUST_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.dtls.trustStoreFile",
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The password for the trust store for the DTLS "
					+ "connections to the SOCKS server",
			name = "socksClient.dtls.trustStorePassword",
			syntax = "socksClient.dtls.trustStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final PropertySpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD =
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.dtls.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The type of trust store file for the DTLS "
					+ "connections to the SOCKS server (default is PKCS12)",
			name = "socksClient.dtls.trustStoreType",
			syntax = "socksClient.dtls.trustStoreType=TYPE",
			valueType = String.class
	)	
	public static final PropertySpec<String> DTLS_TRUST_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.dtls.trustStoreType",
					"PKCS12"));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	public static Map<String, PropertySpec<Object>> valuesMap() {
		return PROPERTY_SPECS.toMap();
	}
	
	private DtlsPropertySpecConstants() { }
}
