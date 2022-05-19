package com.github.jh3nd3rs0n.jargyle.server;

import java.io.File;

public abstract class ConfigurationRepository {
	
	public static ConfigurationRepository newInstance(final File file) {
		return XmlFileSourceConfigurationRepository.newInstance(file);
	}
	
	public abstract Configuration get();
	
	public abstract void set(final Configuration config);
	
}
