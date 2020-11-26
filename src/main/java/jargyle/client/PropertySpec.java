package jargyle.client;

import java.net.UnknownHostException;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.client.socks5.EncryptedPassword;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.Host;
import jargyle.common.net.Port;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
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
			if (!(value instanceof char[])) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						char[].class.getName()));
			}
			char[] val = (char[]) value;
			UsernamePassword.validatePassword(val);
			EncryptedPassword v = EncryptedPassword.newInstance(val);
			return new Property(this, v);
		}

		@Override
		public Property newProperty(final String value) {
			char[] val = value.toCharArray();
			UsernamePassword.validatePassword(val);
			EncryptedPassword v = EncryptedPassword.newInstance(val);
			return new Property(this, v);
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
