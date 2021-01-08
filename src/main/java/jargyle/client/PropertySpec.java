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
			Host val = Host.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			Host bindHost = null;
			try {
				bindHost = Host.newInstance(value);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			return new Property(this, bindHost);
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
			Port val = Port.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, Port.newInstance(value));
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
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, PositiveInteger.newInstance(value));
		}
		
	},
	SOCKET_SETTINGS("socksClient.socketSettings") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, SocketSettings.newInstance());
		}

		@Override
		public Property newProperty(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, SocketSettings.newInstance(value));
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
			AuthMethods val = AuthMethods.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, AuthMethods.newInstance(value));
		}
		
	},
	SOCKS5_FORWARD_HOSTNAME_RESOLUTION(
			"socksClient.socks5.forwardHostnameResolution") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, Boolean.valueOf(DEFAULT_BOOLEAN_VALUE));
		}

		@Override
		public Property newProperty(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, Boolean.valueOf(value));
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
			Oid val = Oid.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			Oid gssapiMechanismOid = null;
			try {
				gssapiMechanismOid = new Oid(value);
			} catch (GSSException e) {
				throw new IllegalArgumentException(e);
			}
			return new Property(this, gssapiMechanismOid);
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
			Boolean val = Boolean.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, Boolean.valueOf(value));
		}
		
	},
	SOCKS5_GSSAPI_PROTECTION_LEVELS("socksClient.socks5.gssapiProtectionLevels") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, GssapiProtectionLevels.DEFAULT_INSTANCE);
		}

		@Override
		public Property newProperty(final Object value) {
			GssapiProtectionLevels val = GssapiProtectionLevels.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, GssapiProtectionLevels.newInstance(value));
		}
		
	},
	SOCKS5_GSSAPI_SERVICE_NAME("socksClient.socks5.gssapiServiceName") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, null);
		}

		@Override
		public Property newProperty(final Object value) {
			String val = String.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
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
			EncryptedPassword val = EncryptedPassword.class.cast(value);
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
			Arrays.fill(val, '\0');
			return new Property(this, v);
		}
		
	},
	SOCKS5_USERNAME("socksClient.socks5.username") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, System.getProperty("user.name"));
		}

		@Override
		public Property newProperty(final Object value) {
			String val = String.class.cast(value);
			UsernamePassword.validateUsername(val);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			UsernamePassword.validateUsername(value);
			return new Property(this, value);
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
			Boolean val = Boolean.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, Boolean.valueOf(value));
		}
		
	},
	SSL_ENABLED_CIPHER_SUITES("socksClient.ssl.enabledCipherSuites") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, CipherSuites.newInstance(new String[] { }));
		}

		@Override
		public Property newProperty(final Object value) {
			CipherSuites val = CipherSuites.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, CipherSuites.newInstance(value));
		}
		
	},
	SSL_ENABLED_PROTOCOLS("socksClient.ssl.enabledProtocols") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, Protocols.newInstance(new String[] { }));
		}

		@Override
		public Property newProperty(final Object value) {
			Protocols val = Protocols.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, Protocols.newInstance(value));
		}
		
	},
	SSL_KEY_STORE_FILE("socksClient.ssl.keyStoreFile") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, null);
		}

		@Override
		public Property newProperty(final Object value) {
			File val = File.class.cast(value);
			if (!val.exists()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' does not exist", 
						val));
			}
			if (!val.isFile()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' must be a file", 
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
			EncryptedPassword val = EncryptedPassword.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	},
	SSL_KEY_STORE_TYPE("socksClient.ssl.keyStoreType") {
		
		private static final String DEFAULT_STRING_VALUE = "PKCS12";
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, DEFAULT_STRING_VALUE);
		}

		@Override
		public Property newProperty(final Object value) {
			String val = String.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, value);
		}
		
	},
	SSL_PROTOCOL("socksClient.ssl.protocol") {
		
		private static final String DEFAULT_STRING_VALUE = "TLSv1";
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, DEFAULT_STRING_VALUE);
		}

		@Override
		public Property newProperty(final Object value) {
			String val = String.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, value);
		}
		
	},
	SSL_TRUST_STORE_FILE("socksClient.ssl.trustStoreFile") {
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, null);
		}

		@Override
		public Property newProperty(final Object value) {
			File val = File.class.cast(value);
			if (!val.exists()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' does not exist", 
						val));
			}
			if (!val.isFile()) {
				throw new IllegalArgumentException(String.format(
						"file `%s' must be a file", 
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
			EncryptedPassword val = EncryptedPassword.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	},
	SSL_TRUST_STORE_TYPE("socksClient.ssl.trustStoreType") {
		
		private static final String DEFAULT_STRING_VALUE = "PKCS12";
		
		@Override
		public Property getDefaultProperty() {
			return new Property(this, DEFAULT_STRING_VALUE);
		}

		@Override
		public Property newProperty(final Object value) {
			String val = String.class.cast(value);
			return new Property(this, val);
		}

		@Override
		public Property newProperty(final String value) {
			return new Property(this, value);
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
