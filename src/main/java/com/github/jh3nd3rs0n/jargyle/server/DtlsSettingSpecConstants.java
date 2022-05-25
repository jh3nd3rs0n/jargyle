package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.text.Words;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.EncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.FileSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.WordsSettingSpec;

public final class DtlsSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The boolean value to indicate if DTLS connections to "
					+ "the SOCKS server are enabled (default is false)",
			usage = "dtls.enabled=true|false"
	)	
	public static final SettingSpec<Boolean> DTLS_ENABLED = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"dtls.enabled", 
					Boolean.FALSE));

	@HelpText(
			doc = "The space separated list of acceptable cipher suites "
					+ "enabled for DTLS connections to the SOCKS server",
			usage = "dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[...]]]"
	)	
	public static final SettingSpec<Words> DTLS_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.addThenGet(new WordsSettingSpec(
					"dtls.enabledCipherSuites", 
					Words.newInstance(new String[] { })));
	
	@HelpText(
			doc = "The space separated list of acceptable protocol versions "
					+ "enabled for DTLS connections to the SOCKS server",
			usage = "dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[...]]]"
	)	
	public static final SettingSpec<Words> DTLS_ENABLED_PROTOCOLS = 
			SETTING_SPECS.addThenGet(new WordsSettingSpec(
					"dtls.enabledProtocols", 
					Words.newInstance(new String[] { })));
	
	@HelpText(
			doc = "The key store file for the DTLS connections to the SOCKS "
					+ "server",
			usage = "dtls.keyStoreFile=FILE"
	)	
	public static final SettingSpec<File> DTLS_KEY_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"dtls.keyStoreFile", 
					null));
	
	@HelpText(
			doc = "The password for the key store for the DTLS connections "
					+ "to the SOCKS server",
			usage = "dtls.keyStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"dtls.keyStorePassword", 
					EncryptedPassword.newInstance(new char[] { })));
	
	@HelpText(
			doc = "The type of key store file for the DTLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "dtls.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> DTLS_KEY_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.keyStoreType", 
					"PKCS12"));
	
	@HelpText(
			doc = "The maximum packet size for the DTLS connections to the "
					+ "SOCKS server (default is 32768)",
			usage = "dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final SettingSpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"dtls.maxPacketSize", 
					PositiveInteger.newInstance(32768)));
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "required for DTLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "dtls.needClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> DTLS_NEED_CLIENT_AUTH = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"dtls.needClientAuth", 
					Boolean.FALSE));
	
	@HelpText(
			doc = "The protocol version for the DTLS connections to the "
					+ "SOCKS server (default is DTLSv1.2)",
			usage = "dtls.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> DTLS_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.protocol", 
					"DTLSv1.2"));
	
	@HelpText(
			doc = "The trust store file for the DTLS connections to the "
					+ "SOCKS server",
			usage = "dtls.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> DTLS_TRUST_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"dtls.trustStoreFile", 
					null));
	
	@HelpText(
			doc = "The password for the trust store for the DTLS "
					+ "connections to the SOCKS server",
			usage = "dtls.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"dtls.trustStorePassword", 
					EncryptedPassword.newInstance(new char[] { })));
	
	@HelpText(
			doc = "The type of trust store file for the DTLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "dtls.trustStoreType=TYPE"
	)		
	public static final SettingSpec<String> DTLS_TRUST_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.trustStoreType", 
					"PKCS12"));
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "requested for DTLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "dtls.wantClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> DTLS_WANT_CLIENT_AUTH = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"dtls.wantClientAuth", 
					Boolean.FALSE));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private DtlsSettingSpecConstants() { }
	
}
