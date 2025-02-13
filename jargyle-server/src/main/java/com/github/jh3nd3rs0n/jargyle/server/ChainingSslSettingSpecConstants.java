package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.bytes.Bytes;
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
		name = "Chaining SSL/TLS Settings"
)
public final class ChainingSslSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			defaultValue = "false",
			description = "The boolean value to indicate if SSL/TLS connections "
					+ "to the other SOCKS server are enabled",
			name = "chaining.ssl.enabled",
			syntax = "chaining.ssl.enabled=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_SSL_ENABLED = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.ssl.enabled", 
					SslPropertySpecConstants.SSL_ENABLED.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable cipher suites "
					+ "enabled for SSL/TLS connections to the other SOCKS "
					+ "server",
			name = "chaining.ssl.enabledCipherSuites",
			syntax = "chaining.ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)
	public static final SettingSpec<CommaSeparatedValues> CHAINING_SSL_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"chaining.ssl.enabledCipherSuites", 
					SslPropertySpecConstants.SSL_ENABLED_CIPHER_SUITES.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protocol "
					+ "versions enabled for SSL/TLS connections to the other "
					+ "SOCKS server",
			name = "chaining.ssl.enabledProtocols",
			syntax = "chaining.ssl.enabledProtocols=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)	
	public static final SettingSpec<CommaSeparatedValues> CHAINING_SSL_ENABLED_PROTOCOLS = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"chaining.ssl.enabledProtocols", 
					SslPropertySpecConstants.SSL_ENABLED_PROTOCOLS.getDefaultProperty().getValue()));

	public static final SettingSpec<Bytes> CHAINING_SSL_KEY_STORE_BYTES =
			SETTING_SPECS.addThenGet(new BytesSettingSpec(
					"chaining.ssl.keyStoreBytes",
					SslPropertySpecConstants.SSL_KEY_STORE_BYTES.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The key store file for the SSL/TLS connections to the "
					+ "other SOCKS server",
			name = "chaining.ssl.keyStoreFile",
			syntax = "chaining.ssl.keyStoreFile=FILE",
			valueType = File.class
	)
	public static final SettingSpec<File> CHAINING_SSL_KEY_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"chaining.ssl.keyStoreFile", 
					SslPropertySpecConstants.SSL_KEY_STORE_FILE.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The password for the key store for the SSL/TLS "
					+ "connections to the other SOCKS server",
			name = "chaining.ssl.keyStorePassword",
			syntax = "chaining.ssl.keyStorePassword=PASSWORD",
			valueType = String.class
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_SSL_KEY_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"chaining.ssl.keyStorePassword", 
					SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "PKCS12",
			description = "The type of key store for the SSL/TLS connections "
					+ "to the other SOCKS server",
			name = "chaining.ssl.keyStoreType",
			syntax = "chaining.ssl.keyStoreType=TYPE",
			valueType = String.class
	)	
	public static final SettingSpec<String> CHAINING_SSL_KEY_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.ssl.keyStoreType", 
					SslPropertySpecConstants.SSL_KEY_STORE_TYPE.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "TLSv1.2",
			description = "The protocol version for the SSL/TLS connections to "
					+ "the other SOCKS server",
			name = "chaining.ssl.protocol",
			syntax = "chaining.ssl.protocol=PROTOCOL",
			valueType = String.class
	)	
	public static final SettingSpec<String> CHAINING_SSL_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.ssl.protocol", 
					SslPropertySpecConstants.SSL_PROTOCOL.getDefaultProperty().getValue()));

	public static final SettingSpec<Bytes> CHAINING_SSL_TRUST_STORE_BYTES =
			SETTING_SPECS.addThenGet(new BytesSettingSpec(
					"chaining.ssl.trustStoreBytes",
					SslPropertySpecConstants.SSL_TRUST_STORE_BYTES.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The trust store file for the SSL/TLS connections to "
					+ "the other SOCKS server",
			name = "chaining.ssl.trustStoreFile",
			syntax = "chaining.ssl.trustStoreFile=FILE",
			valueType = File.class
	)	
	public static final SettingSpec<File> CHAINING_SSL_TRUST_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"chaining.ssl.trustStoreFile", 
					SslPropertySpecConstants.SSL_TRUST_STORE_FILE.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The password for the trust store for the SSL/TLS "
					+ "connections to the other SOCKS server",
			name = "chaining.ssl.trustStorePassword",
			syntax = "chaining.ssl.trustStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final SettingSpec<EncryptedPassword> CHAINING_SSL_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"chaining.ssl.trustStorePassword", 
					SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			defaultValue = "PKCS12",
			description = "The type of trust store for the SSL/TLS "
					+ "connections to the other SOCKS server",
			name = "chaining.ssl.trustStoreType",
			syntax = "chaining.ssl.trustStoreType=TYPE",
			valueType = String.class
	)	
	public static final SettingSpec<String> CHAINING_SSL_TRUST_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.ssl.trustStoreType", 
					SslPropertySpecConstants.SSL_TRUST_STORE_TYPE.getDefaultProperty().getValue()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingSslSettingSpecConstants() { }
	
}
