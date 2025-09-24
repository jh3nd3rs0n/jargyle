package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.util.concurrent.locks.ReentrantLock;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationsHelper;

final class ConfiguredObjectsProvider {

	private final Configuration configuration;
	private ConfiguredObjects configuredObjects;
	private Configuration lastConfiguration;
	private final ReentrantLock lock;
	
	public ConfiguredObjectsProvider(final Configuration config) {
		this.configuration = config;
		this.configuredObjects = null;
		this.lastConfiguration = null;
		this.lock = new ReentrantLock();
	}
	
	public ConfiguredObjects getConfiguredObjects() {
		this.lock.lock();
		try {
			Configuration config = Configuration.newUnmodifiableInstance(
					this.configuration);
			if (!ConfigurationsHelper.equals(this.lastConfiguration, config)) {
				this.configuredObjects =
						new ConfiguredObjects(
								ConfiguredDtlsDatagramSocketFactory.newInstance(
										config),
								ConfiguredSslSocketFactory.newInstance(config),
								config,
								Routes.newInstanceFrom(config),
								Rules.newInstanceFrom(config));
				this.lastConfiguration = config;
			}
		} finally {
			this.lock.unlock();
		}
		return this.configuredObjects;
	}
}
