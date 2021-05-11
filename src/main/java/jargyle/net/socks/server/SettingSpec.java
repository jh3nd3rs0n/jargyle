package jargyle.net.socks.server;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.ietf.jgss.Oid;

import jargyle.help.HelpText;
import jargyle.net.Host;
import jargyle.net.Port;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.server.settingspec.AuthMethodsSettingSpec;
import jargyle.net.socks.server.settingspec.BooleanSettingSpec;
import jargyle.net.socks.server.settingspec.CriteriaSettingSpec;
import jargyle.net.socks.server.settingspec.EncryptedPasswordSettingSpec;
import jargyle.net.socks.server.settingspec.FileSettingSpec;
import jargyle.net.socks.server.settingspec.ProtectionLevelsSettingSpec;
import jargyle.net.socks.server.settingspec.HostSettingSpec;
import jargyle.net.socks.server.settingspec.NonnegativeIntegerSettingSpec;
import jargyle.net.socks.server.settingspec.OidSettingSpec;
import jargyle.net.socks.server.settingspec.PortSettingSpec;
import jargyle.net.socks.server.settingspec.PositiveIntegerSettingSpec;
import jargyle.net.socks.server.settingspec.SocketSettingsSettingSpec;
import jargyle.net.socks.server.settingspec.Socks5RequestCriteriaSettingSpec;
import jargyle.net.socks.server.settingspec.SocksServerUriSettingSpec;
import jargyle.net.socks.server.settingspec.StringSettingSpec;
import jargyle.net.socks.server.settingspec.StringsSettingSpec;
import jargyle.net.socks.server.settingspec.UsernamePasswordAuthenticatorSettingSpec;
import jargyle.net.socks.server.settingspec.UsernamePasswordSettingSpec;
import jargyle.net.socks.server.v5.Socks5RequestCriteria;
import jargyle.net.socks.server.v5.Socks5RequestCriterion;
import jargyle.net.socks.server.v5.userpassauth.UsernamePasswordAuthenticator;
import jargyle.net.socks.transport.v5.AuthMethod;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.Criteria;
import jargyle.util.Criterion;
import jargyle.util.CriterionMethod;
import jargyle.util.NonnegativeInteger;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

public abstract class SettingSpec<V> 
	implements Comparable<SettingSpec<? extends Object>> {

	private static int NEXT_ORDINAL = 0;
	
	@HelpText(
			doc = "The space separated list of allowed client address "
					+ "criteria (default is matches:.*)", 
			usage = "allowedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> ALLOWED_CLIENT_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"allowedClientAddressCriteria", 
			Criteria.newInstance(Criterion.newInstance(CriterionMethod.MATCHES, ".*")));
	
	@HelpText(
			doc = "The maximum length of the queue of incoming connections "
					+ "(default is 50)", 
			usage = "backlog=INTEGER_BETWEEN_0_AND_2147483647"
	)
	public static final SettingSpec<NonnegativeInteger> BACKLOG = new NonnegativeIntegerSettingSpec(
			"backlog",
			NonnegativeInteger.newInstance(50));
	
	@HelpText(
			doc = "The space separated list of blocked client address criteria", 
			usage = "blockedClientAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> BLOCKED_CLIENT_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"blockedClientAddressCriteria",
			Criteria.EMPTY_INSTANCE);
	
	@HelpText(
			doc = "The binding host name or address for the internal socket "
					+ "that is used to connect to the other SOCKS server (used "
					+ "for the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) "
					+ "(default is 0.0.0.0)", 
			usage = "chaining.bindHost=HOST"
	)
	public static final SettingSpec<Host> CHAINING_BIND_HOST = new HostSettingSpec(
			"chaining.bindHost",
			PropertySpec.BIND_HOST.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for the internal "
					+ "socket to connect to the other SOCKS server (used for "
					+ "the SOCKS5 commands RESOLVE, BIND and UDP ASSOCIATE) "
					+ "(default is 60000)", 
			usage = "chaining.connectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> CHAINING_CONNECT_TIMEOUT = new PositiveIntegerSettingSpec(
			"chaining.connectTimeout",
			PropertySpec.CONNECT_TIMEOUT.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The boolean value to indicate if DTLS connections to "
					+ "the other SOCKS server are enabled (default is false)",
			usage = "chaining.dtls.enabled=true|false"
	)
	public static final SettingSpec<Boolean> CHAINING_DTLS_ENABLED = new BooleanSettingSpec(
			"chaining.dtls.enabled",
			PropertySpec.DTLS_ENABLED.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The space separated list of acceptable cipher suites "
					+ "enabled for DTLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[...]]]"
	)
	public static final SettingSpec<Strings> CHAINING_DTLS_ENABLED_CIPHER_SUITES = new StringsSettingSpec(
			"chaining.dtls.enabledCipherSuites",
			PropertySpec.DTLS_ENABLED_CIPHER_SUITES.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The space separated list of acceptable protocol versions "
					+ "enabled for DTLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[...]]]"
	)	
	public static final SettingSpec<Strings> CHAINING_DTLS_ENABLED_PROTOCOLS = new StringsSettingSpec(
			"chaining.dtls.enabledProtocols",
			PropertySpec.DTLS_ENABLED_PROTOCOLS.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The key store file for the DTLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.dtls.keyStoreFile=FILE"
	)
	public static final SettingSpec<File> CHAINING_DTLS_KEY_STORE_FILE = new FileSettingSpec(
			"chaining.dtls.keyStoreFile",
			PropertySpec.DTLS_KEY_STORE_FILE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The password for the key store for the DTLS connections "
					+ "to the other SOCKS server",
			usage = "chaining.dtls.keyStorePassword=PASSWORD"
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_DTLS_KEY_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"chaining.dtls.keyStorePassword",
			PropertySpec.DTLS_KEY_STORE_PASSWORD.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The type of key store file for the DTLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.dtls.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> CHAINING_DTLS_KEY_STORE_TYPE = new StringSettingSpec(
			"chaining.dtls.keyStoreType",
			PropertySpec.DTLS_KEY_STORE_TYPE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The maximum packet size for the DTLS connections to the "
					+ "other SOCKS server (default is 32768)",
			usage = "chaining.dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final SettingSpec<PositiveInteger> CHAINING_DTLS_MAX_PACKET_SIZE = new PositiveIntegerSettingSpec(
			"chaining.dtls.maxPacketSize",
			PropertySpec.DTLS_MAX_PACKET_SIZE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The protocol version for the DTLS connections to the "
					+ "other SOCKS server (default is DTLSv1.2)",
			usage = "chaining.dtls.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> CHAINING_DTLS_PROTOCOL = new StringSettingSpec(
			"chaining.dtls.protocol",
			PropertySpec.DTLS_PROTOCOL.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The trust store file for the DTLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.dtls.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> CHAINING_DTLS_TRUST_STORE_FILE = new FileSettingSpec(
			"chaining.dtls.trustStoreFile",
			PropertySpec.DTLS_TRUST_STORE_FILE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The password for the trust store for the DTLS "
					+ "connections to the other SOCKS server",
			usage = "chaining.dtls.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> CHAINING_DTLS_TRUST_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"chaining.dtls.trustStorePassword",
			PropertySpec.DTLS_TRUST_STORE_PASSWORD.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The type of trust store file for the DTLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.dtls.trustStoreType=TYPE"
	)	
	public static final SettingSpec<String> CHAINING_DTLS_TRUST_STORE_TYPE = new StringSettingSpec(
			"chaining.dtls.trustStoreType",
			PropertySpec.DTLS_TRUST_STORE_TYPE.getDefaultProperty().getValue());	
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "internal socket that is used to connect to the other "
					+ "SOCKS server (used for the SOCKS5 command RESOLVE and "
					+ "UDP ASSOCIATE)", 
			usage = "chaining.socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> CHAINING_SOCKET_SETTINGS = new SocketSettingsSettingSpec(
			"chaining.socketSettings",
			PropertySpec.SOCKET_SETTINGS.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The URI of the other SOCKS server", 
			usage = "chaining.socksServerUri=SCHEME://HOST[:PORT]"
	)
	public static final SettingSpec<SocksServerUri> CHAINING_SOCKS_SERVER_URI = new SocksServerUriSettingSpec(
			"chaining.socksServerUri",
			null);
	
	@HelpText(
			doc = "The space separated list of acceptable authentication "
					+ "methods to the other SOCKS5 server (default is "
					+ "NO_AUTHENTICATION_REQUIRED)", 
			usage = "chaining.socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]"
	)
	public static final SettingSpec<AuthMethods> CHAINING_SOCKS5_AUTH_METHODS = new AuthMethodsSettingSpec(
			"chaining.socks5.authMethods",
			PropertySpec.SOCKS5_AUTH_METHODS.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The object ID for the GSS-API authentication mechanism to "
					+ "the other SOCKS5 server (default is 1.2.840.113554.1.2.2)", 
			usage = "chaining.socks5.gssapiauth.mechanismOid=SOCKS5_GSSAPIAUTH_MECHANISM_OID"
	)
	public static final SettingSpec<Oid> CHAINING_SOCKS5_GSSAPIAUTH_MECHANISM_OID = new OidSettingSpec(
			"chaining.socks5.gssapiauth.mechanismOid",
			PropertySpec.SOCKS5_GSSAPIAUTH_MECHANISM_OID.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected should the other SOCKS5 server use the NEC "
					+ "reference implementation (default is false)", 
			usage = "chaining.socks5.gssapiauth.necReferenceImpl=true|false"
	)
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = new BooleanSettingSpec(
			"chaining.socks5.gssapiauth.necReferenceImpl",
			PropertySpec.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The space separated list of acceptable protection levels "
					+ "after GSS-API authentication with the other SOCKS5 "
					+ "server (The first is preferred. The remaining are "
					+ "acceptable if the server does not accept the first.) "
					+ "(default is "
					+ "REQUIRED_INTEG_AND_CONF REQUIRED_INTEG NONE)", 
			usage = "chaining.socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[...]]"
	)
	public static final SettingSpec<ProtectionLevels> CHAINING_SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = new ProtectionLevelsSettingSpec(
			"chaining.socks5.gssapiauth.protectionLevels",
			PropertySpec.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The GSS-API service name for the other SOCKS5 server", 
			usage = "chaining.socks5.gssapiauth.serviceName=SOCKS5_GSSAPIAUTH_SERVICE_NAME"
	)
	public static final SettingSpec<String> CHAINING_SOCKS5_GSSAPIAUTH_SERVICE_NAME = new StringSettingSpec(
			"chaining.socks5.gssapiauth.serviceName",
			PropertySpec.SOCKS5_GSSAPIAUTH_SERVICE_NAME.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The boolean value to indicate that the RESOLVE command is "
					+ "to be used on the other SOCKS5 server for resolving "
					+ "host names (default is false)", 
			usage = "chaining.socks5.resolve.useResolveCommand=true|false"
	)	
	public static final SettingSpec<Boolean> CHAINING_SOCKS5_RESOLVE_USE_RESOLVE_COMMAND = new BooleanSettingSpec(
			"chaining.socks5.resolve.useResolveCommand",
			PropertySpec.SOCKS5_RESOLVE_USE_RESOLVE_COMMAND.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The username password to be used to access the other "
					+ "SOCKS5 server", 
			usage = "chaining.socks5.userpassauth.usernamePassword=USERNAME:PASSWORD"
	)
	public static final SettingSpec<UsernamePassword> CHAINING_SOCKS5_USERPASSAUTH_USERNAME_PASSWORD = new UsernamePasswordSettingSpec(
			"chaining.socks5.userpassauth.usernamePassword",
			null);
	
	@HelpText(
			doc = "The boolean value to indicate if SSL/TLS connections to "
					+ "the other SOCKS server are enabled (default is false)",
			usage = "chaining.ssl.enabled=true|false"
	)
	public static final SettingSpec<Boolean> CHAINING_SSL_ENABLED = new BooleanSettingSpec(
			"chaining.ssl.enabled",
			PropertySpec.SSL_ENABLED.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The space separated list of acceptable cipher suites "
					+ "enabled for SSL/TLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[...]]]"
	)
	public static final SettingSpec<Strings> CHAINING_SSL_ENABLED_CIPHER_SUITES = new StringsSettingSpec(
			"chaining.ssl.enabledCipherSuites",
			PropertySpec.SSL_ENABLED_CIPHER_SUITES.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The space separated list of acceptable protocol versions "
					+ "enabled for SSL/TLS connections to the other SOCKS "
					+ "server",
			usage = "chaining.ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[...]]]"
	)	
	public static final SettingSpec<Strings> CHAINING_SSL_ENABLED_PROTOCOLS = new StringsSettingSpec(
			"chaining.ssl.enabledProtocols",
			PropertySpec.SSL_ENABLED_PROTOCOLS.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The key store file for the SSL/TLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.ssl.keyStoreFile=FILE"
	)
	public static final SettingSpec<File> CHAINING_SSL_KEY_STORE_FILE = new FileSettingSpec(
			"chaining.ssl.keyStoreFile",
			PropertySpec.SSL_KEY_STORE_FILE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The password for the key store for the SSL/TLS connections "
					+ "to the other SOCKS server",
			usage = "chaining.ssl.keyStorePassword=PASSWORD"
	)
	public static final SettingSpec<EncryptedPassword> CHAINING_SSL_KEY_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"chaining.ssl.keyStorePassword",
			PropertySpec.SSL_KEY_STORE_PASSWORD.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The type of key store file for the SSL/TLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.ssl.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> CHAINING_SSL_KEY_STORE_TYPE = new StringSettingSpec(
			"chaining.ssl.keyStoreType",
			PropertySpec.SSL_KEY_STORE_TYPE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The protocol version for the SSL/TLS connections to the "
					+ "other SOCKS server (default is TLSv1.2)",
			usage = "chaining.ssl.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> CHAINING_SSL_PROTOCOL = new StringSettingSpec(
			"chaining.ssl.protocol",
			PropertySpec.SSL_PROTOCOL.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The trust store file for the SSL/TLS connections to the "
					+ "other SOCKS server",
			usage = "chaining.ssl.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> CHAINING_SSL_TRUST_STORE_FILE = new FileSettingSpec(
			"chaining.ssl.trustStoreFile",
			PropertySpec.SSL_TRUST_STORE_FILE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The password for the trust store for the SSL/TLS "
					+ "connections to the other SOCKS server",
			usage = "chaining.ssl.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> CHAINING_SSL_TRUST_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"chaining.ssl.trustStorePassword",
			PropertySpec.SSL_TRUST_STORE_PASSWORD.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The type of trust store file for the SSL/TLS connections to "
					+ "the other SOCKS server (default is PKCS12)",
			usage = "chaining.ssl.trustStoreType=TYPE"
	)	
	public static final SettingSpec<String> CHAINING_SSL_TRUST_STORE_TYPE = new StringSettingSpec(
			"chaining.ssl.trustStoreType",
			PropertySpec.SSL_TRUST_STORE_TYPE.getDefaultProperty().getValue());
	
	@HelpText(
			doc = "The boolean value to indicate if DTLS connections to "
					+ "the SOCKS server are enabled (default is false)",
			usage = "dtls.enabled=true|false"
	)	
	public static final SettingSpec<Boolean> DTLS_ENABLED = new BooleanSettingSpec(
			"dtls.enabled",
			Boolean.FALSE);
	
	@HelpText(
			doc = "The space separated list of acceptable cipher suites "
					+ "enabled for DTLS connections to the SOCKS server",
			usage = "dtls.enabledCipherSuites=[DTLS_CIPHER_SUITE1[ DTLS_CIPHER_SUITE2[...]]]"
	)	
	public static final SettingSpec<Strings> DTLS_ENABLED_CIPHER_SUITES = new StringsSettingSpec(
			"dtls.enabledCipherSuites",
			Strings.newInstance(new String[] { }));
	
	@HelpText(
			doc = "The space separated list of acceptable protocol versions "
					+ "enabled for DTLS connections to the SOCKS server",
			usage = "dtls.enabledProtocols=[DTLS_PROTOCOL1[ DTLS_PROTOCOL2[...]]]"
	)	
	public static final SettingSpec<Strings> DTLS_ENABLED_PROTOCOLS = new StringsSettingSpec(
			"dtls.enabledProtocols",
			Strings.newInstance(new String[] { }));
	
	@HelpText(
			doc = "The key store file for the DTLS connections to the SOCKS "
					+ "server",
			usage = "dtls.keyStoreFile=FILE"
	)	
	public static final SettingSpec<File> DTLS_KEY_STORE_FILE = new FileSettingSpec(
			"dtls.keyStoreFile",
			null);
	
	@HelpText(
			doc = "The password for the key store for the DTLS connections "
					+ "to the SOCKS server",
			usage = "dtls.keyStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> DTLS_KEY_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"dtls.keyStorePassword",
			EncryptedPassword.newInstance(new char[] { }));
	
	@HelpText(
			doc = "The type of key store file for the DTLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "dtls.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> DTLS_KEY_STORE_TYPE = new StringSettingSpec(
			"dtls.keyStoreType",
			"PKCS12");
	
	@HelpText(
			doc = "The maximum packet size for the DTLS connections to the "
					+ "SOCKS server (default is 32768)",
			usage = "dtls.maxPacketSize=INTEGER_BETWEEN_1_AND_2147483647"
	)	
	public static final SettingSpec<PositiveInteger> DTLS_MAX_PACKET_SIZE = new PositiveIntegerSettingSpec(
			"dtls.maxPacketSize",
			PositiveInteger.newInstance(32768));
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "required for DTLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "dtls.needClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> DTLS_NEED_CLIENT_AUTH = new BooleanSettingSpec(
			"dtls.needClientAuth",
			Boolean.FALSE);
	
	@HelpText(
			doc = "The protocol version for the DTLS connections to the "
					+ "SOCKS server (default is DTLSv1.2)",
			usage = "dtls.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> DTLS_PROTOCOL = new StringSettingSpec(
			"dtls.protocol",
			"DTLSv1.2");
	
	@HelpText(
			doc = "The trust store file for the DTLS connections to the "
					+ "SOCKS server",
			usage = "dtls.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> DTLS_TRUST_STORE_FILE = new FileSettingSpec(
			"dtls.trustStoreFile",
			null);
	
	@HelpText(
			doc = "The password for the trust store for the DTLS "
					+ "connections to the SOCKS server",
			usage = "dtls.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> DTLS_TRUST_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"dtls.trustStorePassword",
			EncryptedPassword.newInstance(new char[] { }));
	
	@HelpText(
			doc = "The type of trust store file for the DTLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "dtls.trustStoreType=TYPE"
	)		
	public static final SettingSpec<String> DTLS_TRUST_STORE_TYPE = new StringSettingSpec(
			"dtls.trustStoreType",
			"PKCS12");
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "requested for DTLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "dtls.wantClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> DTLS_WANT_CLIENT_AUTH = new BooleanSettingSpec(
			"dtls.wantClientAuth",
			Boolean.FALSE);
	
	@HelpText(
			doc = "The host name or address for the SOCKS server (default is "
					+ "0.0.0.0)", 
			usage = "host=HOST"
	)
	public static final SettingSpec<Host> HOST = new HostSettingSpec(
			"host", 
			Host.INET4_ALL_ZEROS_INSTANCE);
	
	@HelpText(
			doc = "The port for the SOCKS server (default is 1080)", 
			usage = "port=INTEGER_BETWEEN_0_AND_65535"
	)
	public static final SettingSpec<Port> PORT = new PortSettingSpec(
			"port",
			Port.newInstance(1080));
	
	@HelpText(
			doc = "The space separated list of socket settings for the SOCKS "
					+ "server", 
			usage = "socketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKET_SETTINGS = new SocketSettingsSettingSpec(
			"socketSettings",
			SocketSettings.newInstance());
	
	public static final SettingSpec<Socks5RequestCriteria> SOCKS5_ALLOWED_SOCKS5_REQUEST_CRITERIA = new Socks5RequestCriteriaSettingSpec(
			"socks5.allowedSocks5RequestCriteria",
			Socks5RequestCriteria.newInstance(
					new Socks5RequestCriterion.Builder().build()));
	
	@HelpText(
			doc = "The space separated list of acceptable authentication "
					+ "methods in order of preference (default is "
					+ "NO_AUTHENTICATION_REQUIRED)", 
			usage = "socks5.authMethods=SOCKS5_AUTH_METHOD1[ SOCKS5_AUTH_METHOD2[...]]"
	)
	public static final SettingSpec<AuthMethods> SOCKS5_AUTH_METHODS = new AuthMethodsSettingSpec(
			"socks5.authMethods",
			AuthMethods.newInstance(AuthMethod.NO_AUTHENTICATION_REQUIRED));
	
	public static final SettingSpec<Socks5RequestCriteria> SOCKS5_BLOCKED_SOCKS5_REQUEST_CRITERIA = new Socks5RequestCriteriaSettingSpec(
			"socks5.blockedSocks5RequestCriteria",
			Socks5RequestCriteria.EMPTY_INSTANCE);
	
	@HelpText(
			doc = "The boolean value to indicate if the exchange of the "
					+ "GSS-API protection level negotiation must be "
					+ "unprotected according to the NEC reference "
					+ "implementation (default is false)", 
			usage = "socks5.gssapiauth.necReferenceImpl=true|false"
	)
	public static final SettingSpec<Boolean> SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL = new BooleanSettingSpec(
			"socks5.gssapiauth.necReferenceImpl",
			Boolean.FALSE);
	
	@HelpText(
			doc = "The space separated list of acceptable protection levels "
					+ "after GSS-API authentication (The first is preferred "
					+ "if the client does not provide a protection level that "
					+ "is acceptable.) (default is REQUIRED_INTEG_AND_CONF "
					+ "REQUIRED_INTEG NONE)", 
			usage = "socks5.gssapiauth.protectionLevels=SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL1[ SOCKS5_GSSAPIAUTH_PROTECTION_LEVEL2[...]]"
	)
	public static final SettingSpec<ProtectionLevels> SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS = new ProtectionLevelsSettingSpec(
			"socks5.gssapiauth.protectionLevels",
			ProtectionLevels.DEFAULT_INSTANCE);
	
	@HelpText(
			doc = "The space separated list of allowed external inbound "
					+ "address criteria (default is matches:.*)", 
			usage = "socks5.onBind.allowedExternalInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_BIND_ALLOWED_EXTERNAL_INBOUND_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"socks5.onBind.allowedExternalInboundAddressCriteria",
			Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
	
	@HelpText(
			doc = "The space separated list of blocked external inbound "
					+ "address criteria", 
			usage = "socks5.onBind.blockedExternalInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_BIND_BLOCKED_EXTERNAL_INBOUND_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"socks5.onBind.blockedExternalInboundAddressCriteria",
			Criteria.EMPTY_INSTANCE);
	
	@HelpText(
			doc = "The space separated list of socket settings for the listen "
					+ "socket", 
			usage = "socks5.onBind.listenSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_BIND_LISTEN_SOCKET_SETTINGS = new SocketSettingsSettingSpec(
			"socks5.onBind.listenSocketSettings",
			SocketSettings.newInstance());
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "1024)", 
			usage = "socks5.onBind.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_BUFFER_SIZE = new PositiveIntegerSettingSpec(
			"socks5.onBind.relayBufferSize",
			PositiveInteger.newInstance(1024));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onBind.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_BIND_RELAY_TIMEOUT = new PositiveIntegerSettingSpec(
			"socks5.onBind.relayTimeout",
			PositiveInteger.newInstance(60000)); // 1 minute
	
	@HelpText(
			doc = "The boolean value to indicate if the server-facing socket "
					+ "is to be prepared before connecting (involves applying "
					+ "the specified socket settings, resolving the target "
					+ "host name, and setting the specified timeout on waiting "
					+ "to connect) (default is false)", 
			usage = "socks5.onConnect.prepareServerFacingSocket=true|false"
	)	
	public static final SettingSpec<Boolean> SOCKS5_ON_CONNECT_PREPARE_SERVER_FACING_SOCKET = new BooleanSettingSpec(
			"socks5.onConnect.prepareServerFacingSocket",
			Boolean.FALSE);
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "1024)", 
			usage = "socks5.onConnect.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_BUFFER_SIZE = new PositiveIntegerSettingSpec(
			"socks5.onConnect.relayBufferSize",
			PositiveInteger.newInstance(1024));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onConnect.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_RELAY_TIMEOUT = new PositiveIntegerSettingSpec(
			"socks5.onConnect.relayTimeout",
			PositiveInteger.newInstance(60000)); // 1 minute
	
	@HelpText(
			doc = "The binding host name or address for the server-facing "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onConnect.serverFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_CONNECT_SERVER_FACING_BIND_HOST = new HostSettingSpec(
			"socks5.onConnect.serverFacingBindHost",
			Host.INET4_ALL_ZEROS_INSTANCE);
	
	@HelpText(
			doc = "The timeout in milliseconds on waiting for the "
					+ "server-facing socket to connect (default is 60000)", 
			usage = "socks5.onConnect.serverFacingConnectTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_CONNECT_SERVER_FACING_CONNECT_TIMEOUT = new PositiveIntegerSettingSpec(
			"socks5.onConnect.serverFacingConnectTimeout",
			PositiveInteger.newInstance(60000)); // 1 minute
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "server-facing socket", 
			usage = "socks5.onConnect.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_CONNECT_SERVER_FACING_SOCKET_SETTINGS = new SocketSettingsSettingSpec(
			"socks5.onConnect.serverFacingSocketSettings",
			SocketSettings.newInstance());
	
	@HelpText(
			doc = "The space separated list of allowed external inbound "
					+ "address criteria (default is matches:.*)", 
			usage = "socks5.onUdpAssociate.allowedExternalInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_EXTERNAL_INBOUND_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"socks5.onUdpAssociate.allowedExternalInboundAddressCriteria",
			Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
	
	@HelpText(
			doc = "The space separated list of allowed internal outbound "
					+ "address criteria (default is matches:.*)", 
			usage = "socks5.onUdpAssociate.allowedInternalOutboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_ALLOWED_INTERNAL_OUTBOUND_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"socks5.onUdpAssociate.allowedInternalOutboundAddressCriteria",
			Criteria.newInstance(Criterion.newInstance(
					CriterionMethod.MATCHES, ".*")));
	
	@HelpText(
			doc = "The space separated list of blocked external inbound "
					+ "address criteria", 
			usage = "socks5.onUdpAssociate.blockedExternalInboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_EXTERNAL_INBOUND_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"socks5.onUdpAssociate.blockedExternalInboundAddressCriteria",
			Criteria.EMPTY_INSTANCE);
	
	@HelpText(
			doc = "The space separated list of blocked internal outbound "
					+ "address criteria", 
			usage = "socks5.onUdpAssociate.blockedInternalOutboundAddressCriteria=[equals|matches:VALUE1[ equals|matches:VALUE2[...]]]"
	)
	public static final SettingSpec<Criteria> SOCKS5_ON_UDP_ASSOCIATE_BLOCKED_INTERNAL_OUTBOUND_ADDRESS_CRITERIA = new CriteriaSettingSpec(
			"socks5.onUdpAssociate.blockedInternalOutboundAddressCriteria",
			Criteria.EMPTY_INSTANCE);
	
	@HelpText(
			doc = "The binding host name or address for the client-facing UDP "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onUdpAssociate.clientFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_BIND_HOST = new HostSettingSpec(
			"socks5.onUdpAssociate.clientFacingBindHost",
			Host.INET4_ALL_ZEROS_INSTANCE);
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "client-facing UDP socket", 
			usage = "socks5.onUdpAssociate.clientFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_CLIENT_FACING_SOCKET_SETTINGS = new SocketSettingsSettingSpec(
			"socks5.onUdpAssociate.clientFacingSocketSettings",
			SocketSettings.newInstance());
	
	@HelpText(
			doc = "The buffer size in bytes for relaying the data (default is "
					+ "32768)", 
			usage = "socks5.onUdpAssociate.relayBufferSize=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_BUFFER_SIZE = new PositiveIntegerSettingSpec(
			"socks5.onUdpAssociate.relayBufferSize",
			PositiveInteger.newInstance(32768));
	
	@HelpText(
			doc = "The timeout in milliseconds on relaying no data (default "
					+ "is 60000)", 
			usage = "socks5.onUdpAssociate.relayTimeout=INTEGER_BETWEEN_1_AND_2147483647"
	)
	public static final SettingSpec<PositiveInteger> SOCKS5_ON_UDP_ASSOCIATE_RELAY_TIMEOUT = new PositiveIntegerSettingSpec(
			"socks5.onUdpAssociate.relayTimeout",
			PositiveInteger.newInstance(60000)); // 1 minute
	
	@HelpText(
			doc = "The binding host name or address for the server-facing UDP "
					+ "socket (default is 0.0.0.0)", 
			usage = "socks5.onUdpAssociate.serverFacingBindHost=HOST"
	)
	public static final SettingSpec<Host> SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_BIND_HOST = new HostSettingSpec(
			"socks5.onUdpAssociate.serverFacingBindHost",
			Host.INET4_ALL_ZEROS_INSTANCE);
	
	@HelpText(
			doc = "The space separated list of socket settings for the "
					+ "server-facing UDP socket", 
			usage = "socks5.onUdpAssociate.serverFacingSocketSettings=[SOCKET_SETTING1[ SOCKET_SETTING2[...]]]"
	)
	public static final SettingSpec<SocketSettings> SOCKS5_ON_UDP_ASSOCIATE_SERVER_FACING_SOCKET_SETTINGS = new SocketSettingsSettingSpec(
			"socks5.onUdpAssociate.serverFacingSocketSettings",
			SocketSettings.newInstance());
	
	@HelpText(
			doc = "The username password authenticator for the SOCKS5 server", 
			usage = "socks5.userpassauth.usernamePasswordAuthenticator=CLASSNAME[:VALUE]"
	)
	public static final SettingSpec<UsernamePasswordAuthenticator> SOCKS5_USERPASSAUTH_USERNAME_PASSWORD_AUTHENTICATOR = new UsernamePasswordAuthenticatorSettingSpec(
			"socks5.userpassauth.usernamePasswordAuthenticator",
			null);
	
	@HelpText(
			doc = "The boolean value to indicate if SSL/TLS connections to "
					+ "the SOCKS server are enabled (default is false)",
			usage = "ssl.enabled=true|false"
	)	
	public static final SettingSpec<Boolean> SSL_ENABLED = new BooleanSettingSpec(
			"ssl.enabled",
			Boolean.FALSE);
	
	@HelpText(
			doc = "The space separated list of acceptable cipher suites "
					+ "enabled for SSL/TLS connections to the SOCKS server",
			usage = "ssl.enabledCipherSuites=[SSL_CIPHER_SUITE1[ SSL_CIPHER_SUITE2[...]]]"
	)	
	public static final SettingSpec<Strings> SSL_ENABLED_CIPHER_SUITES = new StringsSettingSpec(
			"ssl.enabledCipherSuites",
			Strings.newInstance(new String[] { }));
	
	@HelpText(
			doc = "The space separated list of acceptable protocol versions "
					+ "enabled for SSL/TLS connections to the SOCKS server",
			usage = "ssl.enabledProtocols=[SSL_PROTOCOL1[ SSL_PROTOCOL2[...]]]"
	)	
	public static final SettingSpec<Strings> SSL_ENABLED_PROTOCOLS = new StringsSettingSpec(
			"ssl.enabledProtocols",
			Strings.newInstance(new String[] { }));
	
	@HelpText(
			doc = "The key store file for the SSL/TLS connections to the SOCKS "
					+ "server",
			usage = "ssl.keyStoreFile=FILE"
	)	
	public static final SettingSpec<File> SSL_KEY_STORE_FILE = new FileSettingSpec(
			"ssl.keyStoreFile",
			null);
	
	@HelpText(
			doc = "The password for the key store for the SSL/TLS connections "
					+ "to the SOCKS server",
			usage = "ssl.keyStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> SSL_KEY_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"ssl.keyStorePassword",
			EncryptedPassword.newInstance(new char[] { }));
	
	@HelpText(
			doc = "The type of key store file for the SSL/TLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "ssl.keyStoreType=TYPE"
	)	
	public static final SettingSpec<String> SSL_KEY_STORE_TYPE = new StringSettingSpec(
			"ssl.keyStoreType",
			"PKCS12");
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "required for SSL/TLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "ssl.needClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> SSL_NEED_CLIENT_AUTH = new BooleanSettingSpec(
			"ssl.needClientAuth",
			Boolean.FALSE);
	
	@HelpText(
			doc = "The protocol version for the SSL/TLS connections to the "
					+ "SOCKS server (default is TLSv1.2)",
			usage = "ssl.protocol=PROTOCOL"
	)	
	public static final SettingSpec<String> SSL_PROTOCOL = new StringSettingSpec(
			"ssl.protocol",
			"TLSv1.2");
	
	@HelpText(
			doc = "The trust store file for the SSL/TLS connections to the "
					+ "SOCKS server",
			usage = "ssl.trustStoreFile=FILE"
	)	
	public static final SettingSpec<File> SSL_TRUST_STORE_FILE = new FileSettingSpec(
			"ssl.trustStoreFile",
			null);
	
	@HelpText(
			doc = "The password for the trust store for the SSL/TLS "
					+ "connections to the SOCKS server",
			usage = "ssl.trustStorePassword=PASSWORD"
	)	
	public static final SettingSpec<EncryptedPassword> SSL_TRUST_STORE_PASSWORD = new EncryptedPasswordSettingSpec(
			"ssl.trustStorePassword",
			EncryptedPassword.newInstance(new char[] { }));
	
	@HelpText(
			doc = "The type of trust store file for the SSL/TLS connections to "
					+ "the SOCKS server (default is PKCS12)",
			usage = "ssl.trustStoreType=TYPE"
	)		
	public static final SettingSpec<String> SSL_TRUST_STORE_TYPE = new StringSettingSpec(
			"ssl.trustStoreType",
			"PKCS12");
	
	@HelpText(
			doc = "The boolean value to indicate that client authentication is "
					+ "requested for SSL/TLS connections to the SOCKS server "
					+ "(default is false)",
			usage = "ssl.wantClientAuth=true|false"
	)	
	public static final SettingSpec<Boolean> SSL_WANT_CLIENT_AUTH = new BooleanSettingSpec(
			"ssl.wantClientAuth",
			Boolean.FALSE);
	
	private static final List<SettingSpec<Object>> VALUES = 
			new ArrayList<SettingSpec<Object>>();
	
	private static final Map<String, SettingSpec<Object>> VALUES_MAP =
			new HashMap<String, SettingSpec<Object>>();

	private static synchronized void fillValuesAndValuesMapIfEmpty() {
		if (!VALUES.isEmpty() && !VALUES_MAP.isEmpty()) {
			return;
		}
		VALUES.clear();
		VALUES_MAP.clear();
		Field[] fields = SettingSpec.class.getFields();
		for (Field field : fields) {
			int modifiers = field.getModifiers();
			Class<?> type = field.getType();
			if (!Modifier.isStatic(modifiers)
					|| !Modifier.isFinal(modifiers)
					|| !SettingSpec.class.isAssignableFrom(type)) {
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
			SettingSpec<Object> val = (SettingSpec<Object>) value;
			VALUES.add(val);
			VALUES_MAP.put(val.toString(), val);
		}		
	}
	
	public static SettingSpec<Object> valueOfString(final String s) {
		Map<String, SettingSpec<Object>> valuesMap = SettingSpec.valuesMap();
		if (valuesMap.containsKey(s)) {
			return valuesMap.get(s);
		}
		throw new IllegalArgumentException(String.format(
				"unknown setting: %s", s));
	}
	
	public static SettingSpec<Object>[] values() {
		fillValuesAndValuesMapIfEmpty();
		@SuppressWarnings("unchecked")
		SettingSpec<Object>[] vals = (SettingSpec<Object>[]) VALUES.toArray(
				new SettingSpec<?>[VALUES.size()]);
		return vals;
	}
	
	private static Map<String, SettingSpec<Object>> valuesMap() {
		fillValuesAndValuesMapIfEmpty();
		return Collections.unmodifiableMap(VALUES_MAP);
	}
	
	private Setting<V> defaultSetting;
	private final V defaultValue;
	private final int ordinal;
	private final String string;
	private final Class<V> valueType;
		
	public SettingSpec(
			final String s, final Class<V> valType, final V defaultVal) {
		Objects.requireNonNull(s);
		Objects.requireNonNull(valType);
		this.defaultValue = valType.cast(defaultVal);
		this.ordinal = NEXT_ORDINAL++;
		this.string = s;
		this.valueType = valType;
		this.defaultSetting = null;
	}
	
	@Override
	public final int compareTo(final SettingSpec<? extends Object> o) {
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
		SettingSpec<?> other = (SettingSpec<?>) obj;
		if (this.ordinal != other.ordinal) {
			return false;
		}
		return true;
	}

	public final Setting<V> getDefaultSetting() {
		if (this.defaultSetting == null) {
			this.defaultSetting = new Setting<V>(this, this.valueType.cast(
					this.defaultValue)); 
		}
		return this.defaultSetting;
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
	
	public Setting<V> newSetting(final V value) {
		return new Setting<V>(this, this.valueType.cast(value));
	}
	
	public abstract Setting<V> newSettingOfParsableValue(final String value);
	
	public final int ordinal() {
		return this.ordinal;
	}
	
	@Override
	public final String toString() {
		return this.string;
	}
	
}
