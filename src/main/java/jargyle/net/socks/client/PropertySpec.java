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
import jargyle.net.socks.client.propertyspecfactory.PropertySpecFactoryImpl;
import jargyle.net.socks.transport.v5.Method;
import jargyle.net.socks.transport.v5.Methods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public abstract class PropertySpec<V> 
	implements Comparable<PropertySpec<? extends Object>> {
	
	private static final PropertySpecFactory PROPERTY_SPEC_FACTORY = 
			new PropertySpecFactoryImpl(); 

	private static int NEXT_ORDINAL = 0;
	
	public static final PropertySpec<Host> BIND_HOST = 
			PROPERTY_SPEC_FACTORY.newHostPropertySpec(
					"socksClient.bindHost",
					Host.INET4_ALL_ZEROS_INSTANCE);
	
	public static final PropertySpec<Port> BIND_PORT = 
			PROPERTY_SPEC_FACTORY.newPortPropertySpec(
					"socksClient.bindPort",
					Port.newInstance(0));
	
	public static final PropertySpec<PositiveInteger> CONNECT_TIMEOUT = 
			PROPERTY_SPEC_FACTORY.newPositiveIntegerPropertySpec(
					"socksClient.connectTimeout",
					PositiveInteger.newInstance(60000)); // 1 minute
	
	public static final PropertySpec<Boolean> DTLS_ENABLED = 
			PROPERTY_SPEC_FACTORY.newBooleanPropertySpec(
					"socksClient.dtls.enabled",
					Boolean.FALSE);
	
	public static final PropertySpec<Strings> DTLS_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPEC_FACTORY.newStringsPropertySpec(
					"socksClient.dtls.enabledCipherSuites",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<Strings> DTLS_ENABLED_PROTOCOLS = 
			PROPERTY_SPEC_FACTORY.newStringsPropertySpec(
					"socksClient.dtls.enabledProtocols",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<File> DTLS_KEY_STORE_FILE = 
			PROPERTY_SPEC_FACTORY.newFilePropertySpec(
					"socksClient.dtls.keyStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = 
			PROPERTY_SPEC_FACTORY.newEncryptedPasswordPropertySpec(
					"socksClient.dtls.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> DTLS_KEY_STORE_TYPE = 
			PROPERTY_SPEC_FACTORY.newStringPropertySpec(
					"socksClient.dtls.keyStoreType",
					"PKCS12");

	public static final PropertySpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = 
			PROPERTY_SPEC_FACTORY.newPositiveIntegerPropertySpec(
					"socksClient.dtls.maxPacketSize",
					PositiveInteger.newInstance(32768));
	
	public static final PropertySpec<String> DTLS_PROTOCOL = 
			PROPERTY_SPEC_FACTORY.newStringPropertySpec(
					"socksClient.dtls.protocol",
					"DTLSv1.2");
	
	public static final PropertySpec<File> DTLS_TRUST_STORE_FILE = 
			PROPERTY_SPEC_FACTORY.newFilePropertySpec(
					"socksClient.dtls.trustStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD = 
			PROPERTY_SPEC_FACTORY.newEncryptedPasswordPropertySpec(
					"socksClient.dtls.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> DTLS_TRUST_STORE_TYPE = 
			PROPERTY_SPEC_FACTORY.newStringPropertySpec(
					"socksClient.dtls.trustStoreType",
					"PKCS12");
	
	public static final PropertySpec<SocketSettings> SOCKET_SETTINGS = 
			PROPERTY_SPEC_FACTORY.newSocketSettingsPropertySpec(
					"socksClient.socketSettings",
					SocketSettings.newInstance());
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPIAUTH_MECHANISM_OID = 
			PROPERTY_SPEC_FACTORY.newOidPropertySpec(
					"socksClient.socks5.gssapiauth.mechanismOid",
					"1.2.840.113554.1.2.2");
	
	public static final PropertySpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = 
			PROPERTY_SPEC_FACTORY.newBooleanPropertySpec(
					"socksClient.socks5.gssapiauth.necReferenceImpl",
					Boolean.FALSE);
	
	public static final PropertySpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = 
			PROPERTY_SPEC_FACTORY.newProtectionLevelsPropertySpec(
					"socksClient.socks5.gssapiauth.protectionLevels",
					ProtectionLevels.DEFAULT_INSTANCE);
	
	public static final PropertySpec<String> SOCKS5_GSSAPIAUTH_SERVICE_NAME = 
			PROPERTY_SPEC_FACTORY.newStringPropertySpec(
					"socksClient.socks5.gssapiauth.serviceName",
					null);
	
	public static final PropertySpec<Methods> SOCKS5_METHODS = 
			PROPERTY_SPEC_FACTORY.newMethodsPropertySpec(
					"socksClient.socks5.methods",
					Methods.newInstance(Method.NO_AUTHENTICATION_REQUIRED));
	
	public static final PropertySpec<Boolean> SOCKS5_RESOLVE_USE_RESOLVE_COMMAND = 
			PROPERTY_SPEC_FACTORY.newBooleanPropertySpec(
					"socksClient.socks5.resolve.useResolveCommand",
					Boolean.FALSE);
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_USERPASSAUTH_PASSWORD = 
			PROPERTY_SPEC_FACTORY.newUserEncryptedPasswordPropertySpec(
					"socksClient.socks5.userpathauth.password",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SOCKS5_USERPASSAUTH_USERNAME = 
			PROPERTY_SPEC_FACTORY.newUsernamePropertySpec(
					"socksClient.socks5.userpathauth.username",
					System.getProperty("user.name"));
	
	public static final PropertySpec<Boolean> SSL_ENABLED = 
			PROPERTY_SPEC_FACTORY.newBooleanPropertySpec(
					"socksClient.ssl.enabled",
					Boolean.FALSE);
	
	public static final PropertySpec<Strings> SSL_ENABLED_CIPHER_SUITES = 
			PROPERTY_SPEC_FACTORY.newStringsPropertySpec(
					"socksClient.ssl.enabledCipherSuites",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<Strings> SSL_ENABLED_PROTOCOLS = 
			PROPERTY_SPEC_FACTORY.newStringsPropertySpec(
					"socksClient.ssl.enabledProtocols",
					Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<File> SSL_KEY_STORE_FILE = 
			PROPERTY_SPEC_FACTORY.newFilePropertySpec(
					"socksClient.ssl.keyStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = 
			PROPERTY_SPEC_FACTORY.newEncryptedPasswordPropertySpec(
					"socksClient.ssl.keyStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_KEY_STORE_TYPE = 
			PROPERTY_SPEC_FACTORY.newStringPropertySpec(
					"socksClient.ssl.keyStoreType",
					"PKCS12");
	
	public static final PropertySpec<String> SSL_PROTOCOL = 
			PROPERTY_SPEC_FACTORY.newStringPropertySpec(
					"socksClient.ssl.protocol",
					"TLSv1.2");
	
	public static final PropertySpec<File> SSL_TRUST_STORE_FILE = 
			PROPERTY_SPEC_FACTORY.newFilePropertySpec(
					"socksClient.ssl.trustStoreFile",
					null);
	
	public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = 
			PROPERTY_SPEC_FACTORY.newEncryptedPasswordPropertySpec(
					"socksClient.ssl.trustStorePassword",
					EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_TRUST_STORE_TYPE = 
			PROPERTY_SPEC_FACTORY.newStringPropertySpec(
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
		if (!PROPERTY_SPEC_FACTORY.canCreateNewInstanceOf(this.getClass())) {
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
