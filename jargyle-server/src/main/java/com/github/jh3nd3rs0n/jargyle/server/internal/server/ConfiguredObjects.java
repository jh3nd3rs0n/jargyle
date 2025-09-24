package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.DtlsDatagramSocketFactory;
import com.github.jh3nd3rs0n.jargyle.internal.net.ssl.SslSocketFactory;
import com.github.jh3nd3rs0n.jargyle.server.Configuration;

final class ConfiguredObjects {
	
	private final DtlsDatagramSocketFactory clientFacingDtlsDatagramSocketFactory;
	private final SslSocketFactory clientSslSocketFactory;
	private final Configuration configuration;
	private final Routes routes;
	private final Rules rules;
	
	public ConfiguredObjects(
			final DtlsDatagramSocketFactory clientFacingDtlsDatagramSockFactory,
			final SslSocketFactory clientSslSockFactory,
			final Configuration config,
			final Routes rtes,
			final Rules rls) {
		this.clientFacingDtlsDatagramSocketFactory = 
				clientFacingDtlsDatagramSockFactory;
		this.clientSslSocketFactory = clientSslSockFactory;
		this.configuration = config;
		this.routes = rtes;
		this.rules = rls;
	}

	public DtlsDatagramSocketFactory getClientFacingDtlsDatagramSocketFactory() {
		return this.clientFacingDtlsDatagramSocketFactory;
	}

	public SslSocketFactory getClientSslSocketFactory() {
		return this.clientSslSocketFactory;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public Routes getRoutes() {
		return this.routes;
	}

	public Rules getRules() {
		return this.rules;
	}

}
