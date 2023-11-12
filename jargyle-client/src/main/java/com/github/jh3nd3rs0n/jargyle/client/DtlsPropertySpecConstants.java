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

public final class DtlsPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Boolean> DTLS_ENABLED = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.dtls.enabled",
					Boolean.FALSE));

	public static final PropertySpec<CommaSeparatedValues> DTLS_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
					"socksClient.dtls.enabledCipherSuites",
					CommaSeparatedValues.newInstance(new String[] { })));
	
	public static final PropertySpec<CommaSeparatedValues> DTLS_ENABLED_PROTOCOLS = 
			PROPERTY_SPECS.addThenGet(new CommaSeparatedValuesPropertySpec(
					"socksClient.dtls.enabledProtocols",
					CommaSeparatedValues.newInstance(new String[] { })));
	
	public static final PropertySpec<File> DTLS_KEY_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.dtls.keyStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.dtls.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> DTLS_KEY_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.dtls.keyStoreType",
					"PKCS12"));
	
	public static final PropertySpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
					"socksClient.dtls.maxPacketSize",
					PositiveInteger.newInstance(32768)));
	
	public static final PropertySpec<String> DTLS_PROTOCOL = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.dtls.protocol",
					"DTLSv1.2"));
	
	public static final PropertySpec<File> DTLS_TRUST_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.dtls.trustStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD =
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.dtls.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
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
