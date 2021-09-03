package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import java.io.File;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.EncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.FilePropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.StringsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

public final class SslPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Boolean> SSL_ENABLED = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.enabled",
					Boolean.FALSE));

	public static final PropertySpec<Strings> SSL_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPECS.addThenGet(new StringsPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.enabledCipherSuites",
					Strings.newInstance(new String[] { })));
	
	public static final PropertySpec<Strings> SSL_ENABLED_PROTOCOLS = 
			PROPERTY_SPECS.addThenGet(new StringsPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.enabledProtocols",
					Strings.newInstance(new String[] { })));
	
	public static final PropertySpec<File> SSL_KEY_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.keyStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> SSL_KEY_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.keyStoreType",
					"PKCS12"));
	
	public static final PropertySpec<String> SSL_PROTOCOL = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.protocol",
					"TLSv1.2"));
	
	public static final PropertySpec<File> SSL_TRUST_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.trustStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> SSL_TRUST_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.ssl.trustStoreType",
					"PKCS12"));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	private SslPropertySpecConstants() { }
}
