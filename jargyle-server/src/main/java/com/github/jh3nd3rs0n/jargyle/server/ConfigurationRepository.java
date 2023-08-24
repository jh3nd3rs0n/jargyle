package com.github.jh3nd3rs0n.jargyle.server;

public abstract class ConfigurationRepository {
	
	public abstract Configuration get();
	
	public abstract void set(final Configuration config);
	
}
