package com.github.jh3nd3rs0n.jargyle.client;

import java.io.File;
import java.util.List;

import com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl.BooleanPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl.EncryptedPasswordPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl.FilePropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl.PositiveIntegerPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl.StringPropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.propertyspec.impl.StringsPropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.number.impl.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.text.Strings;

public final class DtlsPropertySpecConstants {

	private static final PropertySpecs PROPERTY_SPECS = new PropertySpecs();
	
	public static final PropertySpec<Boolean> DTLS_ENABLED = 
			PROPERTY_SPECS.addThenGet(new BooleanPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.enabled",
					Boolean.FALSE));

	public static final PropertySpec<Strings> DTLS_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPECS.addThenGet(new StringsPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.enabledCipherSuites",
					Strings.newInstance(new String[] { })));
	
	public static final PropertySpec<Strings> DTLS_ENABLED_PROTOCOLS = 
			PROPERTY_SPECS.addThenGet(new StringsPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.enabledProtocols",
					Strings.newInstance(new String[] { })));
	
	public static final PropertySpec<File> DTLS_KEY_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.keyStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> DTLS_KEY_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.keyStoreType",
					"PKCS12"));
	
	public static final PropertySpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			PROPERTY_SPECS.addThenGet(new PositiveIntegerPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.maxPacketSize",
					PositiveInteger.newInstance(32768)));
	
	public static final PropertySpec<String> DTLS_PROTOCOL = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.protocol",
					"DTLSv1.2"));
	
	public static final PropertySpec<File> DTLS_TRUST_STORE_FILE = 
			PROPERTY_SPECS.addThenGet(new FilePropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.trustStoreFile",
					null));
	
	public static final PropertySpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD =
			PROPERTY_SPECS.addThenGet(new EncryptedPasswordPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { })));
	
	public static final PropertySpec<String> DTLS_TRUST_STORE_TYPE = 
			PROPERTY_SPECS.addThenGet(new StringPropertySpec(
					NewPropertySpecPermission.INSTANCE,
					"socksClient.dtls.trustStoreType",
					"PKCS12"));
	
	public static List<PropertySpec<Object>> values() {
		return PROPERTY_SPECS.toList();
	}
	
	private DtlsPropertySpecConstants() { }
}
