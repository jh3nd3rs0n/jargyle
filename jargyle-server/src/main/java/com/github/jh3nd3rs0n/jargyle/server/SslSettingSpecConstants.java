package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.CommaSeparatedValuesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.EncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.FileSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "SSL/TLS Settings"
)
public final class SslSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if SSL/TLS connections "
					+ "to the SOCKS server are enabled (default is false)",
			name = "ssl.enabled",
			syntax = "ssl.enabled=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> SSL_ENABLED = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"ssl.enabled", 
					Boolean.FALSE));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable cipher suites "
					+ "enabled for SSL/TLS connections to the SOCKS server",
			name = "ssl.enabledCipherSuites",
			syntax = "ssl.enabledCipherSuites=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)	
	public static final SettingSpec<CommaSeparatedValues> SSL_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"ssl.enabledCipherSuites", 
					CommaSeparatedValues.newInstance(new String[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protocol "
					+ "versions enabled for SSL/TLS connections to the SOCKS "
					+ "server",
			name = "ssl.enabledProtocols",
			syntax = "ssl.enabledProtocols=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)	
	public static final SettingSpec<CommaSeparatedValues> SSL_ENABLED_PROTOCOLS = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"ssl.enabledProtocols", 
					CommaSeparatedValues.newInstance(new String[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The key store file for the SSL/TLS connections to "
					+ "the SOCKS server",
			name = "ssl.keyStoreFile",
			syntax = "ssl.keyStoreFile=FILE",
			valueType = File.class
	)	
	public static final SettingSpec<File> SSL_KEY_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"ssl.keyStoreFile", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The password for the key store for the SSL/TLS "
					+ "connections to the SOCKS server",
			name = "ssl.keyStorePassword",
			syntax = "ssl.keyStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final SettingSpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"ssl.keyStorePassword", 
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The type of key store file for the SSL/TLS "
					+ "connections to the SOCKS server (default is PKCS12)",
			name = "ssl.keyStoreType",
			syntax = "ssl.keyStoreType=TYPE",
			valueType = String.class
	)	
	public static final SettingSpec<String> SSL_KEY_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"ssl.keyStoreType", 
					"PKCS12"));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that client "
					+ "authentication is required for SSL/TLS connections to "
					+ "the SOCKS server (default is false)",
			name = "ssl.needClientAuth",
			syntax = "ssl.needClientAuth=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> SSL_NEED_CLIENT_AUTH = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"ssl.needClientAuth", 
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The protocol version for the SSL/TLS connections to "
					+ "the SOCKS server (default is TLSv1.2)",
			name = "ssl.protocol",
			syntax = "ssl.protocol=PROTOCOL",
			valueType = String.class
	)	
	public static final SettingSpec<String> SSL_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"ssl.protocol", 
					"TLSv1.2"));
	
	@NameValuePairValueSpecDoc(
			description = "The trust store file for the SSL/TLS connections to "
					+ "the SOCKS server",
			name = "ssl.trustStoreFile",
			syntax = "ssl.trustStoreFile=FILE",
			valueType = File.class
	)	
	public static final SettingSpec<File> SSL_TRUST_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"ssl.trustStoreFile", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The password for the trust store for the SSL/TLS "
					+ "connections to the SOCKS server",
			name = "ssl.trustStorePassword",
			syntax = "ssl.trustStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final SettingSpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"ssl.trustStorePassword", 
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The type of trust store file for the SSL/TLS "
					+ "connections to the SOCKS server (default is PKCS12)",
			name = "ssl.trustStoreType",
			syntax = "ssl.trustStoreType=TYPE",
			valueType = String.class
	)		
	public static final SettingSpec<String> SSL_TRUST_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"ssl.trustStoreType", 
					"PKCS12"));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that client "
					+ "authentication is requested for SSL/TLS connections to "
					+ "the SOCKS server (default is false)",
			name = "ssl.wantClientAuth",
			syntax = "ssl.wantClientAuth=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> SSL_WANT_CLIENT_AUTH = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"ssl.wantClientAuth", 
					Boolean.FALSE));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private SslSettingSpecConstants() { }
	
}
