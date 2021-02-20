package jargyle.net.socks.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ietf.jgss.Oid;

import jargyle.net.DatagramSocketFactory;
import jargyle.net.DefaultNetFactory;
import jargyle.net.Host;
import jargyle.net.HostnameResolverFactory;
import jargyle.net.NetFactory;
import jargyle.net.ServerSocketFactory;
import jargyle.net.SocketFactory;
import jargyle.net.SocketSettings;
import jargyle.net.socks.client.Properties;
import jargyle.net.socks.client.Property;
import jargyle.net.socks.client.PropertySpec;
import jargyle.net.socks.client.SocksClient;
import jargyle.net.socks.client.SocksServerUri;
import jargyle.net.socks.client.v5.UsernamePassword;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.gssapiauth.GssapiProtectionLevels;
import jargyle.net.ssl.CipherSuites;
import jargyle.net.ssl.Protocols;
import jargyle.security.EncryptedPassword;
import jargyle.util.PositiveInteger;

final class ExternalNetFactory extends NetFactory {
		
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private NetFactory netFactory;
		
	public ExternalNetFactory(final Configuration config) {
		this.configuration = config;
		this.lastConfiguration = null;
		this.netFactory = null;
	}
	
	private NetFactory getNetFactory() {
		if (!Configuration.equals(this.lastConfiguration, this.configuration)) {
			this.netFactory = this.newNetFactory();
			this.lastConfiguration = ImmutableConfiguration.newInstance(
					this.configuration);
		}
		return this.netFactory;
	}
	
	@Override
	public DatagramSocketFactory newDatagramSocketFactory() {
		return this.getNetFactory().newDatagramSocketFactory();
	}
	
	@Override
	public HostnameResolverFactory newHostnameResolverFactory() {
		return this.getNetFactory().newHostnameResolverFactory();		
	}
	
	private NetFactory newNetFactory() {
		SocksClient client = this.newSocksClient();
		if (client != null) {
			return client.newNetFactory();
		}
		return new DefaultNetFactory();
	}
	
	@Override
	public ServerSocketFactory newServerSocketFactory() {
		return this.getNetFactory().newServerSocketFactory();		
	}
	
	@Override
	public SocketFactory newSocketFactory() {
		return this.getNetFactory().newSocketFactory();
	}
	
	private List<Property> newSocks5ClientProperties() {
		Settings settings = this.configuration.getSettings();
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
				SettingSpec.CHAINING_SOCKS5_FORWARD_HOSTNAME_RESOLUTION)) {
			Boolean forwardHostnameResolution = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_FORWARD_HOSTNAME_RESOLUTION, 
					Boolean.class);
			properties.add(PropertySpec.SOCKS5_FORWARD_HOSTNAME_RESOLUTION.newProperty(
					forwardHostnameResolution));
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
	
	private SocksClient newSocksClient() {
		Settings settings = this.configuration.getSettings();
		SocksServerUri socksServerUri = settings.getLastValue(
				SettingSpec.CHAINING_SOCKS_SERVER_URI, SocksServerUri.class);
		if (socksServerUri == null) {
			return null;
		}
		List<Property> properties = new ArrayList<Property>();
		properties.addAll(this.newSocksClientProperties());
		properties.addAll(this.newSocks5ClientProperties());
		return socksServerUri.newSocksClient(Properties.newInstance(properties));
	}
	
	private List<Property> newSocksClientProperties() {
		Settings settings = this.configuration.getSettings();
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
	
}
