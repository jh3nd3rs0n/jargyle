package jargyle.net.socks.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

public abstract class PropertySpec {

	public static final PropertySpec BIND_HOST = new HostPropertySpec(
			"socksClient.bindHost",
			Host.getIpv4WildcardInstance());
	
	public static final PropertySpec BIND_PORT = new PortPropertySpec(
			"socksClient.bindPort",
			Port.newInstance(0));
	
	public static final PropertySpec CONNECT_TIMEOUT = new PositiveIntegerPropertySpec(
			"socksClient.connectTimeout",
			PositiveInteger.newInstance(60000)); // 1 minute
	
	public static final PropertySpec SOCKET_SETTINGS = new SocketSettingsPropertySpec(
			"socksClient.socketSettings",
			SocketSettings.newInstance());
	
	public static final PropertySpec SOCKS5_AUTH_METHODS = new AuthMethodsPropertySpec(
			"socksClient.socks5.authMethods",
			AuthMethods.newInstance(AuthMethod.NO_AUTHENTICATION_REQUIRED));
	
	public static final PropertySpec SOCKS5_FORWARD_HOSTNAME_RESOLUTION = new BooleanPropertySpec(
			"socksClient.socks5.forwardHostnameResolution",
			Boolean.FALSE);
	
	public static final PropertySpec SOCKS5_GSSAPI_MECHANISM_OID = new OidPropertySpec(
			"socksClient.socks5.gssapiMechanismOid",
			"1.2.840.113554.1.2.2");
	
	public static final PropertySpec SOCKS5_GSSAPI_NEC_REFERENCE_IMPL = new BooleanPropertySpec(
			"socksClient.socks5.gssapiNecReferenceImpl",
			Boolean.FALSE);
	
	public static final PropertySpec SOCKS5_GSSAPI_PROTECTION_LEVELS = new GssapiProtectionLevelsPropertySpec(
			"socksClient.socks5.gssapiProtectionLevels",
			GssapiProtectionLevels.DEFAULT_INSTANCE);
	
	public static final PropertySpec SOCKS5_GSSAPI_SERVICE_NAME = new StringPropertySpec(
			"socksClient.socks5.gssapiServiceName",
			null);
	
	public static final PropertySpec SOCKS5_PASSWORD = new UserEncryptedPasswordPropertySpec(
			"socksClient.socks5.password",
			EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec SOCKS5_USERNAME = new UsernamePropertySpec(
			"socksClient.socks5.username", 
			System.getProperty("user.name"));
	
	public static final PropertySpec SSL_ENABLED = new BooleanPropertySpec(
			"socksClient.ssl.enabled",
			Boolean.FALSE);
	
	public static final PropertySpec SSL_ENABLED_CIPHER_SUITES = new StringsPropertySpec(
			"socksClient.ssl.enabledCipherSuites", 
			Strings.newInstance(new String[] { }));
	
	public static final PropertySpec SSL_ENABLED_PROTOCOLS = new StringsPropertySpec(
			"socksClient.ssl.enabledProtocols", 
			Strings.newInstance(new String[] { }));
	
	public static final PropertySpec SSL_KEY_STORE_FILE = new FilePropertySpec(
			"socksClient.ssl.keyStoreFile", 
			null);
	
	public static final PropertySpec SSL_KEY_STORE_PASSWORD = new EncryptedPasswordPropertySpec(
			"socksClient.ssl.keyStorePassword", 
			EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec SSL_KEY_STORE_TYPE = new StringPropertySpec(
			"socksClient.ssl.keyStoreType",
			"PKCS12");
	
	public static final PropertySpec SSL_PROTOCOL = new StringPropertySpec(
			"socksClient.ssl.protocol",
			"TLSv1");
	
	public static final PropertySpec SSL_TRUST_STORE_FILE = new FilePropertySpec(
			"socksClient.ssl.trustStoreFile", 
			null);
	
	public static final PropertySpec SSL_TRUST_STORE_PASSWORD = new EncryptedPasswordPropertySpec(
			"socksClient.ssl.trustStorePassword",
			EncryptedPassword.newInstance(new char[] { }));
	
	public static final PropertySpec SSL_TRUST_STORE_TYPE = new StringPropertySpec(
			"socksClient.ssl.trustStoreType",
			"PKCS12");
	
	private static final List<PropertySpec> VALUES = new ArrayList<PropertySpec>();
	
	public static PropertySpec[] values() {
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
				PropertySpec val = (PropertySpec) value;
				VALUES.add(val);
			}
		}
		return VALUES.toArray(new PropertySpec[VALUES.size()]);
	}

	private final Property defaultProperty;
	private final String string;
	
	public PropertySpec(final String s, final Object defaultVal) {
		this.defaultProperty = Property.newInstance(this, defaultVal);
		this.string = s;
	}
	
	public final Property getDefaultProperty() {
		return this.defaultProperty;
	}
	
	public abstract Property newProperty(final Object value);
	
	public abstract Property newProperty(final String value);
	
	@Override
	public final String toString() {
		return this.string;
	}
	
}
