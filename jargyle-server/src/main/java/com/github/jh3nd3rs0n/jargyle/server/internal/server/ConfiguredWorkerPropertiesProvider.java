package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.util.concurrent.locks.ReentrantLock;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationsHelper;

final class ConfiguredWorkerPropertiesProvider {

	private final Configuration configuration;
	private ConfiguredWorkerProperties configuredWorkerProperties;
	private Configuration lastConfiguration;
	private final ReentrantLock lock;
	
	public ConfiguredWorkerPropertiesProvider(final Configuration config) {
		this.configuration = config;
		this.configuredWorkerProperties = null;
		this.lastConfiguration = null;
		this.lock = new ReentrantLock();
	}
	
	public ConfiguredWorkerProperties getConfiguredWorkerProperties() {
		this.lock.lock();
		try {
			Configuration config = Configuration.newUnmodifiableInstance(
					this.configuration);
			if (!ConfigurationsHelper.equals(this.lastConfiguration, config)) {
				this.configuredWorkerProperties = new ConfiguredWorkerProperties(
						new DtlsDatagramSocketFactoryImpl(config),
						new SslSocketFactoryImpl(config),
						config,
						Routes.newInstanceFrom(config),
						Rules.newInstanceFrom(config));
				this.lastConfiguration = config;
			}
		} finally {
			this.lock.unlock();
		}
		return this.configuredWorkerProperties;
	}
}
