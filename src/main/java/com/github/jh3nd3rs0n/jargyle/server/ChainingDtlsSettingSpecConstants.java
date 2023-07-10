package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.common.lang.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.lang.Strings;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.internal.help.HelpText;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.EncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.FileSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringsSettingSpec;

public final class ChainingDtlsSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@HelpText(
			doc = "The boolean value to indicate if DTLS connections to "
					+ "the other SOCKS server are enabled (default is false)",
			usage = "chaining.dtls.enabled=true|false"
	)
	public static final SettingSpec<Boolean> CHAINING_DTLS_ENABLED = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.dtls.enabled", 
					DtlsPropertySpecConstants.DTLS_ENABLED.getDefaultProperty().getValue()));

	@HelpText(
			doc = "The space separated list of acceptable cipher suites "
					+ "enabled for DTLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[ ...]]]"
	)
	public static final SettingSpec<Strings> CHAINING_DTLS_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.addThenGet(new StringsSettingSpec(
					"chaining.dtls.enabledCipherSuites", 
					DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The space separated list of acceptable protocol versions "
					+ "enabled for DTLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[ ...]]]"
	)	
	public static final SettingSpec<Strings> CHAINING_DTLS_ENABLED_PROTOCOLS = 
			SETTING_SPECS.addThenGet(new StringsSettingSpec(
					"chaining.dtls.enabledProtocols", 
					DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The key store file for the DTLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.dtls.keyStoreFile=FILE"
	)
	public static final SettingSpec<File> CHAINING_DTLS_KEY_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"chaining.dtls.keyStoreFile", 
					DtlsPropertySpecConstants.DTLS_KEY_STORE_FILE.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The password for the key store for the DTLS connections "
					+ "to the other SOCKS server",
			usage = "chaining.dtls.keyStorePassword=PASSWORD"
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_DTLS_KEY_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"chaining.dtls.keyStorePassword", 
					DtlsPropertySpecConstants.DTLS_KEY_STORE_PASSWORD.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The type of key store file for the DTLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.dtls.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> CHAINING_DTLS_KEY_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.dtls.keyStoreType", 
					DtlsPropertySpecConstants.DTLS_KEY_STORE_TYPE.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The maximum packet size for the DTLS connections to the "
					+ "other SOCKS server (default is 32768)",
			usage = "chaining.dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final SettingSpec<PositiveInteger> CHAINING_DTLS_MAX_PACKET_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"chaining.dtls.maxPacketSize", 
					DtlsPropertySpecConstants.DTLS_MAX_PACKET_SIZE.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The protocol version for the DTLS connections to the "
					+ "other SOCKS server (default is DTLSv1.2)",
			usage = "chaining.dtls.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> CHAINING_DTLS_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.dtls.protocol", 
					DtlsPropertySpecConstants.DTLS_PROTOCOL.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The trust store file for the DTLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.dtls.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> CHAINING_DTLS_TRUST_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"chaining.dtls.trustStoreFile", 
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The password for the trust store for the DTLS "
					+ "connections to the other SOCKS server",
			usage = "chaining.dtls.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> CHAINING_DTLS_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"chaining.dtls.trustStorePassword", 
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.getDefaultProperty().getValue()));
	
	@HelpText(
			doc = "The type of trust store file for the DTLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.dtls.trustStoreType=TYPE"
	)	
	public static final SettingSpec<String> CHAINING_DTLS_TRUST_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.dtls.trustStoreType", 
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE.getDefaultProperty().getValue()));
	
	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingDtlsSettingSpecConstants() { }
	
}
