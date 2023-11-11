package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.CommaSeparatedValuesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.EncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.FileSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

public final class ChainingSslSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The boolean value to indicate if SSL/TLS connections to "
					+ "the other SOCKS server are enabled (default is false)",
			usage = "chaining.ssl.enabled=true|false"
	)
	public static final SettingSpec<Boolean> CHAINING_SSL_ENABLED = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.ssl.enabled", 
					SslPropertySpecConstants.SSL_ENABLED.getDefaultProperty().getValue()));

	@HelpText(
			doc = "The comma separated list of acceptable cipher suites "
					+ "enabled for SSL/TLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[,SSL_CIPHER_SUITE2[...]]]"
	)
	public static final SettingSpec<CommaSeparatedValues> CHAINING_SSL_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"chaining.ssl.enabledCipherSuites", 
					SslPropertySpecConstants.SSL_ENABLED_CIPHER_SUITES.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The comma separated list of acceptable protocol versions "
					+ "enabled for SSL/TLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.ssl.enabledProtocols=[SSL_PROTOCOL1[,SSL_PROTOCOL2[...]]]"
	)	
	public static final SettingSpec<CommaSeparatedValues> CHAINING_SSL_ENABLED_PROTOCOLS = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"chaining.ssl.enabledProtocols", 
					SslPropertySpecConstants.SSL_ENABLED_PROTOCOLS.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The key store file for the SSL/TLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.ssl.keyStoreFile=FILE"
	)
	public static final SettingSpec<File> CHAINING_SSL_KEY_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"chaining.ssl.keyStoreFile", 
					SslPropertySpecConstants.SSL_KEY_STORE_FILE.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The password for the key store for the SSL/TLS connections "
					+ "to the other SOCKS server",
			usage = "chaining.ssl.keyStorePassword=PASSWORD"
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_SSL_KEY_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"chaining.ssl.keyStorePassword", 
					SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The type of key store file for the SSL/TLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.ssl.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> CHAINING_SSL_KEY_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.ssl.keyStoreType", 
					SslPropertySpecConstants.SSL_KEY_STORE_TYPE.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The protocol version for the SSL/TLS connections to the "
					+ "other SOCKS server (default is TLSv1.2)",
			usage = "chaining.ssl.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> CHAINING_SSL_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.ssl.protocol", 
					SslPropertySpecConstants.SSL_PROTOCOL.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The trust store file for the SSL/TLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.ssl.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> CHAINING_SSL_TRUST_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"chaining.ssl.trustStoreFile", 
					SslPropertySpecConstants.SSL_TRUST_STORE_FILE.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The password for the trust store for the SSL/TLS "
					+ "connections to the other SOCKS server",
			usage = "chaining.ssl.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> CHAINING_SSL_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"chaining.ssl.trustStorePassword", 
					SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The type of trust store file for the SSL/TLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.ssl.trustStoreType=TYPE"
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
