package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.common.number.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.common.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.common.string.CommaSeparatedValues;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecDoc;
import com.github.jh3nd3rs0n.jargyle.internal.annotation.NameValuePairValueSpecsDoc;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.BooleanSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.CommaSeparatedValuesSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.EncryptedPasswordSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.FileSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.PositiveIntegerSettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.internal.settingspec.impl.StringSettingSpec;

@NameValuePairValueSpecsDoc(
		description = "",
		name = "DTLS Settings"
)
public final class DtlsSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if DTLS connections "
					+ "to the SOCKS server are enabled (default is false)",
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
					CommaSeparatedValues.of(new String[] { })));
	
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
					CommaSeparatedValues.of(new String[] { })));
	
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
			description = "The type of key store file for the DTLS connections "
					+ "to the SOCKS server (default is PKCS12)",
			name = "dtls.keyStoreType",
			syntax = "dtls.keyStoreType=TYPE",
			valueType = String.class
	)	
	public static final SettingSpec<String> DTLS_KEY_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.keyStoreType", 
					"PKCS12"));
	
	@NameValuePairValueSpecDoc(
			description = "The maximum packet size for the DTLS connections to "
					+ "the SOCKS server (default is 32768)",
			name = "dtls.maxPacketSize",
			syntax = "dtls.maxPacketSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)	
	public static final SettingSpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"dtls.maxPacketSize", 
					PositiveInteger.newInstance(32768)));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that client "
					+ "authentication is required for DTLS connections to the "
					+ "SOCKS server (default is false)",
			name = "dtls.needClientAuth",
			syntax = "dtls.needClientAuth=true|false",
			valueType = Boolean.class
	)	
	public static final SettingSpec<Boolean> DTLS_NEED_CLIENT_AUTH = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"dtls.needClientAuth", 
					Boolean.FALSE));
	
	@NameValuePairValueSpecDoc(
			description = "The protocol version for the DTLS connections to the "
					+ "SOCKS server (default is DTLSv1.2)",
			name = "dtls.protocol",
			syntax = "dtls.protocol=PROTOCOL",
			valueType = String.class
	)	
	public static final SettingSpec<String> DTLS_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.protocol", 
					"DTLSv1.2"));
	
	@NameValuePairValueSpecDoc(
			description = "The trust store file for the DTLS connections to the "
					+ "SOCKS server",
			name = "dtls.trustStoreFile",
			syntax = "dtls.trustStoreFile=FILE",
			valueType = File.class
	)	
	public static final SettingSpec<File> DTLS_TRUST_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"dtls.trustStoreFile", 
					null));
	
	@NameValuePairValueSpecDoc(
			description = "The password for the trust store for the DTLS "
					+ "connections to the SOCKS server",
			name = "dtls.trustStorePassword",
			syntax = "dtls.trustStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final SettingSpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"dtls.trustStorePassword", 
					EncryptedPassword.newInstance(new char[] { })));
	
	@NameValuePairValueSpecDoc(
			description = "The type of trust store file for the DTLS "
					+ "connections to the SOCKS server (default is PKCS12)",
			name = "dtls.trustStoreType",
			syntax = "dtls.trustStoreType=TYPE",
			valueType = String.class
	)		
	public static final SettingSpec<String> DTLS_TRUST_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"dtls.trustStoreType", 
					"PKCS12"));
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate that client "
					+ "authentication is requested for DTLS connections to the "
					+ "SOCKS server (default is false)",
			name = "dtls.wantClientAuth",
			syntax = "dtls.wantClientAuth=true|false",
			valueType = Boolean.class
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
