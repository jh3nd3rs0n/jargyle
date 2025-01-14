package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
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
		name = "Chaining DTLS Settings"
)
public final class ChainingDtlsSettingSpecConstants {

	private static final SettingSpecs SETTING_SPECS = new SettingSpecs();
	
	@NameValuePairValueSpecDoc(
			description = "The boolean value to indicate if DTLS connections "
					+ "to the other SOCKS server are enabled (default is false)",
			name = "chaining.dtls.enabled",
			syntax = "chaining.dtls.enabled=true|false",
			valueType = Boolean.class
	)
	public static final SettingSpec<Boolean> CHAINING_DTLS_ENABLED = 
			SETTING_SPECS.addThenGet(new BooleanSettingSpec(
					"chaining.dtls.enabled", 
					DtlsPropertySpecConstants.DTLS_ENABLED.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable cipher "
					+ "suites enabled for DTLS connections to the other SOCKS "
					+ "server",
			name = "chaining.dtls.enabledCipherSuites",
			syntax = "chaining.dtls.enabledCipherSuites=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)
	public static final SettingSpec<CommaSeparatedValues> CHAINING_DTLS_ENABLED_CIPHER_SUITES = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"chaining.dtls.enabledCipherSuites", 
					DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The comma separated list of acceptable protocol "
					+ "versions enabled for DTLS connections to the other "
					+ "SOCKS server",
			name = "chaining.dtls.enabledProtocols",
			syntax = "chaining.dtls.enabledProtocols=COMMA_SEPARATED_VALUES",
			valueType = CommaSeparatedValues.class
	)	
	public static final SettingSpec<CommaSeparatedValues> CHAINING_DTLS_ENABLED_PROTOCOLS = 
			SETTING_SPECS.addThenGet(new CommaSeparatedValuesSettingSpec(
					"chaining.dtls.enabledProtocols", 
					DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The protocol version for the DTLS connections to "
					+ "the other SOCKS server (default is DTLSv1.2)",
			name = "chaining.dtls.protocol",
			syntax = "chaining.dtls.protocol=PROTOCOL",
			valueType = String.class
	)	
	public static final SettingSpec<String> CHAINING_DTLS_PROTOCOL = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.dtls.protocol", 
					DtlsPropertySpecConstants.DTLS_PROTOCOL.getDefaultProperty().getValue()));

	public static final SettingSpec<Bytes> CHAINING_DTLS_TRUST_STORE_BYTES =
			SETTING_SPECS.addThenGet(new BytesSettingSpec(
					"chaining.dtls.trustStoreBytes",
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The trust store file for the DTLS connections to "
					+ "the other SOCKS server",
			name = "chaining.dtls.trustStoreFile",
			syntax = "chaining.dtls.trustStoreFile=FILE",
			valueType = File.class
	)	
	public static final SettingSpec<File> CHAINING_DTLS_TRUST_STORE_FILE = 
			SETTING_SPECS.addThenGet(new FileSettingSpec(
					"chaining.dtls.trustStoreFile", 
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The password for the trust store for the DTLS "
					+ "connections to the other SOCKS server",
			name = "chaining.dtls.trustStorePassword",
			syntax = "chaining.dtls.trustStorePassword=PASSWORD",
			valueType = String.class
	)	
	public static final SettingSpec<EncryptedPassword> CHAINING_DTLS_TRUST_STORE_PASSWORD = 
			SETTING_SPECS.addThenGet(new EncryptedPasswordSettingSpec(
					"chaining.dtls.trustStorePassword", 
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD.getDefaultProperty().getValue()));
	
	@NameValuePairValueSpecDoc(
			description = "The type of trust store for the DTLS connections "
					+ "to the other SOCKS server (default is PKCS12)",
			name = "chaining.dtls.trustStoreType",
			syntax = "chaining.dtls.trustStoreType=TYPE",
			valueType = String.class
	)	
	public static final SettingSpec<String> CHAINING_DTLS_TRUST_STORE_TYPE = 
			SETTING_SPECS.addThenGet(new StringSettingSpec(
					"chaining.dtls.trustStoreType", 
					DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE.getDefaultProperty().getValue()));

	@NameValuePairValueSpecDoc(
			description = "The buffer size for receiving DTLS wrapped "
					+ "datagrams for the DTLS connections to the other SOCKS "
					+ "server",
			name = "chaining.dtls.wrappedReceiveBufferSize",
			syntax = "chaining.dtls.wrappedReceiveBufferSize=POSITIVE_INTEGER",
			valueType = PositiveInteger.class
	)
	public static final SettingSpec<PositiveInteger> CHAINING_DTLS_WRAPPED_RECEIVE_BUFFER_SIZE =
			SETTING_SPECS.addThenGet(new PositiveIntegerSettingSpec(
					"chaining.dtls.wrappedReceiveBufferSize",
					DtlsPropertySpecConstants.DTLS_WRAPPED_RECEIVE_BUFFER_SIZE.getDefaultProperty().getValue()));

	public static List<SettingSpec<Object>> values() {
		return SETTING_SPECS.toList();
	}
	
	public static Map<String, SettingSpec<Object>> valuesMap() {
		return SETTING_SPECS.toMap();
	}
	
	private ChainingDtlsSettingSpecConstants() { }
	
}
