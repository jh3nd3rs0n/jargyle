package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.github.jh3nd3rs0n.jargyle.client.DtlsPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.GeneralPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.NetObjectFactory;
import com.github.jh3nd3rs0n.jargyle.client.Properties;
import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.client.Socks5PropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.client.SocksClient;
import com.github.jh3nd3rs0n.jargyle.client.SocksServerUri;
import com.github.jh3nd3rs0n.jargyle.client.SslPropertySpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingDtlsSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingGeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSocks5SettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.ChainingSslSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.GeneralSettingSpecConstants;
import com.github.jh3nd3rs0n.jargyle.server.Setting;
import com.github.jh3nd3rs0n.jargyle.server.SettingSpec;
import com.github.jh3nd3rs0n.jargyle.server.Settings;

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
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_MAX_PACKET_SIZE, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_MAX_PACKET_SIZE));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_PROTOCOL, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_PROTOCOL));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_FILE, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_FILE));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_PASSWORD, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_PASSWORD));
		SETTING_CONVERTER_MAP.put(
				ChainingDtlsSettingSpecConstants.CHAINING_DTLS_TRUST_STORE_TYPE, 
				new SettingToPropertyConverter(
						DtlsPropertySpecConstants.DTLS_TRUST_STORE_TYPE));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_BIND_HOST, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_BIND_HOST));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_BIND_PORT_RANGES, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_BIND_PORT_RANGES));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_CONNECT_TIMEOUT, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_CONNECT_TIMEOUT));
		SETTING_CONVERTER_MAP.put(
				ChainingGeneralSettingSpecConstants.CHAINING_CLIENT_SOCKET_SETTINGS, 
				new SettingToPropertyConverter(
						GeneralPropertySpecConstants.CLIENT_SOCKET_SETTINGS));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIMETHOD_MECHANISM_OID, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_MECHANISM_OID));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_NEC_REFERENCE_IMPL));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_PROTECTION_LEVELS));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIMETHOD_SERVICE_NAME, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SERVICE_NAME));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF,
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_CONF));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG,
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_GSSAPIMETHOD_SUGGESTED_INTEG));
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
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_PASSWORD, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_PASSWORD));
		SETTING_CONVERTER_MAP.put(
				ChainingSocks5SettingSpecConstants.CHAINING_SOCKS5_USERPASSMETHOD_USERNAME, 
				new SettingToPropertyConverter(
						Socks5PropertySpecConstants.SOCKS5_USERPASSMETHOD_USERNAME));
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
						routes.add(new Route(
								routeId,
								chainedSocksClient.newSocksNetObjectFactory()));
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
			routes.add(new Route(lastRouteId, NetObjectFactory.getInstance()));
		} else {
			SocksClient socksClient = socksServerUri.newSocksClient(
					Properties.of(properties), chainedSocksClient);
			if (routeId == null) {
				routes.add(new Route(
						lastRouteId, socksClient.newSocksNetObjectFactory()));
			} else {
				routes.add(new Route(
						routeId, socksClient.newSocksNetObjectFactory()));
				routes.add(new Route(
						lastRouteId, NetObjectFactory.getInstance()));				
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
