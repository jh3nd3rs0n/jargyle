package jargyle.net.socks.client;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public abstract class PropertySpec<V> 
	implements Comparable<PropertySpec<? extends Object>> {

	private static int NEXT_ORDINAL = 0;
	
	public static final PropertySpec<Host> BIND_HOST = 
			PropertySpecFactoryImpl.INSTANCE.newHostPropertySpec(
					"socksClient.bindHost",
					Host.INET4_ALL_ZEROS_INSTANCE);
	
	public static final PropertySpec<Port> BIND_PORT = 
			PropertySpecFactoryImpl.INSTANCE.newPortPropertySpec(
					"socksClient.bindPort",
					Port.newInstance(0));
	
	public static final PropertySpec<PositiveInteger> CONNECT_TIMEOUT = 
			PropertySpecFactoryImpl.INSTANCE.newPositiveIntegerPropertySpec(
					"socksClient.connectTimeout",
					PositiveInteger.newInstance(60000)); // 1 minute
	
	public static final PropertySpec<Boolean> DTLS_ENABLED = 
			PropertySpecFactoryImpl.INSTANCE.newBooleanPropertySpec(
					"socksClient.dtls.enabled",
					Boolean.FALSE);
	
	public static final PropertySpec<Strings> DTLS_ENABLED_CIPHER_SUITES = 
			PropertySpecFactoryImpl.INSTANCE.newStringsPropertySpec(
					"socksClient.dtls.enabledCipherSuites",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<Strings> DTLS_ENABLED_PROTOCOLS = 
			PropertySpecFactoryImpl.INSTANCE.newStringsPropertySpec(
					"socksClient.dtls.enabledProtocols",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<File> DTLS_KEY_STORE_FILE = 
			PropertySpecFactoryImpl.INSTANCE.newFilePropertySpec(
					"socksClient.dtls.keyStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			PropertySpecFactoryImpl.INSTANCE.newEncryptedPasswordPropertySpec(
					"socksClient.dtls.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> DTLS_KEY_STORE_TYPE = 
			PropertySpecFactoryImpl.INSTANCE.newStringPropertySpec(
					"socksClient.dtls.keyStoreType",
					"PKCS12");

	public static final PropertySpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			PropertySpecFactoryImpl.INSTANCE.newPositiveIntegerPropertySpec(
					"socksClient.dtls.maxPacketSize",
					PositiveInteger.newInstance(32768));
	
	public static final PropertySpec<String> DTLS_PROTOCOL = 
			PropertySpecFactoryImpl.INSTANCE.newStringPropertySpec(
					"socksClient.dtls.protocol",
					"DTLSv1.2");
	
	public static final PropertySpec<File> DTLS_TRUST_STORE_FILE = 
			PropertySpecFactoryImpl.INSTANCE.newFilePropertySpec(
					"socksClient.dtls.trustStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD = 
			PropertySpecFactoryImpl.INSTANCE.newEncryptedPasswordPropertySpec(
					"socksClient.dtls.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> DTLS_TRUST_STORE_TYPE = 
			PropertySpecFactoryImpl.INSTANCE.newStringPropertySpec(
					"socksClient.dtls.trustStoreType",
					"PKCS12");
	
	public static final PropertySpec<SocketSettings> SOCKET_SETTINGS = 
			PropertySpecFactoryImpl.INSTANCE.newSocketSettingsPropertySpec(
					"socksClient.socketSettings",
					SocketSettings.newInstance());
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIAUTH_MECHANISM_OID = 
			PropertySpecFactoryImpl.INSTANCE.newOidPropertySpec(
					"socksClient.socks5.gssapiauth.mechanismOid",
					"1.2.840.113554.1.2.2");
	
	public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			PropertySpecFactoryImpl.INSTANCE.newBooleanPropertySpec(
					"socksClient.socks5.gssapiauth.necReferenceImpl",
					Boolean.FALSE);
	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			PropertySpecFactoryImpl.INSTANCE.newProtectionLevelsPropertySpec(
					"socksClient.socks5.gssapiauth.protectionLevels",
					ProtectionLevels.DEFAULT_INSTANCE);
	
	public static final PropertySpec<String> SOCKS5_GSSAPIAUTH_SERVICE_NAME = 
			PropertySpecFactoryImpl.INSTANCE.newStringPropertySpec(
					"socksClient.socks5.gssapiauth.serviceName",
					null);
	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PropertySpecFactoryImpl.INSTANCE.newMethodsPropertySpec(
					"socksClient.socks5.methods",
					Methods.newInstance(Method.NO_AUTHENTICATION_REQUIRED));
	
	public static final PropertySpec<Boolean> SOCKS5_RESOLVE_USE_RESOLVE_COMMAND = 
			PropertySpecFactoryImpl.INSTANCE.newBooleanPropertySpec(
					"socksClient.socks5.resolve.useResolveCommand",
					Boolean.FALSE);
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSAUTH_PASSWORD = 
			PropertySpecFactoryImpl.INSTANCE.newUserEncryptedPasswordPropertySpec(
					"socksClient.socks5.userpathauth.password",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SOCKS5_USERPASSAUTH_USERNAME = 
			PropertySpecFactoryImpl.INSTANCE.newUsernamePropertySpec(
					"socksClient.socks5.userpathauth.username",
					System.getProperty("user.name"));
	
	public static final PropertySpec<Boolean> SSL_ENABLED = 
			PropertySpecFactoryImpl.INSTANCE.newBooleanPropertySpec(
					"socksClient.ssl.enabled",
					Boolean.FALSE);
	
	public static final PropertySpec<Strings> SSL_ENABLED_CIPHER_SUITES = 
			PropertySpecFactoryImpl.INSTANCE.newStringsPropertySpec(
					"socksClient.ssl.enabledCipherSuites",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<Strings> SSL_ENABLED_PROTOCOLS = 
			PropertySpecFactoryImpl.INSTANCE.newStringsPropertySpec(
					"socksClient.ssl.enabledProtocols",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<File> SSL_KEY_STORE_FILE = 
			PropertySpecFactoryImpl.INSTANCE.newFilePropertySpec(
					"socksClient.ssl.keyStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			PropertySpecFactoryImpl.INSTANCE.newEncryptedPasswordPropertySpec(
					"socksClient.ssl.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_KEY_STORE_TYPE = 
			PropertySpecFactoryImpl.INSTANCE.newStringPropertySpec(
					"socksClient.ssl.keyStoreType",
					"PKCS12");
	
	public static final PropertySpec<String> SSL_PROTOCOL = 
			PropertySpecFactoryImpl.INSTANCE.newStringPropertySpec(
					"socksClient.ssl.protocol",
					"TLSv1.2");
	
	public static final PropertySpec<File> SSL_TRUST_STORE_FILE = 
			PropertySpecFactoryImpl.INSTANCE.newFilePropertySpec(
					"socksClient.ssl.trustStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			PropertySpecFactoryImpl.INSTANCE.newEncryptedPasswordPropertySpec(
					"socksClient.ssl.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_TRUST_STORE_TYPE = 
			PropertySpecFactoryImpl.INSTANCE.newStringPropertySpec(
					"socksClient.ssl.trustStoreType",
					"PKCS12");
	
	private static final List<PropertySpec<Object>> VALUES = 
			new ArrayList<PropertySpec<Object>>();
	
	private static void fillValuesIfEmpty() {
		if (!VALUES.isEmpty()) {
			return;
		}
		VALUES.clear();
		Field[] fields = PropertySpec.class.getFields();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			Class<?> type = field.getType();
			if (!Modifier.isStatic(modifiers)
					|| !Modifier.isFinal(modifiers)
					|| !PropertySpec.class.isAssignableFrom(type)) {
				continue;
			}
			Object value = null;
			try {
				value = field.get(null);
			} catch (IllegalArgumentException e) {
				throw new AssertionError(e);
			} catch (IllegalAccessException e) {
				throw new AssertionError(e);
			}
			@SuppressWarnings("unchecked")
			PropertySpec<Object> val = (PropertySpec<Object>) value;
			VALUES.add(val);
		}		
	}
	
	public static PropertySpec<Object>[] values() {
		fillValuesIfEmpty();
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
			final String s, final Class<V> valType, final V defaultVal) {
		if (!PropertySpecFactoryImpl.INSTANCE.canCreateNewInstanceOf(
				this.getClass())) {
			throw new AssertionError(String.format(
					"creating a custom %s is not allowed", 
					PropertySpec.class.getSimpleName()));
		}
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		this.defaultValue = valType.cast(defaultVal);
		this.ordinal = NEXT_ORDINAL++;
		this.string = s;
		this.valueType = valType;
		this.defaultProperty = null;
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
