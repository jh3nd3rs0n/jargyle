package com.github.jh3nd3rs0n.jargyle.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class SocksClient {

	public static SocksClient newInstance() {
		SocksServerUri socksServerUri = SocksServerUri.newInstance();
		SocksClient socksClient = null;
		if (socksServerUri != null) {
			List<Property<? extends Object>> properties = 
					new ArrayList<Property<? extends Object>>();
			for (PropertySpec<Object> propertySpec 
					: SocksClientPropertySpecConstants.values()) {
				String property = System.getProperty(propertySpec.getName());
				if (property != null) {
					properties.add(propertySpec.newPropertyWithParsedValue(
							property));
				}
			}			
			socksClient = socksServerUri.newSocksClient(
					Properties.of(properties));
		}
		return socksClient;
	}
	
	private final SocksClient chainedSocksClient;
	private final Properties properties;
	private final SocksServerUri socksServerUri;

	public SocksClient(final SocksServerUri serverUri, final Properties props) {
		this(serverUri, props, null);
	}
	
	public SocksClient(
			final SocksServerUri serverUri, 
			final Properties props,
			final SocksClient chainedClient) {
		Objects.requireNonNull(
				serverUri, "SOCKS server URI must not be null");
		Objects.requireNonNull(props, "Properties must not be null");
		this.chainedSocksClient = chainedClient;
		this.properties = props;
		this.socksServerUri = serverUri;
	}

	public final SocksClient getChainedSocksClient() {
		return this.chainedSocksClient;
	}

	public final Properties getProperties() {
		return this.properties;
	}
	
	public final SocksServerUri getSocksServerUri() {
		return this.socksServerUri;
	}

	public abstract SocksNetObjectFactory newSocksNetObjectFactory();

	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getSimpleName())
			.append(" [chainedSocksClient=")
			.append(this.chainedSocksClient)		
			.append(", socksServerUri=")
			.append(this.socksServerUri)
			.append("]");
		return builder.toString();
	}
	
}
