package jargyle.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ietf.jgss.Oid;

import jargyle.client.Properties;
import jargyle.client.Property;
import jargyle.client.PropertySpec;
import jargyle.client.SocksClient;
import jargyle.client.SocksServerUri;
import jargyle.client.socks5.Socks5ServerUri;
import jargyle.client.socks5.UsernamePassword;
import jargyle.common.net.Host;
import jargyle.common.net.SocketSettings;
import jargyle.common.net.socks5.AuthMethods;
import jargyle.common.net.socks5.gssapiauth.GssapiProtectionLevels;
import jargyle.common.net.ssl.CipherSuites;
import jargyle.common.net.ssl.Protocols;
import jargyle.common.security.EncryptedPassword;
import jargyle.common.util.Criteria;
import jargyle.common.util.PositiveInteger;

public final class SocksClientFactory {
	
	private static List<Property> getSocks5ClientProperties(
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		List<Property> properties = new ArrayList<Property>();
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_AUTH_METHODS)) {
			AuthMethods authMethods = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_AUTH_METHODS, 
					AuthMethods.class);
			properties.add(PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
					authMethods));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_MECHANISM_OID)) {
			Oid gssapiMechanismOid = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_MECHANISM_OID, 
					Oid.class);
			properties.add(PropertySpec.SOCKS5_GSSAPI_MECHANISM_OID.newProperty(
					gssapiMechanismOid));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL)) {
			Boolean gssapiNecReferenceImpl = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL,
					Boolean.class);
			properties.add(
					PropertySpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newProperty(
							gssapiNecReferenceImpl));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_PROTECTION_LEVELS)) {
			GssapiProtectionLevels gssapiProtectionLevels =	settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_PROTECTION_LEVELS,
					GssapiProtectionLevels.class);
			properties.add(
					PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.newProperty(
							gssapiProtectionLevels));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_SERVICE_NAME)) {
			String gssapiServiceName = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_SERVICE_NAME, 
					String.class);
			properties.add(PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.newProperty(
					gssapiServiceName));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_ON_RESOLVE_DIRECT_RESOLVE_HOSTNAME_CRITERIA)) {
			Criteria directResolveHostnameCriteria = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_ON_RESOLVE_DIRECT_RESOLVE_HOSTNAME_CRITERIA, 
					Criteria.class);
			properties.add(PropertySpec.SOCKS5_ON_RESOLVE_DIRECT_RESOLVE_HOSTNAME_CRITERIA.newProperty(
					directResolveHostnameCriteria));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_ON_RESOLVE_FORWARD_RESOLVE_HOSTNAME_CRITERIA)) {
			Criteria forwardResolveHostnameCriteria = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_ON_RESOLVE_FORWARD_RESOLVE_HOSTNAME_CRITERIA, 
					Criteria.class);
			properties.add(PropertySpec.SOCKS5_ON_RESOLVE_FORWARD_RESOLVE_HOSTNAME_CRITERIA.newProperty(
					forwardResolveHostnameCriteria));			
		}		
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_USERNAME_PASSWORD)) {
			UsernamePassword usernamePassword = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_USERNAME_PASSWORD, 
					UsernamePassword.class);
			if (usernamePassword != null) {
				properties.add(PropertySpec.SOCKS5_USERNAME.newProperty(
						usernamePassword.getUsername()));
				properties.add(PropertySpec.SOCKS5_PASSWORD.newProperty(
						usernamePassword.getEncryptedPassword()));			
			}
		}
		return properties;
	}
	
	private static List<Property> getSocksClientProperties(
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		List<Property> properties = new ArrayList<Property>();
		if (settings.containsNondefaultValue(SettingSpec.CHAINING_BIND_HOST)) {
			Host bindHost = settings.getLastValue(
					SettingSpec.CHAINING_BIND_HOST, Host.class);
			properties.add(PropertySpec.BIND_HOST.newProperty(bindHost));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_CONNECT_TIMEOUT)) {
			PositiveInteger connectTimeout = settings.getLastValue(
					SettingSpec.CHAINING_CONNECT_TIMEOUT, 
					PositiveInteger.class);
			properties.add(PropertySpec.CONNECT_TIMEOUT.newProperty(
					connectTimeout));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKET_SETTINGS)) {
			SocketSettings socketSettings = settings.getLastValue(
					SettingSpec.CHAINING_SOCKET_SETTINGS, SocketSettings.class);
			properties.add(PropertySpec.SOCKET_SETTINGS.newProperty(
					socketSettings));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_ENABLED)) {
			Boolean sslEnabled = settings.getLastValue(
					SettingSpec.CHAINING_SSL_ENABLED, Boolean.class);
			properties.add(PropertySpec.SSL_ENABLED.newProperty(sslEnabled));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_ENABLED_CIPHER_SUITES)) {
			CipherSuites sslEnabledCipherSuites = settings.getLastValue(
					SettingSpec.CHAINING_SSL_ENABLED_CIPHER_SUITES, 
					CipherSuites.class);
			properties.add(PropertySpec.SSL_ENABLED_CIPHER_SUITES.newProperty(
					sslEnabledCipherSuites));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_ENABLED_PROTOCOLS)) {
			Protocols sslEnabledProtocols = settings.getLastValue(
					SettingSpec.CHAINING_SSL_ENABLED_PROTOCOLS, 
					Protocols.class);
			properties.add(PropertySpec.SSL_ENABLED_PROTOCOLS.newProperty(
					sslEnabledProtocols));			
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_KEY_STORE_FILE)) {
			File sslKeyStoreFile = settings.getLastValue(
					SettingSpec.CHAINING_SSL_KEY_STORE_FILE, File.class);
			properties.add(PropertySpec.SSL_KEY_STORE_FILE.newProperty(
					sslKeyStoreFile));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_KEY_STORE_PASSWORD)) {
			EncryptedPassword sslKeyStorePassword = settings.getLastValue(
					SettingSpec.CHAINING_SSL_KEY_STORE_PASSWORD, 
					EncryptedPassword.class);
			properties.add(PropertySpec.SSL_KEY_STORE_PASSWORD.newProperty(
					sslKeyStorePassword));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_KEY_STORE_TYPE)) {
			String sslKeyStoreType = settings.getLastValue(
					SettingSpec.CHAINING_SSL_KEY_STORE_TYPE, String.class);
			properties.add(PropertySpec.SSL_KEY_STORE_TYPE.newProperty(
					sslKeyStoreType));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_PROTOCOL)) {
			String sslProtocol = settings.getLastValue(
					SettingSpec.CHAINING_SSL_PROTOCOL, String.class);
			properties.add(PropertySpec.SSL_PROTOCOL.newProperty(sslProtocol));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_TRUST_STORE_FILE)) {
			File sslTrustStoreFile = settings.getLastValue(
					SettingSpec.CHAINING_SSL_TRUST_STORE_FILE, File.class);
			properties.add(PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
					sslTrustStoreFile));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_TRUST_STORE_PASSWORD)) {
			EncryptedPassword sslTrustStorePassword = settings.getLastValue(
					SettingSpec.CHAINING_SSL_TRUST_STORE_PASSWORD, 
					EncryptedPassword.class);
			properties.add(PropertySpec.SSL_TRUST_STORE_PASSWORD.newProperty(
					sslTrustStorePassword));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_TRUST_STORE_TYPE)) {
			String sslTrustStoreType = settings.getLastValue(
					SettingSpec.CHAINING_SSL_TRUST_STORE_TYPE, String.class);
			properties.add(PropertySpec.SSL_TRUST_STORE_TYPE.newProperty(
					sslTrustStoreType));
		}
		return properties;
	}
	
	public static SocksClient newSocksClient(
			final Configuration configuration) {
		Settings settings = configuration.getSettings();
		SocksServerUri socksServerUri = settings.getLastValue(
				SettingSpec.CHAINING_SOCKS_SERVER_URI, SocksServerUri.class);
		if (socksServerUri == null) {
			return null;
		}
		List<Property> properties = new ArrayList<Property>();
		properties.addAll(getSocksClientProperties(configuration));
		SocksClient socksClient = null;
		if (socksServerUri instanceof Socks5ServerUri) {
			properties.addAll(getSocks5ClientProperties(configuration));
			Socks5ServerUri socks5ServerUri = (Socks5ServerUri) socksServerUri;
			socksClient = socks5ServerUri.newSocksClient(Properties.newInstance(
					properties));
		} else {
			throw new AssertionError(String.format(
					"unhandled %s: %s", 
					SocksServerUri.class.getSimpleName(), 
					socksServerUri.getClass().getSimpleName()));
		}
		return socksClient;
	}
	
	private SocksClientFactory() { }
	
}
