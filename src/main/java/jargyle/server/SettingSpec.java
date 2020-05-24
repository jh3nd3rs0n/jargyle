package jargyle.server;

import java.net.UnknownHostException;

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import jargyle.client.SocksClient;
import jargyle.client.SocksServerUri;
import jargyle.client.socks5.Socks5Client;
import jargyle.common.cli.HelpTextParams;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethod;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.util.NonnegativeInteger;
import jargyle.common.util.PositiveInteger;

public enum SettingSpec implements HelpTextParams {
	
	BACKLOG {

		private static final int DEFAULT_INT_VALUE = 50;
		private static final String NAME = "backlog";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, NonnegativeInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The maximum length of the queue of incoming connections "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(NonnegativeInteger.newInstance(value));
		}
		
	},	
	CLIENT_SOCKET_SETTINGS {
		
		private static final String DOC = 
				"The space separated list of socket settings for the client "
				+ "socket";
		private static final String NAME = "clientSocketSettings";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_BIND_HOST {
		
		private static final String NAME = "externalClient.bindHost";
		
		@Override
		public String getDoc() {
			return String.format(
					"The binding host or address for the socket to connect to "
					+ "the external SOCKS server for external connections "
					+ "(default is %s)", 
					SocksClient.DEFAULT_BIND_HOST);
		}

		@Override
		public String getUsage() {
			return String.format("%s=HOST", NAME);
		}

		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocksClient.DEFAULT_BIND_HOST);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof String)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						String.class.getName()));
			}
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return new Setting(NAME, value);
		}
		
	},
	EXTERNAL_CLIENT_BIND_PORT {
		
		private static final int DEFAULT_INT_VALUE = 
				SocksClient.DEFAULT_BIND_PORT;
		private static final String NAME = "externalClient.bindPort";
		
		@Override
		public String getDoc() {
			return String.format(
					"The binding port for the socket to connect to the "
					+ "external SOCKS server for external connections "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
					SocksClient.MIN_BIND_PORT, 
					SocksClient.MAX_BIND_PORT);
		}

		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, Port.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Port)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Port.class.getName()));
			}
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Port.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_CONNECT_TIMEOUT {

		private static final int DEFAULT_INT_VALUE = 
				SocksClient.DEFAULT_CONNECT_TIMEOUT;
		private static final String NAME = "externalClient.connectTimeout";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on waiting for the socket to "
					+ "connect to the external SOCKS server for external "
					+ "connections (default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_EXTERNAL_SERVER_URI {
		
		private static final String DOC = 
				"The URI of the external SOCKS server for external connections.";
		
		private static final String NAME = "externalClient.externalServerUri";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, null);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%s=SCHEME://HOST[:PORT]", NAME);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof SocksServerUri)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						SocksServerUri.class.getName()));
			}
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocksServerUri.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKET_SETTINGS {

		private static final String DOC = 
				"The space separated list of socket settings for the socket "
				+ "to connect to the external SOCKS server for external "
				+ "connections";
		private static final String NAME = "externalClient.socketSettings";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocksClient.DEFAULT_SOCKET_SETTINGS);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
			
	},
	EXTERNAL_CLIENT_SOCKS5_AUTH_METHODS {
		
		private static final String NAME = "externalClient.socks5.authMethods";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, Socks5Client.DEFAULT_AUTH_METHODS);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The space separated list of acceptable authentication "
					+ "methods to the external SOCKS5 server for external "
					+ "connections (default is %s)",
					Socks5Client.DEFAULT_AUTH_METHODS);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%1$s=%2$s1[ %2$s2[...]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(AuthMethods.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_MECHANISM_OID {
		
		private static final String NAME = 
				"externalClient.socks5.gssapiMechanismOid";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, Socks5Client.DEFAULT_GSSAPI_MECHANISM_OID);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The object ID for the GSS-API authentication mechanism to "
					+ "the external SOCKS5 server for external connections "
					+ "(default is %s)", 
					Socks5Client.DEFAULT_GSSAPI_MECHANISM_OID);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%s=GSSAPI_MECHANISM_OID", NAME);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Oid)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Oid.class.getName()));
			}
			return new Setting(NAME, value);
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
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = 
				Socks5Client.DEFAULT_GSSAPI_NEC_REFERENCE_IMPL;
		private static final String NAME = 
				"externalClient.socks5.gssapiNecReferenceImpl";

		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The boolean value to indicate if the exchange of the "
					+ "GSSAPI protection level negotiation must be unprotected "
					+ "should the external SOCKS5 server for external "
					+ "connections use the NEC reference implementation "
					+ "(default is %s)", 
					DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%s=true|false", NAME);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Boolean)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Boolean.class.getName()));
			}
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_PROTECTION_LEVELS {
		
		private static final String NAME = 
				"externalClient.socks5.gssapiProtectionLevels";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, Socks5Client.DEFAULT_GSSAPI_PROTECTION_LEVELS);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The space separated list of acceptable protection levels "
					+ "after GSS-API authentication with the external SOCKS5 "
					+ "server for external connections (The first is "
					+ "preferred. The remaining are acceptable if the server "
					+ "does not accept the first.) (default is %s)",
					Socks5Client.DEFAULT_GSSAPI_PROTECTION_LEVELS);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=%2$s1[ %2$s2[...]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(GssapiProtectionLevels.newInstance(value));
		}
		
	},
	EXTERNAL_CLIENT_SOCKS5_GSSAPI_SERVICE_NAME {
		
		private static final String DOC = "The GSS-API service name for the "
				+ "external SOCKS5 server for external connections";
		private static final String NAME = 
				"externalClient.socks5.gssapiServiceName";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, null);
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%s=GSSAPI_SERVICE_NAME", NAME);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof String)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						String.class.getName()));
			}
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return new Setting(NAME, value);
		}
		
	},
	HOST {

		private static final String DEFAULT_HOST = "0.0.0.0";
		private static final String NAME = "host";
		
		@Override
		public Setting getDefaultSetting() {
			Host host = null;
			try {
				host = Host.newInstance(DEFAULT_HOST);
			} catch (UnknownHostException e) {
				throw new AssertionError(e);
			}
			return new Setting(NAME, host);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The host name or address for the SOCKS server (default is %s)", 
					DEFAULT_HOST);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%s=HOST", NAME);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Host)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Host.class.getName()));
			}
			return new Setting(NAME, value);
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
	PORT {
		
		private static final int DEFAULT_INT_VALUE = 1080;
		private static final String NAME = "port";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, Port.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The port for the SOCKS server (default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Port.newInstance(value));
		}
		
	},
	SOCKET_SETTINGS {

		private static final String DOC = 
				"The space separated list of socket settings for the SOCKS "
				+ "server";
		private static final String NAME = "socketSettings";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_AUTH_METHODS {
		
		private static final String NAME = "socks5.authMethods";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, AuthMethods.newInstance(
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
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%1$s=%2$s1[ %2$s2[...]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(AuthMethods.newInstance(value));
		}
		
	},
	SOCKS5_GSSAPI_NEC_REFERENCE_IMPL {
		
		private static final boolean DEFAULT_BOOLEAN_VALUE = false;
		private static final String NAME = "socks5.gssapiNecReferenceImpl";

		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getDoc() {
			return String.format(
					"The boolean value to indicate if the exchange of the "
					+ "GSSAPI protection level negotiation must be unprotected "
					+ "according to the NEC reference implementation "
					+ "(default is %s)", 
					DEFAULT_BOOLEAN_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format("%s=true|false", NAME);
		}

		@Override
		public Setting newSetting(final Object value) {
			if (!(value instanceof Boolean)) {
				throw new ClassCastException(String.format(
						"unable to cast %s to %s",
						value.getClass().getName(),
						Boolean.class.getName()));
			}
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(Boolean.valueOf(value));
		}
		
	},
	SOCKS5_GSSAPI_PROTECTION_LEVELS {
		
		private static final String NAME = "socks5.gssapiProtectionLevels";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, GssapiProtectionLevels.DEFAULT_INSTANCE);
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
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=%2$s1[ %2$s2[...]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(GssapiProtectionLevels.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_INCOMING_SOCKET_SETTINGS {
		
		private static final String DOC = 
				"The space separated list of socket settings for the incoming "
				+ "socket";
		private static final String NAME = 
				"socks5.onBind.incomingSocketSettings";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS {
		
		private static final String DOC = 
				"The space separated list of socket settings for the listen "
				+ "socket";
		private static final String NAME = "socks5.onBind.listenSocketSettings";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_RELAY_BUFFER_SIZE {
		
		private static final int DEFAULT_INT_VALUE = 1024;
		private static final String NAME = "socks5.onBind.relayBufferSize";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The buffer size in bytes for relaying the data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_BIND_RELAY_TIMEOUT {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
		private static final String NAME = "socks5.onBind.relayTimeout";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on relaying no data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE {
		
		private static final int DEFAULT_INT_VALUE = 1024;
		private static final String NAME = "socks5.onConnect.relayBufferSize";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The buffer size in bytes for relaying the data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_CONNECT_RELAY_TIMEOUT {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
		private static final String NAME = "socks5.onConnect.relayTimeout";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on relaying no data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_CONNECT_SERVER_CONNECT_TIMEOUT {

		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
		private static final String NAME = 
				"socks5.onConnect.serverConnectTimeout";
		
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on waiting the "
					+ "server-facing socket to connect (default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_CONNECT_SERVER_SOCKET_SETTINGS {
		
		private static final String DOC = 
				"The space separated list of socket settings for the "
				+ "server-facing socket";
		private static final String NAME = 
				"socks5.onConnect.serverSocketSettings";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_CLIENT_SOCKET_SETTINGS {
		
		private static final String DOC = 
				"The space separated list of socket settings for the "
				+ "client-facing UDP socket";
		private static final String NAME = 
				"socks5.onUdpAssociate.clientSocketSettings";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE {
		
		private static final int DEFAULT_INT_VALUE = 32768;
		private static final String NAME = 
				"socks5.onUdpAssociate.relayBufferSize";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The buffer size in bytes for relaying the data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT {
		
		private static final int DEFAULT_INT_VALUE = 60000; // 1 minute
		private static final String NAME = "socks5.onUdpAssociate.relayTimeout";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(
					NAME, PositiveInteger.newInstance(DEFAULT_INT_VALUE));
		}

		@Override
		public String getDoc() {
			return String.format(
					"The timeout in milliseconds on relaying no data "
					+ "(default is %s)", 
					DEFAULT_INT_VALUE);
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%s=INTEGER_BETWEEN_%s_AND_%s", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(PositiveInteger.newInstance(value));
		}
		
	},
	SOCKS5_ON_UDP_ASSOCIATE_SERVER_SOCKET_SETTINGS {
		
		private static final String DOC = 
				"The space separated list of socket settings for the "
				+ "server-facing UDP socket";
		private static final String NAME = 
				"socks5.onUdpAssociate.serverSocketSettings";
				
		@Override
		public Setting getDefaultSetting() {
			return new Setting(NAME, SocketSettings.newInstance());
		}

		@Override
		public String getDoc() {
			return DOC;
		}

		@Override
		public String getName() {
			return NAME;
		}

		@Override
		public String getUsage() {
			return String.format(
					"%1$s=[%2$s1[ %2$s2[...]]]", 
					NAME, 
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
			return new Setting(NAME, value);
		}

		@Override
		public Setting newSetting(final String value) {
			return newSetting(SocketSettings.newInstance(value));
		}
		
	};

	public static SettingSpec getInstance(final String name) {
		for (SettingSpec settingSpec : SettingSpec.values()) {
			if (settingSpec.getName().equals(name)) {
				return settingSpec;
			}
		}
		throw new IllegalArgumentException(String.format(
				"unknown setting name: %s", name));
	}
	
	public abstract Setting getDefaultSetting();
	
	public abstract String getName();
	
	public abstract Setting newSetting(final Object value);
	
	public abstract Setting newSetting(final String value);
	
}
