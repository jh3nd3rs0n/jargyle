package com.github.jh3nd3rs0n.jargyle.server.internal.server;

import java.util.concurrent.locks.ReentrantLock;

import com.github.jh3nd3rs0n.jargyle.server.Configuration;
import com.github.jh3nd3rs0n.jargyle.server.ConfigurationsHelper;

final class ConfiguredWorkerParamsProvider {

	private final Configuration configuration;
	private ConfiguredWorkerParams configuredWorkerParams;
	private Configuration lastConfiguration;
	private final ReentrantLock lock;
	
	public ConfiguredWorkerParamsProvider(final Configuration config) {
		this.configuration = config;
		this.configuredWorkerParams = null;		
		this.lastConfiguration = null;
		this.lock = new ReentrantLock();
	}
	
	public ConfiguredWorkerParams getConfiguredWorkerParams() {
		this.lock.lock();
		try {
			Configuration config = Configuration.newUnmodifiableInstance(
					this.configuration);
			if (!ConfigurationsHelper.equals(this.lastConfiguration, config)) {
				this.configuredWorkerParams = new ConfiguredWorkerParams(
						DtlsDatagramSocketFactoryImpl.isDtlsEnabled(config) ?
								new DtlsDatagramSocketFactoryImpl(config) : null,
						SslSocketFactoryImpl.isSslEnabled(config) ?
								new SslSocketFactoryImpl(config) : null,
						config,
						Routes.newInstance(config),
						Rules.newInstance(config));
				this.lastConfiguration = config;
			}
		} finally {
			this.lock.unlock();
		}
		return this.configuredWorkerParams;
	}
}
