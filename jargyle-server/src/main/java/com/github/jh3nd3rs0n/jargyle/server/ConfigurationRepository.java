package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.server.internal.configrepo.impl.FileSourceConfigurationRepository;

import java.io.File;

public abstract class ConfigurationRepository {

	public static ConfigurationRepository newFileSourceInstance(
			final File file) {
		return FileSourceConfigurationRepository.newInstance(file);
	}
	
	public abstract Configuration get();
	
	public abstract void set(final Configuration config);
	
}
