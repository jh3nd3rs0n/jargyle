package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;

import com.github.jh3nd3rs0n.jargyle.server.configrepo.impl.XmlFileSourceConfigurationRepository;

public abstract class ConfigurationRepository {

	public static ConfigurationRepository newFileSourceInstance(
			final File file) {
		return XmlFileSourceConfigurationRepository.newInstance(file);
	}
	
	public abstract Configuration get();
	
	public abstract void set(final Configuration config);
	
}
