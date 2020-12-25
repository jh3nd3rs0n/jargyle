package jargyle.client;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.Host;
import jargyle.common.net.Port;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.net.ssl.CipherSuites;
import jargyle.common.net.ssl.Protocols;
import jargyle.common.security.EncryptedPassword;
import jargyle.common.util.PositiveInteger;

public enum PropertySpec {

	BIND_HOST("socksClient.bindHost") {
		
		private static final String DEFAULT_BIND_HOST = "0.0.0.0";
		
		@Override
		public Property getDefaultProperty() {
			Host bindHost = null;
			try {
				bindHost = Host.newInstance(DEFAULT_BIND_HOST);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return new Property(this, bindHost);
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof Host)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Host.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			Host bindHost = null;
			try {
				bindHost = Host.newInstance(value);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			return newProperty(bindHost);
		}
		
	},
	BIND_PORT("socksClient.bindPort") {
		
		private static final int DEFAULT_INT_VALUE = 0;
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, Port.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof Port)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Port.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(Port.newInstance(value));
		}
		
	},
	CONNECT_TIMEOUT("socksClient.connectTimeout") {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, PositiveInteger.newInstance(
					DEFAULT_INT_VALUE));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKET_SETTINGS("socksClient.socketSettings") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, SocketSettings.newInstance());
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_AUTH_METHODS("socksClient.socks5.authMethods") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, AuthMethods.newInstance(
					AuthMethod.NO_AUTHENTICATION_REQUIRED));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof AuthMethods)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						AuthMethods.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(AuthMethods.newInstance(value));
		}
		
	},
	SOCKS5_GSSAPI_MECHANISM_OID("socksClient.socks5.gssapiMechanismOid") {
		
		@Override
		public Property getDefaultProperty() {
			Oid gssapiMechanismOid = null;
			try {
				gssapiMechanismOid = new Oid("1.2.840.113554.1.2.2");
			} catch (GSSException e) {
				throw new AssertionError(e);
			}
			return new Property(this, gssapiMechanismOid);
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof Oid)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Oid.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			Oid gssapiMechanismOid = null;
			try {
				gssapiMechanismOid = new Oid(value);
			} catch (GSSException e) {
				throw new IllegalArgumentException(e);
			}
			return newProperty(gssapiMechanismOid);
		}
		
	},
	SOCKS5_GSSAPI_NEC_REFERENCE_IMPL("socksClient.socks5.gssapiNecReferenceImpl") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, Boolean.valueOf(DEFAULT_BOOLEAN_VALUE));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof Boolean)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Boolean.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(Boolean.valueOf(value));
		}
		
	},
	SOCKS5_GSSAPI_PROTECTION_LEVELS("socksClient.socks5.gssapiProtectionLevels") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, GssapiProtectionLevels.DEFAULT_INSTANCE);
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof GssapiProtectionLevels)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						GssapiProtectionLevels.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(GssapiProtectionLevels.newInstance(value));
		}
		
	},
	SOCKS5_GSSAPI_SERVICE_NAME("socksClient.socks5.gssapiServiceName") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, null);
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof String)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						String.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, value);
		}
		
	},
	SOCKS5_USERNAME("socksClient.socks5.username") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, System.getProperty("user.name"));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof String)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						String.class.getName()));
			}
			UsernamePassword.validateUsername((String) value);
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			UsernamePassword.validateUsername((String) value);
			return new Property(this, value);
		}
		
	},
	SOCKS5_PASSWORD("socksClient.socks5.password") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, EncryptedPassword.newInstance(new char[] { }));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof EncryptedPassword)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						EncryptedPassword.class.getName()));
			}
			EncryptedPassword val = (EncryptedPassword) value;
			char[] password = val.getPassword();
			UsernamePassword.validatePassword(password);
			Arrays.fill(password, '\0');
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			char[] val = value.toCharArray();
			UsernamePassword.validatePassword(val);
			EncryptedPassword v = EncryptedPassword.newInstance(val);
			return new Property(this, v);
		}
		
	},
	SSL_ENABLED("socksClient.ssl.enabled") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, Boolean.valueOf(DEFAULT_BOOLEAN_VALUE));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof Boolean)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Boolean.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(Boolean.valueOf(value));
		}
		
	},
	SSL_ENABLED_CIPHER_SUITES("socksClient.ssl.enabledCipherSuites") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, CipherSuites.newInstance(new String[] { }));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof CipherSuites)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						CipherSuites.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(CipherSuites.newInstance(value));
		}
		
	},
	SSL_ENABLED_PROTOCOLS("socksClient.ssl.enabledProtocols") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, Protocols.newInstance(new String[] { }));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof Protocols)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Protocols.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(Protocols.newInstance(value));
		}
		
	},
	SSL_KEY_STORE_FILE("socksClient.ssl.keyStoreFile") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, null);
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof File)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						File.class.getName()));
			}
			File val = (File) value;
			if (!val.exists()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' does not exist", 
						val));
			}
			if (!val.isDirectory()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' must not be a directory", 
						val));
			}
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(new File(value));
		}
		
	},
	SSL_KEY_STORE_PASSWORD("socksClient.ssl.keyStorePassword") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, EncryptedPassword.newInstance(new char[] { }));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof EncryptedPassword)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						EncryptedPassword.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	},
	SSL_TRUST_STORE_FILE("socksClient.ssl.trustStoreFile") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, null);
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof File)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						File.class.getName()));
			}
			File val = (File) value;
			if (!val.exists()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' does not exist", 
						val));
			}
			if (!val.isDirectory()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' must not be a directory", 
						val));
			}
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(new File(value));
		}
		
	},
	SSL_TRUST_STORE_PASSWORD("socksClient.ssl.trustStorePassword") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, EncryptedPassword.newInstance(new char[] { }));
		}

		@Override
		public Property newProperty(final Object value) {
			if (!(value instanceof EncryptedPassword)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						EncryptedPassword.class.getName()));
			}
			return new Property(this, value);
		}

		@Override
		public Property newProperty(final String value) {
			return newProperty(EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	};
	
	private final String string;
	
	private PropertySpec(final String s) {
		this.string = s;
	}
	
	public abstract Property getDefaultProperty();
	
	public abstract Property newProperty(final Object value);
	
	public abstract Property newProperty(final String value);
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
