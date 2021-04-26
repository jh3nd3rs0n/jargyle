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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import jargyle.net.socks.client.v5.userpassauth.UsernamePassword;
import jargyle.net.socks.transport.v5.AuthMethods;
import jargyle.net.socks.transport.v5.gssapiauth.ProtectionLevels;
import jargyle.security.EncryptedPassword;
import jargyle.util.PositiveInteger;
import jargyle.util.Strings;

final class NetObjectFactoryImpl extends NetObjectFactory {
	
	private static abstract class SettingConverter {
		
		public abstract List<Property<Object>> convertToProperties(
				final Setting<Object> setting);
		
	}
	
	private static final Map<SettingSpec<? extends Object>, SettingConverter> SETTING_CONVERTER_MAP =
			new HashMap<SettingSpec<? extends Object>, SettingConverter>();
	
	private static Property<Object> cast(
			final Property<? extends Object> property) {
		@SuppressWarnings("unchecked")
		Property<Object> prop = (Property<Object>) property;
		return prop;
	}
	
	private static void fillSettingConverterMapIfEmpty() {
		if (!SETTING_CONVERTER_MAP.isEmpty()) {
			return;
		}
		putChainingDtlsSettingConverters();
		putChainingDtlsKeyStoreSettingConverters();
		putChainingDtlsTrustStoreSettingConverters();
		putChainingSettingConverters();
		putChainingSocks5SettingConverters();
		putChainingSocks5GssapiAuthSettingConverters();
		putChainingSslSettingConverters();
		putChainingSslKeyStoreSettingConverters();
		putChainingSslTrustStoreSettingConverters();
	}
	
	private static Map<SettingSpec<? extends Object>, SettingConverter> getSettingConverterMap() {
		fillSettingConverterMapIfEmpty();
		return SETTING_CONVERTER_MAP;
	}
	
	private static void putChainingDtlsKeyStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_KEY_STORE_FILE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						File dtlsKeyStoreFile = (File) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_KEY_STORE_FILE.newProperty(
										dtlsKeyStoreFile)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_KEY_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						EncryptedPassword dtlsKeyStorePassword = 
								(EncryptedPassword) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_KEY_STORE_PASSWORD.newProperty(
										dtlsKeyStorePassword)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_KEY_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						String dtlsKeyStoreType = (String) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_KEY_STORE_TYPE.newProperty(
										dtlsKeyStoreType)));
						return properties;
					}
					
				});
	}
	
	private static void putChainingDtlsSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_ENABLED, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Boolean dtlsEnabled = (Boolean) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_ENABLED.newProperty(
										dtlsEnabled)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_ENABLED_CIPHER_SUITES, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Strings dtlsEnabledCipherSuites = 
								(Strings) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_ENABLED_CIPHER_SUITES.newProperty(
										dtlsEnabledCipherSuites)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_ENABLED_PROTOCOLS, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Strings dtlsEnabledProtocols = 
								(Strings) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_ENABLED_PROTOCOLS.newProperty(
										dtlsEnabledProtocols)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_MAX_PACKET_SIZE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						PositiveInteger dtlsMaxPacketSize = 
								(PositiveInteger) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_MAX_PACKET_SIZE.newProperty(
										dtlsMaxPacketSize)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_PROTOCOL, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						String dtlsProtocol = (String) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_PROTOCOL.newProperty(
										dtlsProtocol)));
						return properties;
					}
					
				});
	}
	
	private static void putChainingDtlsTrustStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_TRUST_STORE_FILE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						File dtlsTrustStoreFile = (File) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_TRUST_STORE_FILE.newProperty(
										dtlsTrustStoreFile)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_TRUST_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						EncryptedPassword dtlsTrustStorePassword = 
								(EncryptedPassword) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_TRUST_STORE_PASSWORD.newProperty(
										dtlsTrustStorePassword)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_DTLS_TRUST_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						String dtlsTrustStoreType = (String) setting.getValue();
						properties.add(cast(
								PropertySpec.DTLS_TRUST_STORE_TYPE.newProperty(
										dtlsTrustStoreType)));
						return properties;
					}
					
				});		
	}
	
	private static void putChainingSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_BIND_HOST, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Host bindHost = (Host) setting.getValue();
						properties.add(cast(PropertySpec.BIND_HOST.newProperty(
								bindHost)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_CONNECT_TIMEOUT, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						PositiveInteger connectTimeout = 
								(PositiveInteger) setting.getValue();
						properties.add(cast(
								PropertySpec.CONNECT_TIMEOUT.newProperty(
										connectTimeout)));
						return properties;
					} 
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKET_SETTINGS, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						SocketSettings socketSettings = 
								(SocketSettings) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKET_SETTINGS.newProperty(
										socketSettings)));
						return properties;
					}
					
				});
	}
	
	private static void putChainingSocks5GssapiAuthSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKS5_GSSAPIAUTH_MECHANISM_OID, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Oid mechanismOid = (Oid) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKS5_GSSAPIAUTH_MECHANISM_OID.newProperty(
										mechanismOid)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Boolean necReferenceImpl = (Boolean) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKS5_GSSAPIAUTH_NEC_REFERENCE_IMPL.newProperty(
										necReferenceImpl)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						ProtectionLevels protectionLevels =	
								(ProtectionLevels) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKS5_GSSAPIAUTH_PROTECTION_LEVELS.newProperty(
										protectionLevels)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKS5_GSSAPIAUTH_SERVICE_NAME, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						String serviceName = (String) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKS5_GSSAPIAUTH_SERVICE_NAME.newProperty(
										serviceName)));
						return properties;
					}
					
				});
	}
	
	private static void putChainingSocks5SettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKS5_AUTH_METHODS, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						AuthMethods authMethods = 
								(AuthMethods) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKS5_AUTH_METHODS.newProperty(
										authMethods)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKS5_RESOLVE_USE_RESOLVE_COMMAND, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Boolean useResolveCommand = 
								(Boolean) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKS5_RESOLVE_USE_RESOLVE_COMMAND.newProperty(
										useResolveCommand)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SOCKS5_USERPASSAUTH_USERNAME_PASSWORD, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						UsernamePassword usernamePassword = 
								(UsernamePassword) setting.getValue();
						properties.add(cast(
								PropertySpec.SOCKS5_USERPASSAUTH_USERNAME.newProperty(
										usernamePassword.getUsername())));
						properties.add(cast(
								PropertySpec.SOCKS5_USERPASSAUTH_PASSWORD.newProperty(
										usernamePassword.getEncryptedPassword())));
						return properties;
					}
					
				});
	}
	
	private static void putChainingSslKeyStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_KEY_STORE_FILE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						File sslKeyStoreFile = (File) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_KEY_STORE_FILE.newProperty(
										sslKeyStoreFile)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_KEY_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						EncryptedPassword sslKeyStorePassword = 
								(EncryptedPassword) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_KEY_STORE_PASSWORD.newProperty(
										sslKeyStorePassword)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_KEY_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						String sslKeyStoreType = (String) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_KEY_STORE_TYPE.newProperty(
										sslKeyStoreType)));
						return properties;
					}
					
				});
	}
	
	private static void putChainingSslSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_ENABLED, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Boolean sslEnabled = (Boolean) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_ENABLED.newProperty(
										sslEnabled)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_ENABLED_CIPHER_SUITES, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Strings sslEnabledCipherSuites = 
								(Strings) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_ENABLED_CIPHER_SUITES.newProperty(
										sslEnabledCipherSuites)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_ENABLED_PROTOCOLS, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						Strings sslEnabledProtocols = 
								(Strings) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_ENABLED_PROTOCOLS.newProperty(
										sslEnabledProtocols)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_PROTOCOL, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						String sslProtocol = (String) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_PROTOCOL.newProperty(
										sslProtocol)));
						return properties;
					}
					
				});
	}
	
	private static void putChainingSslTrustStoreSettingConverters() {
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_TRUST_STORE_FILE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						File sslTrustStoreFile = (File) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_TRUST_STORE_FILE.newProperty(
										sslTrustStoreFile)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_TRUST_STORE_PASSWORD, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						EncryptedPassword sslTrustStorePassword = 
								(EncryptedPassword) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_TRUST_STORE_PASSWORD.newProperty(
										sslTrustStorePassword)));
						return properties;
					}
					
				});
		SETTING_CONVERTER_MAP.put(
				SettingSpec.CHAINING_SSL_TRUST_STORE_TYPE, 
				new SettingConverter() {

					@Override
					public List<Property<Object>> convertToProperties(
							final Setting<Object> setting) {
						List<Property<Object>> properties = 
								new ArrayList<Property<Object>>();
						String sslTrustStoreType = (String) setting.getValue();
						properties.add(cast(
								PropertySpec.SSL_TRUST_STORE_TYPE.newProperty(
										sslTrustStoreType)));
						return properties;
					}
					
				});		
	}
	
	private final Configuration configuration;
	private Configuration lastConfiguration;
	private NetObjectFactory netObjectFactory;

	public NetObjectFactoryImpl(final Configuration config) {
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
	
	private SocksClient newSocksClient() {
		Settings settings = this.configuration.getSettings();
		Map<SettingSpec<? extends Object>, SettingConverter> settingConverterMap =
				getSettingConverterMap();
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
			SettingConverter settingConverter = settingConverterMap.get(
					settingSpec);
			if (settingConverter != null) {
				properties.addAll(settingConverter.convertToProperties(setting));
			}
		}
		if (socksServerUri == null) {
			return null;
		}
		return socksServerUri.newSocksClient(
				Properties.newInstance(properties), chainedSocksClient);
	}

}
