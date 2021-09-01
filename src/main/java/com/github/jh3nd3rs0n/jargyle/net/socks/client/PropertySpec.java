package com.github.jh3nd3rs0n.jargyle.net.socks.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.ietf.jgss.Oid;

import com.github.jh3nd3rs0n.jargyle.net.Host;
import com.github.jh3nd3rs0n.jargyle.net.Port;
import com.github.jh3nd3rs0n.jargyle.net.SocketSettings;
import com.github.jh3nd3rs0n.jargyle.net.socks.client.propertyspec.impl.PropertySpecHelper;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Method;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.Methods;
import com.github.jh3nd3rs0n.jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import com.github.jh3nd3rs0n.jargyle.security.EncryptedPassword;
import com.github.jh3nd3rs0n.jargyle.util.PositiveInteger;
import com.github.jh3nd3rs0n.jargyle.util.Strings;

public abstract class PropertySpec<V> 
	implements Comparable<PropertySpec<? extends Object>> {
	
	private static final Object PERMISSION_OBJECT = new Object();
	
	private static int nextOrdinal = 0;
	
	private static final List<PropertySpec<Object>> VALUES = 
			new ArrayList<PropertySpec<Object>>();
	
	public static final PropertySpec<Host> BIND_HOST = 
			PropertySpecHelper.newHostPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.bindHost",
					Host.getInet4AllZerosInstance());
	
	public static final PropertySpec<Port> BIND_PORT = 
			PropertySpecHelper.newPortPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.bindPort",
					Port.newInstance(0));
	
	public static final PropertySpec<PositiveInteger> CONNECT_TIMEOUT = 
			PropertySpecHelper.newPositiveIntegerPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.connectTimeout",
					PositiveInteger.newInstance(60000)); // 1 minute
	
	public static final PropertySpec<Boolean> DTLS_ENABLED = 
			PropertySpecHelper.newBooleanPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.enabled",
					Boolean.FALSE);
	
	public static final PropertySpec<Strings> DTLS_ENABLED_CIPHER_SUITES = 
			PropertySpecHelper.newStringsPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.enabledCipherSuites",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<Strings> DTLS_ENABLED_PROTOCOLS = 
			PropertySpecHelper.newStringsPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.enabledProtocols",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<File> DTLS_KEY_STORE_FILE = 
			PropertySpecHelper.newFilePropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.keyStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			PropertySpecHelper.newEncryptedPasswordPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> DTLS_KEY_STORE_TYPE = 
			PropertySpecHelper.newStringPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.keyStoreType",
					"PKCS12");

	public static final PropertySpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			PropertySpecHelper.newPositiveIntegerPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.maxPacketSize",
					PositiveInteger.newInstance(32768));
	
	public static final PropertySpec<String> DTLS_PROTOCOL = 
			PropertySpecHelper.newStringPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.protocol",
					"DTLSv1.2");
	
	public static final PropertySpec<File> DTLS_TRUST_STORE_FILE = 
			PropertySpecHelper.newFilePropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.trustStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD = 
			PropertySpecHelper.newEncryptedPasswordPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> DTLS_TRUST_STORE_TYPE = 
			PropertySpecHelper.newStringPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.dtls.trustStoreType",
					"PKCS12");
	
	public static final PropertySpec<SocketSettings> SOCKET_SETTINGS = 
			PropertySpecHelper.newSocketSettingsPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socketSettings",
					SocketSettings.newInstance());
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIAUTH_MECHANISM_OID = 
			PropertySpecHelper.newOidPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.gssapiauth.mechanismOid",
					"1.2.840.113554.1.2.2");
	
	public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			PropertySpecHelper.newBooleanPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.gssapiauth.necReferenceImpl",
					Boolean.FALSE);
	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			PropertySpecHelper.newProtectionLevelsPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.gssapiauth.protectionLevels",
					ProtectionLevels.getDefault());
	
	public static final PropertySpec<String> SOCKS5_GSSAPIAUTH_SERVICE_NAME = 
			PropertySpecHelper.newStringPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.gssapiauth.serviceName",
					null);
	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PropertySpecHelper.newMethodsPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.methods",
					Methods.newInstance(Method.NO_AUTHENTICATION_REQUIRED));
	
	public static final PropertySpec<Boolean> SOCKS5_RESOLVE_USE_RESOLVE_COMMAND = 
			PropertySpecHelper.newBooleanPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.resolve.useResolveCommand",
					Boolean.FALSE);
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSAUTH_PASSWORD = 
			PropertySpecHelper.newUserEncryptedPasswordPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.userpathauth.password",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SOCKS5_USERPASSAUTH_USERNAME = 
			PropertySpecHelper.newUsernamePropertySpec(
					PERMISSION_OBJECT,
					"socksClient.socks5.userpathauth.username",
					System.getProperty("user.name"));
	
	public static final PropertySpec<Boolean> SSL_ENABLED = 
			PropertySpecHelper.newBooleanPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.enabled",
					Boolean.FALSE);
	
	public static final PropertySpec<Strings> SSL_ENABLED_CIPHER_SUITES = 
			PropertySpecHelper.newStringsPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.enabledCipherSuites",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<Strings> SSL_ENABLED_PROTOCOLS = 
			PropertySpecHelper.newStringsPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.enabledProtocols",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<File> SSL_KEY_STORE_FILE = 
			PropertySpecHelper.newFilePropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.keyStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			PropertySpecHelper.newEncryptedPasswordPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_KEY_STORE_TYPE = 
			PropertySpecHelper.newStringPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.keyStoreType",
					"PKCS12");
	
	public static final PropertySpec<String> SSL_PROTOCOL = 
			PropertySpecHelper.newStringPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.protocol",
					"TLSv1.2");
	
	public static final PropertySpec<File> SSL_TRUST_STORE_FILE = 
			PropertySpecHelper.newFilePropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.trustStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			PropertySpecHelper.newEncryptedPasswordPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_TRUST_STORE_TYPE = 
			PropertySpecHelper.newStringPropertySpec(
					PERMISSION_OBJECT,
					"socksClient.ssl.trustStoreType",
					"PKCS12");
	
	public static PropertySpec<Object>[] values() {
		@SuppressWarnings("unchecked")
		PropertySpec<Object>[] vals = (PropertySpec<Object>[]) VALUES.toArray(
				new PropertySpec<?>[VALUES.size()]);
		return vals;
	}

	private Property<V> defaultProperty;
	private final V defaultValue;
	private final int ordinal;
	private final String string;
	private final Class<V> valueType;
	
	public PropertySpec(
			final Object permissionObj, 
			final String s, 
			final Class<V> valType, 
			final V defaultVal) {
		Objects.requireNonNull(permissionObj);
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		if (!PERMISSION_OBJECT.equals(permissionObj)) {
			throw new IllegalArgumentException(
					"permission object not from PropertySpec");
		}
		this.defaultValue = valType.cast(defaultVal);
		this.ordinal = nextOrdinal++;
		this.string = s;
		this.valueType = valType;
		this.defaultProperty = null;
		@SuppressWarnings("unchecked")
		PropertySpec<Object> val = (PropertySpec<Object>) this;
		VALUES.add(val);
	}
	
	@Override
	public final int compareTo(final PropertySpec<? extends Object> o) {
		return this.ordinal - o.ordinal;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		PropertySpec<?> other = (PropertySpec<?>) obj;
		if (this.ordinal != other.ordinal) {
			return false;
		}
		return true;
	}

	public final Property<V> getDefaultProperty() {
		if (this.defaultProperty == null) {
			this.defaultProperty = new Property<V>(this, this.valueType.cast(
					this.defaultValue));
		}
		return this.defaultProperty;
	}

	public final Class<V> getValueType() {
		return this.valueType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.ordinal;
		return result;
	}
	
	public Property<V> newProperty(final V value) {
		return new Property<V>(this, this.valueType.cast(value));
	}
	
	public abstract Property<V> newPropertyOfParsableValue(final String value);
	
	public final int ordinal() {
		return this.ordinal;
	}
	
	@Override
	public final String toString() {
		return this.string;
	}
	
}
