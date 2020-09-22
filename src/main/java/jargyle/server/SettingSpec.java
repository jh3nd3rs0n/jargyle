package jargyle.server;

import java.net.UnknownHostException;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.client.SocksClient;
import jargyle.client.SocksServerUri;
import jargyle.client.socks5.Socks5Client;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.cli.HelpTextParams;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.util.NonnegativeInteger;
import jargyle.common.util.PositiveInteger;
import jargyle.server.socks5.Socks5RequestCriteria;
import jargyle.server.socks5.Socks5RequestCriterion;
import jargyle.server.socks5.UsernamePasswordAuthenticator;

public enum SettingSpec implements HelpTextParams {
	
	ALLOWED_CLIENT_ADDRESS_CRITERIA("allowedClientAddressCriteria") {

		private static final String DOC = "The space separated list of "
				+ "allowed client address criteria (default is matches:.*)";
		
		@Override
		public Setting getDefaultSetting() {
			return newSetting(Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]", 
					this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Criteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Criteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	BACKLOG("backlog") {

		private static final int DEFAULT_INT_VALUE = 50;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, NonnegativeInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The maximum length of the queue of incoming connections "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					NonnegativeInteger.MIN_INT_VALUE, 
					NonnegativeInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof NonnegativeInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						NonnegativeInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(NonnegativeInteger.newInstance(value));
		}
		
	},
	BLOCKED_CLIENT_ADDRESS_CRITERIA("blockedClientAddressCriteria") {

		private static final String DOC = "The space separated list of "
				+ "blocked client address criteria";
		
		@Override
		public Setting getDefaultSetting() {
			return newSetting(Criteria.EMPTY_INSTANCE);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]", 
					this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Criteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Criteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	CLIENT_SOCKET_SETTINGS("clientSocketSettings") {
		
		private static final String DOC = 
				"The space separated list of socket settings for the client "
				+ "socket";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_BIND_HOST("externalClient.bindHost") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocksClient.DEFAULT_BIND_HOST);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The binding host name or address for the socket to "
					+ "connect to the external SOCKS server used for external "
					+ "connections (default is %s)", 
					SocksClient.DEFAULT_BIND_HOST);
		}

		@Override
		public String getUsage() {
			return String.format("%s=HOST", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof String)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						String.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return new Setting(this, value);
		}
		
	},
	EXTERNAL_CLIENT_CONNECT_TIMEOUT("externalClient.connectTimeout") {

		private static final int DEFAULT_INT_VALUE = 
				SocksClient.DEFAULT_CONNECT_TIMEOUT;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on waiting for the socket to "
					+ "connect to the external SOCKS server used for external "
					+ "connections (default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_EXTERNAL_SERVER_URI("externalClient.externalServerUri") {
		
		private static final String DOC = 
				"The URI of the external SOCKS server used for external "
				+ "connections.";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format("%s=SCHEME://HOST[:PORT]", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocksServerUri)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocksServerUri.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocksServerUri.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKET_SETTINGS("externalClient.socketSettings") {

		private static final String DOC = 
				"The space separated list of socket settings for the socket "
				+ "to connect to the external SOCKS server used for external "
				+ "connections";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocksClient.DEFAULT_SOCKET_SETTINGS);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
			
	},
	EXTERNAL_CLIENT_SOCKS5_AUTH_METHODS("externalClient.socks5.authMethods") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Socks5Client.DEFAULT_AUTH_METHODS);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The space separated list of acceptable authentication "
					+ "methods to the external SOCKS5 server used for external "
					+ "connections (default is %s)",
					Socks5Client.DEFAULT_AUTH_METHODS);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=%2$s1[ %2$s2[...]]", 
					this, 
					"SOCKS5_AUTH_METHOD");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof AuthMethods)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						AuthMethods.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(AuthMethods.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_MECHANISM_OID(
			"externalClient.socks5.gssapiMechanismOid") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Socks5Client.DEFAULT_GSSAPI_MECHANISM_OID);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The object ID for the GSS-API authentication mechanism to "
					+ "the external SOCKS5 server used for external "
					+ "connections (default is %s)", 
					Socks5Client.DEFAULT_GSSAPI_MECHANISM_OID);
		}

		@Override
		public String getUsage() {
			return String.format("%s=GSSAPI_MECHANISM_OID", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Oid)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Oid.class.getName()));
			}
			return new Setting(this, value);
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
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL(
			"externalClient.socks5.gssapiNecReferenceImpl") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = 
				Socks5Client.DEFAULT_GSSAPI_NEC_REFERENCE_IMPL;

		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected should the external SOCKS5 server used for "
					+ "external connections use the NEC reference "
					+ "implementation (default is %s)", 
					DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format("%s=true|false", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Boolean)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Boolean.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_PROTECTION_LEVELS(
			"externalClient.socks5.gssapiProtectionLevels") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, Socks5Client.DEFAULT_GSSAPI_PROTECTION_LEVELS);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The space separated list of acceptable protection levels "
					+ "after GSS-API authentication with the external SOCKS5 "
					+ "server used for external connections (The first is "
					+ "preferred. The remaining are acceptable if the server "
					+ "does not accept the first.) (default is %s)",
					Socks5Client.DEFAULT_GSSAPI_PROTECTION_LEVELS);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=%2$s1[ %2$s2[...]]", 
					this, 
					"SOCKS5_GSSAPI_PROTECTION_LEVEL");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof GssapiProtectionLevels)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						GssapiProtectionLevels.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(GssapiProtectionLevels.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_SERVICE_NAME(
			"externalClient.socks5.gssapiServiceName") {
		
		private static final String DOC = "The GSS-API service name for the "
				+ "external SOCKS5 server used for external connections";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format("%s=GSSAPI_SERVICE_NAME", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof String)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						String.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return new Setting(this, value);
		}
		
	},
	EXTERNAL_CLIENT_SOCKS5_USERNAME_PASSWORD(
			"externalClient.socks5.usernamePassword") {

		private static final String DOC = "The username password to be used "
				+ "to access the external SOCKS5 server used for external "
				+ "connections";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format("%s=USERNAME:PASSWORD", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof UsernamePassword)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						UsernamePassword.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(UsernamePassword.newInstance(value));
		}
		
	},
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
		public String getDoc() {
			return String.format(
					"The host name or address for the SOCKS server (default is %s)", 
					DEFAULT_HOST);
		}

		@Override
		public String getUsage() {
			return String.format("%s=HOST", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Host)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Host.class.getName()));
			}
			return new Setting(this, value);
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
	PORT("port") {
		
		private static final int DEFAULT_INT_VALUE = 1080;
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, Port.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The port for the SOCKS server (default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					Port.MIN_INT_VALUE, 
					Port.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Port)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Port.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Port.newInstance(value));
		}
		
	},
	SOCKET_SETTINGS("socketSettings") {

		private static final String DOC = 
				"The space separated list of socket settings for the SOCKS "
				+ "server";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
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
			return newSetting(new Socks5RequestCriteria(
					new Socks5RequestCriterion(null, null, null, null)));
		}

		@Override
		public String getDoc() {
			return null;
		}

		@Override
		public String getUsage() {
			return null;
		}
		
		@Override
		public boolean isDisplayable() {
			return false;
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Socks5RequestCriteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Socks5RequestCriteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			throw new UnsupportedOperationException(String.format(
					"%s does not accept a String representation of %s",
					this,
					Socks5RequestCriteria.class.getName()));
		}
		
	},
	SOCKS5_AUTH_METHODS("socks5.authMethods") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, AuthMethods.newInstance(
					AuthMethod.NO_AUTHENTICATION_REQUIRED));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The space separated list of acceptable authentication "
					+ "methods in order of preference (default is %s)",
					AuthMethod.NO_AUTHENTICATION_REQUIRED);
		}

		@Override
		public String getUsage() {
			return String.format("%1$s=%2$s1[ %2$s2[...]]", 
					this, 
					"SOCKS5_AUTH_METHOD");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof AuthMethods)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						AuthMethods.class.getName()));
			}
			return new Setting(this, value);
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
			return newSetting(Socks5RequestCriteria.EMPTY_INSTANCE);
		}

		@Override
		public String getDoc() {
			return null;
		}

		@Override
		public String getUsage() {
			return null;
		}
		
		@Override
		public boolean isDisplayable() {
			return false;
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Socks5RequestCriteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Socks5RequestCriteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			throw new UnsupportedOperationException(String.format(
					"%s does not accept a String representation of %s",
					this,
					Socks5RequestCriteria.class.getName()));
		}
		
	},
	SOCKS5_GSSAPI_NEC_REFERENCE_IMPL("socks5.gssapiNecReferenceImpl") {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;

		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected according to the NEC reference "
					+ "implementation (default is %s)", 
					DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format("%s=true|false", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Boolean)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Boolean.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	SOCKS5_GSSAPI_PROTECTION_LEVELS("socks5.gssapiProtectionLevels") {
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, GssapiProtectionLevels.DEFAULT_INSTANCE);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The space separated list of acceptable protection levels "
					+ "after GSS-API authentication (The first is preferred "
					+ "if the client does not provide a protection level that "
					+ "is acceptable.) (default is %s)",
					GssapiProtectionLevels.DEFAULT_INSTANCE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=%2$s1[ %2$s2[...]]", 
					this, 
					"SOCKS5_GSSAPI_PROTECTION_LEVEL");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof GssapiProtectionLevels)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						GssapiProtectionLevels.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(GssapiProtectionLevels.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onBind.allowedExternalIncomingAddressCriteria") {

		private static final String DOC = "The space separated list of "
				+ "allowed external incoming address criteria (default is "
				+ "matches:.*)";
		
		@Override
		public Setting getDefaultSetting() {
			return newSetting(Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]", 
					this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Criteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Criteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onBind.blockedExternalIncomingAddressCriteria") {

		private static final String DOC = "The space separated list of "
				+ "blocked external incoming address criteria";
		
		@Override
		public Setting getDefaultSetting() {
			return newSetting(Criteria.EMPTY_INSTANCE);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]", 
					this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Criteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Criteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_EXTERNAL_INCOMING_SOCKET_SETTINGS(
			"socks5.onBind.externalIncomingSocketSettings") {
		
		private static final String DOC = 
				"The space separated list of socket settings for the external "
				+ "incoming socket";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS(
			"socks5.onBind.listenSocketSettings") {
		
		private static final String DOC = 
				"The space separated list of socket settings for the listen "
				+ "socket";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_RELAY_BUFFER_SIZE("socks5.onBind.relayBufferSize") {
		
		private static final int DEFAULT_INT_VALUE = 1024;
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The buffer size in bytes for relaying the data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_RELAY_TIMEOUT("socks5.onBind.relayTimeout") {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on relaying no data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE("socks5.onConnect.relayBufferSize") {
		
		private static final int DEFAULT_INT_VALUE = 1024;
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The buffer size in bytes for relaying the data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_CONNECT_RELAY_TIMEOUT("socks5.onConnect.relayTimeout") {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on relaying no data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
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
		public String getDoc() {
			return String.format(
					"The binding host name or address for the server-facing "
					+ "socket (default is %s)", 
					DEFAULT_BIND_HOST);
		}

		@Override
		public String getUsage() {
			return String.format("%s=HOST", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Host)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Host.class.getName()));
			}
			return new Setting(this, value);
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
	SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT(
			"socks5.onConnect.serverConnectTimeout") {

		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on waiting the "
					+ "server-facing socket to connect (default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_CONNECT_SERVER_SOCKET_SETTINGS(
			"socks5.onConnect.serverSocketSettings") {
		
		private static final String DOC = 
				"The space separated list of socket settings for the "
				+ "server-facing socket";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onUdpAssociate.allowedExternalIncomingAddressCriteria") {

		private static final String DOC = "The space separated list of "
				+ "allowed external incoming address criteria (default is "
				+ "matches:.*)";
		
		@Override
		public Setting getDefaultSetting() {
			return newSetting(Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]", 
					this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Criteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Criteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_INCOMING_ADDRESS_CRITERIA(
			"socks5.onUdpAssociate.blockedExternalIncomingAddressCriteria") {

		private static final String DOC = "The space separated list of "
				+ "blocked external incoming address criteria";
		
		@Override
		public Setting getDefaultSetting() {
			return newSetting(Criteria.EMPTY_INSTANCE);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]", 
					this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Criteria)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Criteria.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Criteria.newInstance(value));
		}
		
	},
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
		public String getDoc() {
			return String.format(
					"The binding host name or address for the client-facing "
					+ "UDP socket (default is %s)", 
					DEFAULT_BIND_HOST);
		}

		@Override
		public String getUsage() {
			return String.format("%s=HOST", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Host)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Host.class.getName()));
			}
			return new Setting(this, value);
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
	SOCKS5_ON_UDP_ASSOCIATE_CLIENT_SOCKET_SETTINGS(
			"socks5.onUdpAssociate.clientSocketSettings") {
		
		private static final String DOC = 
				"The space separated list of socket settings for the "
				+ "client-facing UDP socket";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE(
			"socks5.onUdpAssociate.relayBufferSize") {
		
		private static final int DEFAULT_INT_VALUE = 32768;
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The buffer size in bytes for relaying the data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT(
			"socks5.onUdpAssociate.relayTimeout") {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					this, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on relaying no data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					this, 
					PositiveInteger.MIN_INT_VALUE, 
					PositiveInteger.MAX_INT_VALUE);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof PositiveInteger)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						PositiveInteger.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
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
		public String getDoc() {
			return String.format(
					"The binding host name or address for the server-facing "
					+ "UDP socket (default is %s)", 
					DEFAULT_BIND_HOST);
		}

		@Override
		public String getUsage() {
			return String.format("%s=HOST", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Host)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Host.class.getName()));
			}
			return new Setting(this, value);
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
	SOCKS5_ON_UDP_ASSOCIATE_SERVER_SOCKET_SETTINGS(
			"socks5.onUdpAssociate.serverSocketSettings") {
		
		private static final String DOC = 
				"The space separated list of socket settings for the "
				+ "server-facing UDP socket";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					this, 
					"SOCKET_SETTING");
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocketSettings)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocketSettings.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_USERNAME_PASSWORD_AUTHENTICATOR(
			"socks5.usernamePasswordAuthenticator") {

		private static final String DOC = "The username password "
				+ "authenticator for the SOCKS5 server";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(this, null);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getUsage() {
			return String.format("%s=CLASSNAME[:VALUE]", this);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof UsernamePasswordAuthenticator)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						UsernamePasswordAuthenticator.class.getName()));
			}
			return new Setting(this, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(UsernamePasswordAuthenticator.getInstance(value));
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
	
	@Override
	public boolean isDisplayable() {
		return true;
	}
	
	public abstract Setting newSetting(final Object value);
	
	public abstract Setting newSetting(final String value);
	
	@Override
	public String toString() {
		return this.string;
	}
	
}
