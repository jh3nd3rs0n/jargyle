package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.*;

import java.io.File;
import java.util.List;
import java.util.Map;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "DTLS Settings"
)
public final class DtlsSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			defaultValue = "false",
			description = "The boolean value to indicate if DTLS connections "
					+ "to the SOCKS server are enabled",
			name = "dtls.enabled",
			syntax = "dtls.enabled=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> DTLS_ENABLED = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"dtls.enabled", 
					Boolean.FALSE));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable cipher "
					+ "suites enabled for DTLS connections to the SOCKS server",
			name = "dtls.enabledCipherSuites",
			syntax = "dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)	
	public static final SettingSpec<CommaSeparatedValues> DTLS_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"dtls.enabledCipherSuites", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protocol "
					+ "versions enabled for DTLS connections to the SOCKS "
					+ "server",
			name = "dtls.enabledProtocols",
			syntax = "dtls.enabledProtocols=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)	
	public static final SettingSpec<CommaSeparatedValues> DTLS_ENABLED_PROTOCOLS = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"dtls.enabledProtocols", 
					null));

	public static final SettingSpec<Bytes> DTLS_KEY_STORE_BYTES =
			SETTING_SPECS.addThenGet(new BytesSettingSpec(
					"dtls.keyStoreBytes",
					null));

	@NameValuePairValueSpecDoc(
			description = "The key store file for the DTLS connections to the "
					+ "SOCKS server",
			name = "dtls.keyStoreFile",
			syntax = "dtls.keyStoreFile=FILE",
			valueType = File.class
	)	
	public static final SettingSpec<File> DTLS_KEY_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"dtls.keyStoreFile", 
					null));

	@NameValuePairValueSpecDoc(
			description = "The password for the key store for the DTLS "
					+ "connections to the SOCKS server",
			name = "dtls.keyStorePassword",
			syntax = "dtls.keyStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final SettingSpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"dtls.keyStorePassword", 
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "PKCS12",
			description = "The type of key store for the DTLS connections to "
					+ "the SOCKS server",
			name = "dtls.keyStoreType",
			syntax = "dtls.keyStoreType=TYPE",
			valueType = String.class
	)	
	public static final SettingSpec<String> DTLS_KEY_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.keyStoreType", 
					"PKCS12"));

	@NameValuePairValueSpecDoc(
			defaultValue = "DTLSv1.2",
			description = "The protocol version for the DTLS connections to the "
					+ "SOCKS server",
			name = "dtls.protocol",
			syntax = "dtls.protocol=PROTOCOL",
			valueType = String.class
	)	
	public static final SettingSpec<String> DTLS_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.protocol", 
					"DTLSv1.2"));

	@NameValuePairValueSpecDoc(
			description = "The buffer size for receiving DTLS wrapped "
					+ "datagrams for the DTLS connections to the SOCKS server",
			name = "dtls.wrappedReceiveBufferSize",
			syntax = "dtls.wrappedReceiveBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> DTLS_WRAPPED_RECEIVE_BUFFER_SIZE =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"dtls.wrappedReceiveBufferSize",
					null));

	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private DtlsSettingSpecConstants() { }
	
}
