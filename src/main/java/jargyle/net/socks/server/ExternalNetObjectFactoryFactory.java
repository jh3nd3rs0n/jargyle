package jargyle.net.socks.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ietf.jgss.Oid;

import jargyle.net.DatagramSocketFactory;
import jargyle.net.DefaultNetObjectFactoryFactory;
import jargyle.net.Host;
import jargyle.net.HostResolverFactory;
import jargyle.net.NetObjectFactoryFactory;
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
import jargyle.security.EncryptedPassword;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

final class ExternalNetObjectFactoryFactory extends NetObjectFactoryFactory {
	
	private static Property<Object> cast(
			final Property<? extends Object> property) {
		@SuppressWarnings("unchecked")
		Property<Object> prop = (Property<Object>) property;
		return prop;
	}
	
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private NetObjectFactoryFactory netObjectFactoryFactory;
		
	public ExternalNetObjectFactoryFactory(final Configuration config) {
		this.configuration = config;
		this.lastConfiguration = null;
		this.netObjectFactoryFactory = null;
	}
	
	private NetObjectFactoryFactory getNetObjectFactoryFactory() {
		if (!Configuration.equals(this.lastConfiguration, this.configuration)) {
			this.netObjectFactoryFactory = this.newNetObjectFactoryFactory();
			this.lastConfiguration = ImmutableConfiguration.newInstance(
					this.configuration);
		}
		return this.netObjectFactoryFactory;
	}
	
	@Override
	public DatagramSocketFactory newDatagramSocketFactory() {
		return this.getNetObjectFactoryFactory().newDatagramSocketFactory();
	}
	
	@Override
	public HostResolverFactory newHostResolverFactory() {
		return this.getNetObjectFactoryFactory().newHostResolverFactory();		
	}
	
	private NetObjectFactoryFactory newNetObjectFactoryFactory() {
		SocksClient client = this.newSocksClient();
		if (client != null) {
			return client.newNetObjectFactoryFactory();
		}
		return new DefaultNetObjectFactoryFactory();
	}
	
	@Override
	public ServerSocketFactory newServerSocketFactory() {
		return this.getNetObjectFactoryFactory().newServerSocketFactory();		
	}
	
	@Override
	public SocketFactory newSocketFactory() {
		return this.getNetObjectFactoryFactory().newSocketFactory();
	}
	
	private List<Property<Object>> newSocks5ClientProperties() {
		Settings settings = this.configuration.getSettings();
		List<Property<Object>> properties =	new ArrayList<Property<Object>>();
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_AUTH_METHODS)) {
			AuthMethods authMethods = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_AUTH_METHODS);
			properties.add(cast(PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
					authMethods)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_FORWARD_HOSTNAME_RESOLUTION)) {
			Boolean forwardHostnameResolution = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_FORWARD_HOSTNAME_RESOLUTION);
			properties.add(cast(
					PropertySpec.SOCKS5_FORWARD_HOSTNAME_RESOLUTION.newProperty(
							forwardHostnameResolution)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_MECHANISM_OID)) {
			Oid gssapiMechanismOid = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_MECHANISM_OID);
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_MECHANISM_OID.newProperty(
							gssapiMechanismOid)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL)) {
			Boolean gssapiNecReferenceImpl = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL);
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newProperty(
							gssapiNecReferenceImpl)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_PROTECTION_LEVELS)) {
			GssapiProtectionLevels gssapiProtectionLevels =	settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_PROTECTION_LEVELS);
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.newProperty(
							gssapiProtectionLevels)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_SERVICE_NAME)) {
			String gssapiServiceName = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_GSSAPI_SERVICE_NAME);
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.newProperty(
							gssapiServiceName)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKS5_USERNAME_PASSWORD)) {
			UsernamePassword usernamePassword = settings.getLastValue(
					SettingSpec.CHAINING_SOCKS5_USERNAME_PASSWORD);
			if (usernamePassword != null) {
				properties.add(cast(PropertySpec.SOCKS5_USERNAME.newProperty(
						usernamePassword.getUsername())));
				properties.add(cast(PropertySpec.SOCKS5_PASSWORD.newProperty(
						usernamePassword.getEncryptedPassword())));			
			}
		}
		return properties;		
	}
	
	private SocksClient newSocksClient() {
		Settings settings = this.configuration.getSettings();
		SocksServerUri socksServerUri = settings.getLastValue(
				SettingSpec.CHAINING_SOCKS_SERVER_URI);
		if (socksServerUri == null) {
			return null;
		}
		List<Property<? extends Object>> properties = 
				new ArrayList<Property<? extends Object>>();
		properties.addAll(this.newSocksClientProperties());
		properties.addAll(this.newSocksClientSslProperties());
		properties.addAll(this.newSocks5ClientProperties());
		return socksServerUri.newSocksClient(Properties.newInstance(properties));
	}
	
	private List<Property<Object>> newSocksClientProperties() {
		Settings settings = this.configuration.getSettings();
		List<Property<Object>> properties = new ArrayList<Property<Object>>();
		if (settings.containsNondefaultValue(SettingSpec.CHAINING_BIND_HOST)) {
			Host bindHost = settings.getLastValue(
					SettingSpec.CHAINING_BIND_HOST);
			properties.add(cast(PropertySpec.BIND_HOST.newProperty(bindHost)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_CONNECT_TIMEOUT)) {
			PositiveInteger connectTimeout = settings.getLastValue(
					SettingSpec.CHAINING_CONNECT_TIMEOUT);
			properties.add(cast(PropertySpec.CONNECT_TIMEOUT.newProperty(
					connectTimeout)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SOCKET_SETTINGS)) {
			SocketSettings socketSettings = settings.getLastValue(
					SettingSpec.CHAINING_SOCKET_SETTINGS);
			properties.add(cast(PropertySpec.SOCKET_SETTINGS.newProperty(
					socketSettings)));
		}
		return properties;		
	}
	
	private List<Property<Object>> newSocksClientSslProperties() {
		Settings settings = this.configuration.getSettings();
		List<Property<Object>> properties = new ArrayList<Property<Object>>();
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_ENABLED)) {
			Boolean sslEnabled = settings.getLastValue(
					SettingSpec.CHAINING_SSL_ENABLED);
			properties.add(cast(PropertySpec.SSL_ENABLED.newProperty(
					sslEnabled)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_ENABLED_CIPHER_SUITES)) {
			Strings sslEnabledCipherSuites = settings.getLastValue(
					SettingSpec.CHAINING_SSL_ENABLED_CIPHER_SUITES);
			properties.add(cast(
					PropertySpec.SSL_ENABLED_CIPHER_SUITES.newProperty(
							sslEnabledCipherSuites)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_ENABLED_PROTOCOLS)) {
			Strings sslEnabledProtocols = settings.getLastValue(
					SettingSpec.CHAINING_SSL_ENABLED_PROTOCOLS);
			properties.add(cast(PropertySpec.SSL_ENABLED_PROTOCOLS.newProperty(
					sslEnabledProtocols)));			
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_KEY_STORE_FILE)) {
			File sslKeyStoreFile = settings.getLastValue(
					SettingSpec.CHAINING_SSL_KEY_STORE_FILE);
			properties.add(cast(PropertySpec.SSL_KEY_STORE_FILE.newProperty(
					sslKeyStoreFile)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_KEY_STORE_PASSWORD)) {
			EncryptedPassword sslKeyStorePassword = settings.getLastValue(
					SettingSpec.CHAINING_SSL_KEY_STORE_PASSWORD);
			properties.add(cast(PropertySpec.SSL_KEY_STORE_PASSWORD.newProperty(
					sslKeyStorePassword)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_KEY_STORE_TYPE)) {
			String sslKeyStoreType = settings.getLastValue(
					SettingSpec.CHAINING_SSL_KEY_STORE_TYPE);
			properties.add(cast(PropertySpec.SSL_KEY_STORE_TYPE.newProperty(
					sslKeyStoreType)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_PROTOCOL)) {
			String sslProtocol = settings.getLastValue(
					SettingSpec.CHAINING_SSL_PROTOCOL);
			properties.add(cast(PropertySpec.SSL_PROTOCOL.newProperty(
					sslProtocol)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_TRUST_STORE_FILE)) {
			File sslTrustStoreFile = settings.getLastValue(
					SettingSpec.CHAINING_SSL_TRUST_STORE_FILE);
			properties.add(cast(PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
					sslTrustStoreFile)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_TRUST_STORE_PASSWORD)) {
			EncryptedPassword sslTrustStorePassword = settings.getLastValue(
					SettingSpec.CHAINING_SSL_TRUST_STORE_PASSWORD);
			properties.add(cast(
					PropertySpec.SSL_TRUST_STORE_PASSWORD.newProperty(
							sslTrustStorePassword)));
		}
		if (settings.containsNondefaultValue(
				SettingSpec.CHAINING_SSL_TRUST_STORE_TYPE)) {
			String sslTrustStoreType = settings.getLastValue(
					SettingSpec.CHAINING_SSL_TRUST_STORE_TYPE);
			properties.add(cast(PropertySpec.SSL_TRUST_STORE_TYPE.newProperty(
					sslTrustStoreType)));
		}
		return properties;		
	}
	
}
