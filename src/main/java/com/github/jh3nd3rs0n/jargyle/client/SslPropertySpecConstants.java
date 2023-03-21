package com.github.jh3nd3rs0n.jargyle.client;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.EncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.FilePropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl.ValuesPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.text.Values;

public final class SslPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Boolean> SSL_ENABLED = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					"socksClient.ssl.enabled",
					Boolean.FALSE));

	public static final PropertySpec<Values> SSL_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPECS.addThenGet(new ValuesPropertySpec(
					"socksClient.ssl.enabledCipherSuites",
					Values.newInstance(new String[] { })));
	
	public static final PropertySpec<Values> SSL_ENABLED_PROTOCOLS = 
			PROPERTY_SPECS.addThenGet(new ValuesPropertySpec(
					"socksClient.ssl.enabledProtocols",
					Values.newInstance(new String[] { })));
	
	public static final PropertySpec<File> SSL_KEY_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.ssl.keyStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.ssl.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> SSL_KEY_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.ssl.keyStoreType",
					"PKCS12"));
	
	public static final PropertySpec<String> SSL_PROTOCOL = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					"socksClient.ssl.protocol",
					"TLSv1.2"));
	
	public static final PropertySpec<File> SSL_TRUST_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					"socksClient.ssl.trustStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					"socksClient.ssl.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
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
