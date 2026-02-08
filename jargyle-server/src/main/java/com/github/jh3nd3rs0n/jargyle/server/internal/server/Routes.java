package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.client.*;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.server.*;

import java.util.*;

public final class Routes {
	
	private static abstract class SettingConverter {
		
		public abstract Object convert(final Setting<Object> setting);
		
	}
	
	private static final class SettingToPropertyConverter 
		extends SettingConverter {
		
		private final PropertySpec<Object> propertySpec;
		
		public SettingToPropertyConverter(
				final PropertySpec<? extends Object> propSpec) {
			@SuppressWarnings("unchecked")
			PropertySpec<Object> prpSpec = (PropertySpec<Object>) propSpec;
			this.propertySpec = prpSpec;
		}
		
		@Override
		public Object convert(final Setting<Object> setting) {
			return this.propertySpec.newProperty(setting.getValue());
		}
		
	}
	
	private static final class SettingToValueConverter 
		extends SettingConverter {
		
		public SettingToValueConverter() {	}
		
		@Override
		public Object convert(final Setting<Object> setting) {
			return setting.getValue();
		}
		
	}
	
	private static final Map<SettingSpec<? extends Object>, SettingConverter> SETTING_CONVERTER_MAP;
	
	static {
		SETTING_CONVERTER_MAP =
				new HashMap<SettingSpec<? extends Object>, SettingConverter>();
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_SOCKS_SERVER_URI, 
				new SettingToValueConverter());
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_ENABLED));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED_CIPHER_SUITES, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_ENABLED_CIPHER_SUITES));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_ENABLED_PROTOCOLS, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_ENABLED_PROTOCOLS));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_PROTOCOL, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_PROTOCOL));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_FILE, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_BYTES,
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_BYTES));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_TYPE, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_WRAPPED_RECEIVE_BUFFER_SIZE,
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_WRAPPED_RECEIVE_BUFFER_SIZE));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_BIND_HOST, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_BIND_HOST));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_BIND_HOST_ADDRESS_TYPES,
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_BIND_HOST_ADDRESS_TYPES));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_BIND_PORT_RANGES, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_CONNECT_TIMEOUT, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_NET_INTERFACE,
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_NET_INTERFACE));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_SOCKET_SETTINGS, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_MECHANISM_OID));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_PROTECTION_LEVELS));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SERVICE_NAME));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_CONF));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_GSSAPIAUTHMETHOD_SUGGESTED_INTEG));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_METHODS,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_METHODS));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_SOCKS_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_SOCKS_HOST_RESOLVER_RESOLVE_FROM_SOCKS_SERVER));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_USERPASSAUTHMETHOD_PASSWORD,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_PASSWORD));
		SETTING_CONVERTER_MAP.put(
				ChainingSocksSettingSpecConstants.CHAINING_SOCKS_USERPASSAUTHMETHOD_USERNAME,
				new SettingToPropertyConverter(
						SocksPropertySpecConstants.SOCKS_USERPASSAUTHMETHOD_USERNAME));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_MECHANISM_OID));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_NEC_REFERENCE_IMPL));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_PROTECTION_LEVELS));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SERVICE_NAME));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF,
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_CONF));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG,
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIAUTHMETHOD_SUGGESTED_INTEG));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_METHODS, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_METHODS));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE,
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_SOCKS5_DATAGRAM_SOCKET_CLIENT_INFO_UNAVAILABLE));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER,
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_SOCKS5_HOST_RESOLVER_RESOLVE_FROM_SOCKS5_SERVER));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTHMETHOD_PASSWORD, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_PASSWORD));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSAUTHMETHOD_USERNAME, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_USERPASSAUTHMETHOD_USERNAME));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_ENABLED));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED_CIPHER_SUITES, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_ENABLED_CIPHER_SUITES));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_ENABLED_PROTOCOLS, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_ENABLED_PROTOCOLS));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_FILE, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_KEY_STORE_FILE));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_BYTES,
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_KEY_STORE_BYTES));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_PASSWORD, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_KEY_STORE_PASSWORD));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_KEY_STORE_TYPE, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_KEY_STORE_TYPE));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_PROTOCOL, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_PROTOCOL));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_FILE, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_TRUST_STORE_FILE));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_BYTES,
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_TRUST_STORE_BYTES));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_PASSWORD, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_TRUST_STORE_PASSWORD));
		SETTING_CONVERTER_MAP.put(
				ChainingSslSettingSpecConstants.CHAINING_SSL_TRUST_STORE_TYPE, 
				new SettingToPropertyConverter(
						SslPropertySpecConstants.SSL_TRUST_STORE_TYPE));		
	}
	
	public static Routes newInstanceFrom(final Configuration configuration) {
		List<Route> routes = new ArrayList<Route>();
		Settings settings = configuration.getSettings();
		SocksServerUri socksServerUri = null;
		List<Property<? extends Object>> properties = 
				new ArrayList<Property<? extends Object>>();
		SocksClient chainedSocksClient = null;
		String routeId = null;
		for (Setting<Object> setting : settings.toList()) {
			SettingSpec<Object> settingSpec = setting.getSettingSpec();
			if (settingSpec.equals(
					ChainingGeneralSettingSpecConstants.CHAINING_ROUTE_ID)) {
				routeId = (String) setting.getValue();
				continue;
			}
			SettingConverter settingConverter = SETTING_CONVERTER_MAP.get(
					settingSpec);
			if (settingConverter == null) {
				continue;
			}
			Object obj = settingConverter.convert(setting);
			if (obj instanceof SocksServerUri) {
				SocksServerUri serverUri = (SocksServerUri) obj;
				if (socksServerUri != null) {
					chainedSocksClient = socksServerUri.newSocksClient(
							Properties.of(properties),
							chainedSocksClient);
					properties.clear();
					if (routeId != null) {
						routes.add(new ChainRoute(
								routeId, chainedSocksClient));
						chainedSocksClient = null;
						routeId = null;
					}
				}
				socksServerUri = serverUri;
				continue;
			}
			if (obj instanceof Property) {
				@SuppressWarnings("unchecked")
				Property<Object> prop = (Property<Object>) obj;
				properties.add(prop);
			}
		}
		String lastRouteId = settings.getLastValue(
				GeneralSettingSpecConstants.LAST_ROUTE_ID);
		if (socksServerUri == null) {
			routes.add(new DirectRoute(lastRouteId));
		} else {
			SocksClient socksClient = socksServerUri.newSocksClient(
					Properties.of(properties), chainedSocksClient);
			if (routeId == null) {
				routes.add(new ChainRoute(lastRouteId, socksClient));
			} else {
				routes.add(new ChainRoute(routeId, socksClient));
				routes.add(new DirectRoute(lastRouteId));
			}
		}
		return of(routes);
	}
	
	public static Routes of(final List<Route> rtes) {
		return new Routes(rtes);
	}
	
	public static Routes of(final Routes other) {
		return new Routes(other);
	}
	
	private final Map<String, Route> routes;
	
	private Routes(final List<Route> rtes) {
		Map<String, Route> map = new LinkedHashMap<String, Route>();
		for (Route rte : rtes) {
			String id = rte.getId();
			if (map.containsKey(id)) {
				map.remove(id);
			}
			map.put(id, rte);
		}
		this.routes = map;
	}
	
	private Routes(final Routes other) {
		this.routes = new LinkedHashMap<String, Route>(other.routes); 
	}

	public Route get(final String id) {
		return this.routes.get(id);
	}

	public Map<String, Route> toMap() {
		return Collections.unmodifiableMap(this.routes);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [routes=")
			.append(this.routes)
			.append("]");
		return builder.toString();
	}
	
}
