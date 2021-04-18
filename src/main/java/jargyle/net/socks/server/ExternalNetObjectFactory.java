package jargyle.net.socks.server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.ietf.jgss.Oid;

import jargyle.net.Host;
import jargyle.net.HostResolver;
import jargyle.net.NetObjectFactory;
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
import jargyle.util.Criteria;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

final class ExternalNetObjectFactory extends NetObjectFactory {
	
	private static Property<Object> cast(
			final Property<? extends Object> property) {
		@SuppressWarnings("unchecked")
		Property<Object> prop = (Property<Object>) property;
		return prop;
	}
	
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private NetObjectFactory netObjectFactory;

	public ExternalNetObjectFactory(final Configuration config) {
		this.configuration = config;
		this.lastConfiguration = null;
		this.netObjectFactory = null;
	}
	
	private NetObjectFactory getNetObjectFactory() {
		if (!ConfigurationsHelper.equals(
				this.lastConfiguration, this.configuration)) {
			this.netObjectFactory = this.newNetObjectFactory();
			this.lastConfiguration = ImmutableConfiguration.newInstance(
					this.configuration);
		}
		return this.netObjectFactory;
	}
	
	@Override
	public DatagramSocket newDatagramSocket() throws SocketException {
		return this.getNetObjectFactory().newDatagramSocket();
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port) throws SocketException {
		return this.getNetObjectFactory().newDatagramSocket(port);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final int port, final InetAddress laddr) throws SocketException {
		return this.getNetObjectFactory().newDatagramSocket(port, laddr);
	}

	@Override
	public DatagramSocket newDatagramSocket(
			final SocketAddress bindaddr) throws SocketException {
		return this.getNetObjectFactory().newDatagramSocket(bindaddr);
	}

	@Override
	public HostResolver newHostResolver() {
		return this.getNetObjectFactory().newHostResolver();
	}

	private NetObjectFactory newNetObjectFactory() {
		SocksClient client = this.newSocksClient();
		if (client != null) {
			return client.newSocksNetObjectFactory();
		}
		return NetObjectFactory.newInstance();
	}

	@Override
	public ServerSocket newServerSocket() throws IOException {
		return this.getNetObjectFactory().newServerSocket();
	}

	@Override
	public ServerSocket newServerSocket(final int port) throws IOException {
		return this.getNetObjectFactory().newServerSocket(port);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, final int backlog) throws IOException {
		return this.getNetObjectFactory().newServerSocket(port, backlog);
	}

	@Override
	public ServerSocket newServerSocket(
			final int port, 
			final int backlog, 
			final InetAddress bindAddr) throws IOException {
		return this.getNetObjectFactory().newServerSocket(
				port, backlog, bindAddr);
	}

	@Override
	public Socket newSocket() {
		return this.getNetObjectFactory().newSocket();
	}

	@Override
	public Socket newSocket(
			final InetAddress address, final int port) throws IOException {
		return this.getNetObjectFactory().newSocket(address, port);
	}

	@Override
	public Socket newSocket(
			final InetAddress address, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return this.getNetObjectFactory().newSocket(
				address, port, localAddr, localPort);
	}

	@Override
	public Socket newSocket(
			final String host, 
			final int port) throws UnknownHostException, IOException {
		return this.getNetObjectFactory().newSocket(host, port);
	}
	
	@Override
	public Socket newSocket(
			final String host, 
			final int port, 
			final InetAddress localAddr, 
			final int localPort) throws IOException {
		return this.getNetObjectFactory().newSocket(
				host, port, localAddr, localPort);
	}
	
	private List<Property<Object>> newSocks5ClientProperties(
			final Setting<Object> setting) {
		SettingSpec<Object> settingSpec = setting.getSettingSpec();
		List<Property<Object>> properties = new ArrayList<Property<Object>>();
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_AUTH_METHODS)) {
			AuthMethods authMethods = (AuthMethods) setting.getValue();
			properties.add(cast(PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
					authMethods)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_MECHANISM_OID)) {
			Oid gssapiMechanismOid = (Oid) setting.getValue();
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_MECHANISM_OID.newProperty(
							gssapiMechanismOid)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_NEC_REFERENCE_IMPL)) {
			Boolean gssapiNecReferenceImpl = (Boolean) setting.getValue();
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_NEC_REFERENCE_IMPL.newProperty(
							gssapiNecReferenceImpl)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_PROTECTION_LEVELS)) {
			GssapiProtectionLevels gssapiProtectionLevels =	
					(GssapiProtectionLevels) setting.getValue();
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_PROTECTION_LEVELS.newProperty(
							gssapiProtectionLevels)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_GSSAPI_SERVICE_NAME)) {
			String gssapiServiceName = (String) setting.getValue();
			properties.add(cast(
					PropertySpec.SOCKS5_GSSAPI_SERVICE_NAME.newProperty(
							gssapiServiceName)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_RESOLVE_HOST_NAMES_THROUGH_SERVER)) {
			Boolean resolveHostNamesThroughServer = 
					(Boolean) setting.getValue();
			properties.add(cast(
					PropertySpec.SOCKS5_RESOLVE_HOST_NAMES_THROUGH_SERVER.newProperty(
							resolveHostNamesThroughServer)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_SERVER_RESOLVABLE_HOST_NAME_CRITERIA)) {
			Criteria serverResolvableHostNameCriteria = 
					(Criteria) setting.getValue();
			properties.add(cast(
					PropertySpec.SOCKS5_SERVER_RESOLVABLE_HOST_NAME_CRITERIA.newProperty(
							serverResolvableHostNameCriteria)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_SYSTEM_RESOLVABLE_HOST_NAME_CRITERIA)) {
			Criteria systemResolvableHostNameCriteria = 
					(Criteria) setting.getValue();
			properties.add(cast(
					PropertySpec.SOCKS5_SYSTEM_RESOLVABLE_HOST_NAME_CRITERIA.newProperty(
							systemResolvableHostNameCriteria)));
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SOCKS5_USERNAME_PASSWORD)) {
			UsernamePassword usernamePassword = 
					(UsernamePassword) setting.getValue();
			properties.add(cast(PropertySpec.SOCKS5_USERNAME.newProperty(
					usernamePassword.getUsername())));
			properties.add(cast(PropertySpec.SOCKS5_PASSWORD.newProperty(
					usernamePassword.getEncryptedPassword())));
		}		
		return properties;
	}
	
	private SocksClient newSocksClient() {
		Settings settings = this.configuration.getSettings();
		SocksServerUri socksServerUri = null;
		List<Property<? extends Object>> properties = 
				new ArrayList<Property<? extends Object>>();
		SocksClient chainedSocksClient = null;
		for (Setting<Object> setting : settings.toList()) {
			SettingSpec<Object> settingSpec = setting.getSettingSpec();
			if (settingSpec.equals(SettingSpec.CHAINING_SOCKS_SERVER_URI)) {
				if (socksServerUri != null) {
					chainedSocksClient = socksServerUri.newSocksClient(
							Properties.newInstance(properties), 
							chainedSocksClient);
					properties.clear();
				}
				socksServerUri = (SocksServerUri) setting.getValue();
				continue;
			}
			List<Property<Object>> props = this.newSocksClientProperties(
					setting);
			if (props.size() != 0) {
				properties.addAll(props);
				continue;
			}
			props = this.newSocksClientDtlsProperties(setting);
			if (props.size() != 0) {
				properties.addAll(props);
				continue;
			}
			props = this.newSocksClientSslProperties(setting);
			if (props.size() != 0) {
				properties.addAll(props);
				continue;
			}
			props = this.newSocks5ClientProperties(setting);
			if (props.size() != 0) {
				properties.addAll(props);
			}
		}
		if (socksServerUri == null) {
			return null;
		}
		return socksServerUri.newSocksClient(
				Properties.newInstance(properties), chainedSocksClient);
	}
	
	private List<Property<Object>> newSocksClientDtlsProperties(
			final Setting<Object> setting) {
		SettingSpec<Object> settingSpec = setting.getSettingSpec();
		List<Property<Object>> properties = new ArrayList<Property<Object>>();
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_ENABLED)) {
			Boolean dtlsEnabled = (Boolean) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_ENABLED.newProperty(
					dtlsEnabled)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_DTLS_ENABLED_CIPHER_SUITES)) {
			Strings dtlsEnabledCipherSuites = (Strings) setting.getValue();
			properties.add(cast(
					PropertySpec.DTLS_ENABLED_CIPHER_SUITES.newProperty(
							dtlsEnabledCipherSuites)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_ENABLED_PROTOCOLS)) {
			Strings dtlsEnabledProtocols = (Strings) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_ENABLED_PROTOCOLS.newProperty(
					dtlsEnabledProtocols)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_KEY_STORE_FILE)) {
			File dtlsKeyStoreFile = (File) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_KEY_STORE_FILE.newProperty(
					dtlsKeyStoreFile)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_KEY_STORE_PASSWORD)) {
			EncryptedPassword dtlsKeyStorePassword = 
					(EncryptedPassword) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_KEY_STORE_PASSWORD.newProperty(
					dtlsKeyStorePassword)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_KEY_STORE_TYPE)) {
			String dtlsKeyStoreType = (String) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_KEY_STORE_TYPE.newProperty(
					dtlsKeyStoreType)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_MAX_PACKET_SIZE)) {
			PositiveInteger dtlsMaxPacketSize = 
					(PositiveInteger) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_MAX_PACKET_SIZE.newProperty(
					dtlsMaxPacketSize)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_PROTOCOL)) {
			String dtlsProtocol = (String) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_PROTOCOL.newProperty(
					dtlsProtocol)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_TRUST_STORE_FILE)) {
			File dtlsTrustStoreFile = (File) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_TRUST_STORE_FILE.newProperty(
					dtlsTrustStoreFile)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_DTLS_TRUST_STORE_PASSWORD)) {
			EncryptedPassword dtlsTrustStorePassword = 
					(EncryptedPassword) setting.getValue();
			properties.add(cast(
					PropertySpec.DTLS_TRUST_STORE_PASSWORD.newProperty(
							dtlsTrustStorePassword)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_DTLS_TRUST_STORE_TYPE)) {
			String dtlsTrustStoreType = (String) setting.getValue();
			properties.add(cast(PropertySpec.DTLS_TRUST_STORE_TYPE.newProperty(
					dtlsTrustStoreType)));
		}		
		return properties;
	}
	
	private List<Property<Object>> newSocksClientProperties(
			final Setting<Object> setting) {
		SettingSpec<Object> settingSpec = setting.getSettingSpec();
		List<Property<Object>> properties = new ArrayList<Property<Object>>();
		if (settingSpec.equals(SettingSpec.CHAINING_BIND_HOST)) {
			Host bindHost = (Host) setting.getValue();
			properties.add(cast(PropertySpec.BIND_HOST.newProperty(bindHost)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_CONNECT_TIMEOUT)) {
			PositiveInteger connectTimeout = 
					(PositiveInteger) setting.getValue();
			properties.add(cast(PropertySpec.CONNECT_TIMEOUT.newProperty(
					connectTimeout)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SOCKET_SETTINGS)) {
			SocketSettings socketSettings = (SocketSettings) setting.getValue();
			properties.add(cast(PropertySpec.SOCKET_SETTINGS.newProperty(
					socketSettings)));
		}		
		return properties;
	}
	
	private List<Property<Object>> newSocksClientSslProperties(
			final Setting<Object> setting) {
		SettingSpec<Object> settingSpec = setting.getSettingSpec();
		List<Property<Object>> properties = new ArrayList<Property<Object>>();
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_ENABLED)) {
			Boolean sslEnabled = (Boolean) setting.getValue();
			properties.add(cast(PropertySpec.SSL_ENABLED.newProperty(
					sslEnabled)));
			return properties;
		}
		if (settingSpec.equals(
				SettingSpec.CHAINING_SSL_ENABLED_CIPHER_SUITES)) {
			Strings sslEnabledCipherSuites = (Strings) setting.getValue();
			properties.add(cast(
					PropertySpec.SSL_ENABLED_CIPHER_SUITES.newProperty(
							sslEnabledCipherSuites)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_ENABLED_PROTOCOLS)) {
			Strings sslEnabledProtocols = (Strings) setting.getValue();
			properties.add(cast(PropertySpec.SSL_ENABLED_PROTOCOLS.newProperty(
					sslEnabledProtocols)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_KEY_STORE_FILE)) {
			File sslKeyStoreFile = (File) setting.getValue();
			properties.add(cast(PropertySpec.SSL_KEY_STORE_FILE.newProperty(
					sslKeyStoreFile)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_KEY_STORE_PASSWORD)) {
			EncryptedPassword sslKeyStorePassword = 
					(EncryptedPassword) setting.getValue();
			properties.add(cast(PropertySpec.SSL_KEY_STORE_PASSWORD.newProperty(
					sslKeyStorePassword)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_KEY_STORE_TYPE)) {
			String sslKeyStoreType = (String) setting.getValue();
			properties.add(cast(PropertySpec.SSL_KEY_STORE_TYPE.newProperty(
					sslKeyStoreType)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_PROTOCOL)) {
			String sslProtocol = (String) setting.getValue();
			properties.add(cast(PropertySpec.SSL_PROTOCOL.newProperty(
					sslProtocol)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_TRUST_STORE_FILE)) {
			File sslTrustStoreFile = (File) setting.getValue();
			properties.add(cast(PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
					sslTrustStoreFile)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_TRUST_STORE_PASSWORD)) {
			EncryptedPassword sslTrustStorePassword = 
					(EncryptedPassword) setting.getValue();
			properties.add(cast(
					PropertySpec.SSL_TRUST_STORE_PASSWORD.newProperty(
							sslTrustStorePassword)));
			return properties;
		}
		if (settingSpec.equals(SettingSpec.CHAINING_SSL_TRUST_STORE_TYPE)) {
			String sslTrustStoreType = (String) setting.getValue();
			properties.add(cast(PropertySpec.SSL_TRUST_STORE_TYPE.newProperty(
					sslTrustStoreType)));
		}		
		return properties;
	}

}
