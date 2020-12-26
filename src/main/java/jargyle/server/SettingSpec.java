package jargyle.server;

import java.io.File;
import java.net.UnknownHostException;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.client.PropertySpec;
import jargyle.client.SocksServerUri;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.annotation.HelpText;
import jargyle.common.net.Host;
import jargyle.common.net.Port;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.net.ssl.CipherSuites;
import jargyle.common.net.ssl.Protocols;
import jargyle.common.security.EncryptedPassword;
import jargyle.common.util.NonnegativeInteger;
import jargyle.common.util.PositiveInteger;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.Socks5RequestCriterion;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public enum SettingSpec {
	
	@HelpText(
			doc = "The space separated list of allowed client address "
					+ "criteria (default is matches:.*)", 
			usage = "allowedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	ALLOWED_CLIENT_ADDRESS_CRITERIA("allowedClientAddressCriteria") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
		}

		@Override
		public Setting newSetting(final Object value) {
			Criteria val = Criteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The maximum length of the queue of incoming connections "
					+ "(default is 50)", 
			usage = "backlog=INTEGER_BETWEEN_0_AND_2147483647"
	)
	BACKLOG("backlog") {

		private static final int DEFAULT_INT_VALUE = 50;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, NonnegativeInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			NonnegativeInteger val = NonnegativeInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(NonnegativeInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of blocked client address criteria", 
			usage = "blockedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	BLOCKED_CLIENT_ADDRESS_CRITERIA("blockedClientAddressCriteria") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Criteria.EMPTY_INSTANCE);
		}

		@Override
		public Setting newSetting(final Object value) {
			Criteria val = Criteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the client "
					+ "socket", 
			usage = "clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	CLIENT_SOCKET_SETTINGS("clientSocketSettings") {
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The binding host name or address for the socket to connect "
					+ "to the external SOCKS server used for external "
					+ "connections (default is 0.0.0.0)", 
			usage = "externalClient.bindHost=HOST"
	)
	EXTERNAL_CLIENT_BIND_HOST("externalClient.bindHost") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.BIND_HOST.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			Host val = Host.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return new Setting(this, value);
		}
		
	},
	@HelpText(
			doc = "The timeout in milliseconds on waiting for the socket to "
					+ "connect to the external SOCKS server used for external "
					+ "connections (default is 60000)", 
			usage = "externalClient.connectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	EXTERNAL_CLIENT_CONNECT_TIMEOUT("externalClient.connectTimeout") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.CONNECT_TIMEOUT.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The URI of the external SOCKS server used for external "
					+ "connections", 
			usage = "externalClient.externalServerUri=SCHEME://HOST[:PORT]"
	)
	EXTERNAL_CLIENT_EXTERNAL_SERVER_URI("externalClient.externalServerUri") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocksServerUri.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocksServerUri val = SocksServerUri.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocksServerUri.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the socket "
					+ "to connect to the external SOCKS server used for "
					+ "external connections", 
			usage = "externalClient.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	EXTERNAL_CLIENT_SOCKET_SETTINGS("externalClient.socketSettings") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.SOCKET_SETTINGS.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
			
	},
	@HelpText(
			doc = "The space separated list of acceptable authentication "
					+ "methods to the external SOCKS5 server used for external "
					+ "connections (default is NO_AUTHENTICATION_REQUIRED)", 
			usage = "externalClient.socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]"
	)
	EXTERNAL_CLIENT_SOCKS5_AUTH_METHODS("externalClient.socks5.authMethods") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.SOCKS5_AUTH_METHODS.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			AuthMethods val = AuthMethods.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(AuthMethods.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The object ID for the GSS-API authentication mechanism to "
					+ "the external SOCKS5 server used for external "
					+ "connections (default is 1.2.840.113554.1.2.2)", 
			usage = "externalClient.socks5.gssapiMechanismOid=GSSAPI_MECHANISM_OID"
	)
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_MECHANISM_OID(
			"externalClient.socks5.gssapiMechanismOid") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.SOCKS5_GSSAPI_MECHANISM_OID.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			Oid val = Oid.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			Oid gssapiMechanismOid = null;
			try {
				gssapiMechanismOid = new Oid(value);
			} catch (GSSException e) {
				throw new IllegalArgumentException(e);
			}
			return newSetting(gssapiMechanismOid);
		}
		
	},
	@HelpText(
			doc = "The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected should the external SOCKS5 server used for "
					+ "external connections use the NEC reference "
					+ "implementation (default is false)", 
			usage = "externalClient.socks5.gssapiNecReferenceImpl=true|false"
	)
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL(
			"externalClient.socks5.gssapiNecReferenceImpl") {

		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of acceptable protection levels "
					+ "after GSS-API authentication with the external SOCKS5 "
					+ "server used for external connections (The first is "
					+ "preferred. The remaining are acceptable if the server "
					+ "does not accept the first.) (default is "
					+ "REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)", 
			usage = "externalClient.socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]"
	)
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_PROTECTION_LEVELS(
			"externalClient.socks5.gssapiProtectionLevels") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			GssapiProtectionLevels val = GssapiProtectionLevels.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(GssapiProtectionLevels.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The GSS-API service name for the external SOCKS5 server "
					+ "used for external connections", 
			usage = "externalClient.socks5.gssapiServiceName=GSSAPI_SERVICE_NAME"
	)
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_SERVICE_NAME(
			"externalClient.socks5.gssapiServiceName") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			String val = String.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return new Setting(this, value);
		}
		
	},
	@HelpText(
			doc = "The username password to be used to access the external "
					+ "SOCKS5 server used for external connections", 
			usage = "externalClient.socks5.usernamePassword=USERNAME:PASSWORD"
	)
	EXTERNAL_CLIENT_SOCKS5_USERNAME_PASSWORD(
			"externalClient.socks5.usernamePassword") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public Setting newSetting(final Object value) {
			UsernamePassword val = UsernamePassword.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(UsernamePassword.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The boolean value to indicate if SSL/TLS connections to "
					+ "the external SOCKS server for external connections are "
					+ "enabled (default is false)",
			usage = "externalClient.ssl.enabled=true|false"
	)
	EXTERNAL_CLIENT_SSL_ENABLED("externalClient.ssl.enabled") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_ENABLED.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of cipher suites enabled for "
					+ "SSL/TLS connections to the external SOCKS server for "
					+ "external connections",
			usage = "externalClient.ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[...]]]"
	)
	EXTERNAL_CLIENT_SSL_ENABLED_CIPHER_SUITES(
			"externalClient.ssl.enabledCipherSuites") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_ENABLED_CIPHER_SUITES.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			CipherSuites val = CipherSuites.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(CipherSuites.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of protocol versions enabled for "
					+ "SSL/TLS connections to the external SOCKS server for "
					+ "external connections",
			usage = "externalClient.ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[...]]]"
	)	
	EXTERNAL_CLIENT_SSL_ENABLED_PROTOCOLS(
			"externalClient.ssl.enabledProtocols") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_ENABLED_PROTOCOLS.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			Protocols val = Protocols.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Protocols.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The key store file for the SSL/TLS connections to the "
					+ "external SOCKS server for external connections",
			usage = "externalClient.ssl.keyStoreFile=FILE"
	)
	EXTERNAL_CLIENT_SSL_KEY_STORE_FILE("externalClient.ssl.keyStoreFile") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_KEY_STORE_FILE.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			File val = File.class.cast(value);
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
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(new File(value));
		}
		
	},
	@HelpText(
			doc = "The password for the key store for the SSL/TLS connections "
					+ "to the external SOCKS server for external connections",
			usage = "externalClient.ssl.keyStorePassword=PASSWORD"
	)
	EXTERNAL_CLIENT_SSL_KEY_STORE_PASSWORD(
			"externalClient.ssl.keyStorePassword") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_KEY_STORE_PASSWORD.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			EncryptedPassword val = EncryptedPassword.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	},
	@HelpText(
			doc = "The type of key store file for the SSL/TLS connections to "
					+ "the external SOCKS server for external connections",
			usage = "externalClient.ssl.keyStoreType=TYPE"
	)	
	EXTERNAL_CLIENT_SSL_KEY_STORE_TYPE("externalClient.ssl.keyStoreType") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, 
					PropertySpec.SSL_KEY_STORE_TYPE.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			String val = String.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(String value) {
			return newSetting(value);
		}
	},
	@HelpText(
			doc = "The trust store file for the SSL/TLS connections to the "
					+ "external SOCKS server for external connections",
			usage = "externalClient.ssl.trustStoreFile=FILE"
	)	
	EXTERNAL_CLIENT_SSL_TRUST_STORE_FILE("externalClient.ssl.trustStoreFile") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_TRUST_STORE_FILE.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			File val = File.class.cast(value);
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
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(new File(value));
		}
		
	},
	@HelpText(
			doc = "The password for the trust store for the SSL/TLS "
					+ "connections to the external SOCKS server for external "
					+ "connections",
			usage = "externalClient.ssl.trustStorePassword=PASSWORD"
	)	
	EXTERNAL_CLIENT_SSL_TRUST_STORE_PASSWORD(
			"externalClient.ssl.trustStorePassword") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_TRUST_STORE_PASSWORD.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			EncryptedPassword val = EncryptedPassword.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	},
	@HelpText(
			doc = "The type of trust store file for the SSL/TLS connections to "
					+ "the external SOCKS server for external connections",
			usage = "externalClient.ssl.trustStoreType=TYPE"
	)	
	EXTERNAL_CLIENT_SSL_TRUST_STORE_TYPE("externalClient.ssl.trustStoreType") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this,
					PropertySpec.SSL_TRUST_STORE_TYPE.getDefaultProperty().getValue());
		}

		@Override
		public Setting newSetting(final Object value) {
			String val = String.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(value);
		}
		
	},
	@HelpText(
			doc = "The host name or address for the SOCKS server (default is "
					+ "0.0.0.0)", 
			usage = "host=HOST"
	)
	HOST("host") {

		private static final String DEFAULT_HOST = "0.0.0.0";
		
		@Override
		public Setting getDefaultSetting() {
			Host host = null;
			try {
				host = Host.newInstance(DEFAULT_HOST);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return new Setting(this, host);
		}

		@Override
		public Setting newSetting(final Object value) {
			Host val = Host.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			Host host = null;
			try {
				host = Host.newInstance(value);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			return newSetting(host);
		}
		
	},
	@HelpText(
			doc = "The port for the SOCKS server (default is 1080)", 
			usage = "port=INTEGER_BETWEEN_0_AND_65535"
	)
	PORT("port") {
		
		private static final int DEFAULT_INT_VALUE = 1080;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Port.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			Port val = Port.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Port.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the SOCKS "
					+ "server", 
			usage = "socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	SOCKET_SETTINGS("socketSettings") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ALLOWED_SOCKS5_REQUEST_CRITERIA(
			"socks5.allowedSocks5RequestCriteria") {

		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, new Socks5RequestCriteria(
					new Socks5RequestCriterion(null, null, null, null)));
		}

		@Override
		public Setting newSetting(final Object value) {
			Socks5RequestCriteria val = Socks5RequestCriteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			throw new UnsupportedOperationException(String.format(
					"%s does not accept a String representation of %s",
					this,
					Socks5RequestCriteria.class.getName()));
		}
		
	},
	@HelpText(
			doc = "The space separated list of acceptable authentication "
					+ "methods in order of preference (default is "
					+ "NO_AUTHENTICATION_REQUIRED)", 
			usage = "socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]"
	)
	SOCKS5_AUTH_METHODS("socks5.authMethods") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, AuthMethods.newInstance(
					AuthMethod.NO_AUTHENTICATION_REQUIRED));
		}

		@Override
		public Setting newSetting(final Object value) {
			AuthMethods val = AuthMethods.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(AuthMethods.newInstance(value));
		}
		
	},
	SOCKS5_BLOCKED_SOCKS5_REQUEST_CRITERIA(
			"socks5.blockedSocks5RequestCriteria") {

		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Socks5RequestCriteria.EMPTY_INSTANCE);
		}

		@Override
		public Setting newSetting(final Object value) {
			Socks5RequestCriteria val = Socks5RequestCriteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			throw new UnsupportedOperationException(String.format(
					"%s does not accept a String representation of %s",
					this,
					Socks5RequestCriteria.class.getName()));
		}
		
	},
	@HelpText(
			doc = "The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected according to the NEC reference "
					+ "implementation (default is false)", 
			usage = "socks5.gssapiNecReferenceImpl=true|false"
	)
	SOCKS5_GSSAPI_NEC_REFERENCE_IMPL("socks5.gssapiNecReferenceImpl") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;

		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Boolean.valueOf(DEFAULT_BOOLEAN_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of acceptable protection levels "
					+ "after GSS-API authentication (The first is preferred "
					+ "if the client does not provide a protection level that "
					+ "is acceptable.) (default is REQUIRED_INTEG_AND_CONF "
					+ "REQUIRED_INTEG NONE)", 
			usage = "socks5.gssapiProtectionLevels=SOCKS5_GSSAPI_PROTECTION_LEVEL1[ SOCKS5_GSSAPI_PROTECTION_LEVEL2[...]]"
	)
	SOCKS5_GSSAPI_PROTECTION_LEVELS("socks5.gssapiProtectionLevels") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, GssapiProtectionLevels.DEFAULT_INSTANCE);
		}

		@Override
		public Setting newSetting(final Object value) {
			GssapiProtectionLevels val = GssapiProtectionLevels.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(GssapiProtectionLevels.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of allowed external incoming "
					+ "address criteria (default is matches:.*)", 
			usage = "socks5.onBind.allowedExternalIncomingAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	SOCKS5_ON_BIND_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onBind.allowedExternalIncomingAddressCriteria") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
		}

		@Override
		public Setting newSetting(final Object value) {
			Criteria val = Criteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of blocked external incoming "
					+ "address criteria", 
			usage = "socks5.onBind.blockedExternalIncomingAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	SOCKS5_ON_BIND_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onBind.blockedExternalIncomingAddressCriteria") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Criteria.EMPTY_INSTANCE);
		}

		@Override
		public Setting newSetting(final Object value) {
			Criteria val = Criteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "external incoming socket", 
			usage = "socks5.onBind.externalIncomingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	SOCKS5_ON_BIND_EXTERNAL_INCOMING_SOCKET_SETTINGS(
			"socks5.onBind.externalIncomingSocketSettings") {
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the listen "
					+ "socket", 
			usage = "socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS(
			"socks5.onBind.listenSocketSettings") {
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "1024)", 
			usage = "socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SOCKS5_ON_BIND_RELAY_BUFFER_SIZE("socks5.onBind.relayBufferSize") {
		
		private static final int DEFAULT_INT_VALUE = 1024;
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onBind.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SOCKS5_ON_BIND_RELAY_TIMEOUT("socks5.onBind.relayTimeout") {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "1024)", 
			usage = "socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE("socks5.onConnect.relayBufferSize") {
		
		private static final int DEFAULT_INT_VALUE = 1024;
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onConnect.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SOCKS5_ON_CONNECT_RELAY_TIMEOUT("socks5.onConnect.relayTimeout") {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The binding host name or address for the server-facing "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onConnect.serverBindHost=HOST"
	)
	SOCKS5_ON_CONNECT_SERVER_BIND_HOST("socks5.onConnect.serverBindHost") {
		
		private static final String DEFAULT_BIND_HOST = "0.0.0.0";
		
		@Override
		public Setting getDefaultSetting() {
			Host host = null;
			try {
				host = Host.newInstance(DEFAULT_BIND_HOST);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return new Setting(this, host);
		}

		@Override
		public Setting newSetting(final Object value) {
			Host val = Host.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			Host host = null;
			try {
				host = Host.newInstance(value);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			return newSetting(host);
		}
		
	},
	@HelpText(
			doc = "The timeout in milliseconds on waiting the server-facing "
					+ "socket to connect (default is 60000)", 
			usage = "socks5.onConnect.serverConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT(
			"socks5.onConnect.serverConnectTimeout") {

		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "server-facing socket", 
			usage = "socks5.onConnect.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	SOCKS5_ON_CONNECT_SERVER_SOCKET_SETTINGS(
			"socks5.onConnect.serverSocketSettings") {
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of allowed external incoming "
					+ "address criteria (default is matches:.*)", 
			usage = "socks5.onUdpAssociate.allowedExternalIncomingAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onUdpAssociate.allowedExternalIncomingAddressCriteria") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
		}

		@Override
		public Setting newSetting(final Object value) {
			Criteria val = Criteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of blocked external incoming "
					+ "address criteria", 
			usage = "socks5.onUdpAssociate.blockedExternalIncomingAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onUdpAssociate.blockedExternalIncomingAddressCriteria") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Criteria.EMPTY_INSTANCE);
		}

		@Override
		public Setting newSetting(final Object value) {
			Criteria val = Criteria.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The binding host name or address for the client-facing UDP "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onUdpAssociate.clientBindHost=HOST"
	)
	SOCKS5_ON_UDP_ASSOCIATE_CLIENT_BIND_HOST(
			"socks5.onUdpAssociate.clientBindHost") {
		
		private static final String DEFAULT_BIND_HOST = "0.0.0.0";
		
		@Override
		public Setting getDefaultSetting() {
			Host host = null;
			try {
				host = Host.newInstance(DEFAULT_BIND_HOST);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return new Setting(this, host);
		}

		@Override
		public Setting newSetting(final Object value) {
			Host val = Host.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			Host host = null;
			try {
				host = Host.newInstance(value);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			return newSetting(host);
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "client-facing UDP socket", 
			usage = "socks5.onUdpAssociate.clientSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	SOCKS5_ON_UDP_ASSOCIATE_CLIENT_SOCKET_SETTINGS(
			"socks5.onUdpAssociate.clientSocketSettings") {
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "32768)", 
			usage = "socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE(
			"socks5.onUdpAssociate.relayBufferSize") {
		
		private static final int DEFAULT_INT_VALUE = 32768;
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onUdpAssociate.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT(
			"socks5.onUdpAssociate.relayTimeout") {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			PositiveInteger val = PositiveInteger.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The binding host name or address for the server-facing UDP "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onUdpAssociate.serverBindHost=HOST"
	)
	SOCKS5_ON_UDP_ASSOCIATE_SERVER_BIND_HOST(
			"socks5.onUdpAssociate.serverBindHost") {
		
		private static final String DEFAULT_BIND_HOST = "0.0.0.0";
		
		@Override
		public Setting getDefaultSetting() {
			Host host = null;
			try {
				host = Host.newInstance(DEFAULT_BIND_HOST);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return new Setting(this, host);
		}

		@Override
		public Setting newSetting(final Object value) {
			Host val = Host.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			Host host = null;
			try {
				host = Host.newInstance(value);
			} catch (UnknownHostException e) {
				throw new IllegalArgumentException(e);
			}
			return newSetting(host);
		}
		
	},
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "server-facing UDP socket", 
			usage = "socks5.onUdpAssociate.serverSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	SOCKS5_ON_UDP_ASSOCIATE_SERVER_SOCKET_SETTINGS(
			"socks5.onUdpAssociate.serverSocketSettings") {
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public Setting newSetting(final Object value) {
			SocketSettings val = SocketSettings.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The username password authenticator for the SOCKS5 server", 
			usage = "socks5.usernamePasswordAuthenticator=CLASSNAME[:VALUE]"
	)
	SOCKS5_USERNAME_PASSWORD_AUTHENTICATOR(
			"socks5.usernamePasswordAuthenticator") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public Setting newSetting(final Object value) {
			UsernamePasswordAuthenticator val = 
					UsernamePasswordAuthenticator.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(UsernamePasswordAuthenticator.getInstance(value));
		}
		
	},
	@HelpText(
			doc = "The boolean value to indicate if SSL/TLS connections to "
					+ "the SOCKS server are enabled (default is false)",
			usage = "ssl.enabled=true|false"
	)	
	SSL_ENABLED("ssl.enabled") {
	
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Boolean.valueOf(DEFAULT_BOOLEAN_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of cipher suites enabled for "
					+ "SSL/TLS connections to the SOCKS server",
			usage = "ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[...]]]"
	)	
	SSL_ENABLED_CIPHER_SUITES("ssl.enabledCipherSuites") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, CipherSuites.newInstance(new String[] { }));
		}

		@Override
		public Setting newSetting(final Object value) {
			CipherSuites val = CipherSuites.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(CipherSuites.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The space separated list of protocol versions enabled for "
					+ "SSL/TLS connections to the SOCKS server",
			usage = "ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[...]]]"
	)	
	SSL_ENABLED_PROTOCOLS("ssl.enabledProtocols") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Protocols.newInstance(new String[] { }));
		}

		@Override
		public Setting newSetting(final Object value) {
			Protocols val = Protocols.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Protocols.newInstance(value));
		}
		
	},
	@HelpText(
			doc = "The key store file for the SSL/TLS connections to the SOCKS "
					+ "server",
			usage = "ssl.keyStoreFile=FILE"
	)	
	SSL_KEY_STORE_FILE("ssl.keyStoreFile") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public Setting newSetting(final Object value) {
			File val = File.class.cast(value);
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
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(new File(value));
		}
		
	},
	@HelpText(
			doc = "The password for the key store for the SSL/TLS connections "
					+ "to the SOCKS server",
			usage = "ssl.keyStorePassword=PASSWORD"
	)	
	SSL_KEY_STORE_PASSWORD("ssl.keyStorePassword") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, EncryptedPassword.newInstance(new char[] { }));
		}

		@Override
		public Setting newSetting(final Object value) {
			EncryptedPassword val = EncryptedPassword.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	},
	@HelpText(
			doc = "The type of key store file for the SSL/TLS connections to "
					+ "the SOCKS server",
			usage = "ssl.keyStoreType=TYPE"
	)	
	SSL_KEY_STORE_TYPE("ssl.keyStoreType") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public Setting newSetting(final Object value) {
			String val = String.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(value);
		}
		
	},
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "required for SSL/TLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "ssl.needClientAuth=true|false"
	)	
	SSL_NEED_CLIENT_AUTH("ssl.needClientAuth") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Boolean.valueOf(DEFAULT_BOOLEAN_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	@HelpText(
			doc = "The trust store file for the SSL/TLS connections to the "
					+ "SOCKS server",
			usage = "ssl.trustStoreFile=FILE"
	)	
	SSL_TRUST_STORE_FILE("ssl.trustStoreFile") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public Setting newSetting(final Object value) {
			File val = File.class.cast(value);
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
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(new File(value));
		}
		
	},
	@HelpText(
			doc = "The password for the trust store for the SSL/TLS "
					+ "connections to the SOCKS server",
			usage = "ssl.trustStorePassword=PASSWORD"
	)	
	SSL_TRUST_STORE_PASSWORD("ssl.trustStorePassword") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, EncryptedPassword.newInstance(new char[] { }));
		}

		@Override
		public Setting newSetting(final Object value) {
			EncryptedPassword val = EncryptedPassword.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(EncryptedPassword.newInstance(value.toCharArray()));
		}
		
	},
	@HelpText(
			doc = "The type of trust store file for the SSL/TLS connections to "
					+ "the SOCKS server",
			usage = "ssl.trustStoreType=TYPE"
	)		
	SSL_TRUST_STORE_TYPE("ssl.trustStoreType") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public Setting newSetting(final Object value) {
			String val = String.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(value);
		}
		
	},
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "requested for SSL/TLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "ssl.wantClientAuth=true|false"
	)	
	SSL_WANT_CLIENT_AUTH("ssl.wantClientAuth") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Boolean.valueOf(DEFAULT_BOOLEAN_VALUE));
		}

		@Override
		public Setting newSetting(final Object value) {
			Boolean val = Boolean.class.cast(value);
			return new Setting(this, val);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}

	};

	public static SettingSpec getInstance(final String s) {
		for (SettingSpec settingSpec : SettingSpec.values()) {
			if (settingSpec.toString().equals(s)) {
				return settingSpec;
			}
		}
		throw new IllegalArgumentException(String.format(
				"unknown setting: %s", s));
	}
	
	private final String string;
	
	private SettingSpec(final String s) {
		this.string = s;
	}
	
	public abstract Setting getDefaultSetting();
	
	public abstract Setting newSetting(final Object value);
	
	public abstract Setting newSetting(final String value);
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
