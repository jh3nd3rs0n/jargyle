package jargyle.net.socks.client;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.propertyspec.AuthMethodsPropertySpec;
import jargyle.net.socks.client.propertyspec.BooleanPropertySpec;
import jargyle.net.socks.client.propertyspec.EncryptedPasswordPropertySpec;
import jargyle.net.socks.client.propertyspec.FilePropertySpec;
import jargyle.net.socks.client.propertyspec.GssapiProtectionLevelsPropertySpec;
import jargyle.net.socks.client.propertyspec.HostPropertySpec;
import jargyle.net.socks.client.propertyspec.OidPropertySpec;
import jargyle.net.socks.client.propertyspec.PortPropertySpec;
import jargyle.net.socks.client.propertyspec.PositiveIntegerPropertySpec;
import jargyle.net.socks.client.propertyspec.SocketSettingsPropertySpec;
import jargyle.net.socks.client.propertyspec.StringPropertySpec;
import jargyle.net.socks.client.propertyspec.StringsPropertySpec;
import jargyle.net.socks.client.propertyspec.UserEncryptedPasswordPropertySpec;
import jargyle.net.socks.client.propertyspec.UsernamePropertySpec;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public abstract class PropertySpec<V> {

	public static final PropertySpec<Host> BIND_HOST = new HostPropertySpec(
			"socksClient.bindHost",
			Host.getIpv4WildcardInstance());
	
	public static final PropertySpec<Port> BIND_PORT = new PortPropertySpec(
			"socksClient.bindPort",
			Port.newInstance(0));
	
	public static final PropertySpec<PositiveInteger> CONNECT_TIMEOUT = new PositiveIntegerPropertySpec(
			"socksClient.connectTimeout",
			PositiveInteger.newInstance(60000)); // 1 minute
	
	public static final PropertySpec<SocketSettings> SOCKET_SETTINGS = new SocketSettingsPropertySpec(
			"socksClient.socketSettings",
			SocketSettings.newInstance());
	
	public static final PropertySpec<AuthMethods> SOCKS5_AUTH_METHODS = new AuthMethodsPropertySpec(
			"socksClient.socks5.authMethods",
			AuthMethods.newInstance(AuthMethod.NO_AUTHENTICATION_REQUIRED));
	
	public static final PropertySpec<Boolean> SOCKS5_FORWARD_HOSTNAME_RESOLUTION = new BooleanPropertySpec(
			"socksClient.socks5.forwardHostnameResolution",
			Boolean.FALSE);
	
	public static final PropertySpec<Oid> SOCKS5_GSSAPI_MECHANISM_OID = new OidPropertySpec(
			"socksClient.socks5.gssapiMechanismOid",
			"1.2.840.113554.1.2.2");
	
	public static final PropertySpec<Boolean> SOCKS5_GSSAPI_NEC_REFERENCE_IMPL = new BooleanPropertySpec(
			"socksClient.socks5.gssapiNecReferenceImpl",
			Boolean.FALSE);
	
	public static final PropertySpec<GssapiProtectionLevels> SOCKS5_GSSAPI_PROTECTION_LEVELS = new GssapiProtectionLevelsPropertySpec(
			"socksClient.socks5.gssapiProtectionLevels",
			GssapiProtectionLevels.DEFAULT_INSTANCE);
	
	public static final PropertySpec<String> SOCKS5_GSSAPI_SERVICE_NAME = new StringPropertySpec(
			"socksClient.socks5.gssapiServiceName",
			null);
	
	public static final PropertySpec<EncryptedPassword> SOCKS5_PASSWORD = new UserEncryptedPasswordPropertySpec(
			"socksClient.socks5.password",
			EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SOCKS5_USERNAME = new UsernamePropertySpec(
			"socksClient.socks5.username", 
			System.getProperty("user.name"));
	
	public static final PropertySpec<Boolean> SSL_ENABLED = new BooleanPropertySpec(
			"socksClient.ssl.enabled",
			Boolean.FALSE);
	
	public static final PropertySpec<Strings> SSL_ENABLED_CIPHER_SUITES = new StringsPropertySpec(
			"socksClient.ssl.enabledCipherSuites", 
			Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<Strings> SSL_ENABLED_PROTOCOLS = new StringsPropertySpec(
			"socksClient.ssl.enabledProtocols", 
			Strings.newInstance(new String[] { }));
	
	public static final PropertySpec<File> SSL_KEY_STORE_FILE = new FilePropertySpec(
			"socksClient.ssl.keyStoreFile", 
			null);
	
	public static final PropertySpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = new EncryptedPasswordPropertySpec(
			"socksClient.ssl.keyStorePassword", 
			EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_KEY_STORE_TYPE = new StringPropertySpec(
			"socksClient.ssl.keyStoreType",
			"PKCS12");
	
	public static final PropertySpec<String> SSL_PROTOCOL = new StringPropertySpec(
			"socksClient.ssl.protocol",
			"TLSv1");
	
	public static final PropertySpec<File> SSL_TRUST_STORE_FILE = new FilePropertySpec(
			"socksClient.ssl.trustStoreFile", 
			null);
	
	public static final PropertySpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = new EncryptedPasswordPropertySpec(
			"socksClient.ssl.trustStorePassword",
			EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec<String> SSL_TRUST_STORE_TYPE = new StringPropertySpec(
			"socksClient.ssl.trustStoreType",
			"PKCS12");
	
	private static final List<PropertySpec<Object>> VALUES = 
			new ArrayList<PropertySpec<Object>>();
	
	public static PropertySpec<Object>[] values() {
		if (VALUES.isEmpty()) {
			Field[] fields = PropertySpec.class.getFields();
			for (Field field : fields) {
				Class<?> type = field.getType();
				if (!PropertySpec.class.isAssignableFrom(type)) {
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
		@SuppressWarnings("unchecked")
		PropertySpec<Object>[] vals =
				(PropertySpec<Object>[]) VALUES.toArray(
						new PropertySpec<?>[VALUES.size()]);
		return vals;
	}

	private Property<V> defaultProperty;
	private final V defaultValue;
	private final String string;
	private final Class<V> valueType;
	
	public PropertySpec(
			final String s, final Class<V> valType, final V defaultVal) {
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		this.defaultValue = valType.cast(defaultVal);
		this.string = s;
		this.valueType = valType;
		this.defaultProperty = null;
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
		if (this.string == null) {
			if (other.string != null) {
				return false;
			}
		} else if (!this.string.equals(other.string)) {
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
		result = prime * result + ((this.string == null) ? 
				0 : this.string.hashCode());
		return result;
	}
	
	public Property<V> newProperty(final V value) {
		return new Property<V>(this, this.valueType.cast(value));
	}
	
	public abstract Property<V> newPropertyOfParsableValue(final String value);
	
	@Override
	public final String toString() {
		return this.string;
	}
	
}
