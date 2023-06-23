package com.github.jh3nd3rs0n.jargyle.server;

import com.github.jh3nd3rs0n.jargyle.server.internal.config.impl.ModifiableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.internal.config.impl.UnmodifiableConfiguration;
import com.github.jh3nd3rs0n.jargyle.server.internal.config.impl.UpdatedConfiguration;

public abstract class Configuration {
	
	public static Configuration newModifiableInstance() {
		return ModifiableConfiguration.newInstance();
	}
	
	public static Configuration newModifiableInstance(
			final Configuration config) {
		return ModifiableConfiguration.newInstance(config);
	}
	
	public static Configuration newModifiableInstance(
			final Settings settings) {
		return ModifiableConfiguration.newInstance(settings); 
	}
	
	public static Configuration newUnmodifiableInstance(
			final Configuration config) {
		return UnmodifiableConfiguration.newInstance(config);
	}
	
	public static Configuration newUnmodifiableInstance(
			final Settings settings) {
		return UnmodifiableConfiguration.newInstance(settings);
	}
	
	public static Configuration newUpdatedInstance(
			final ConfigurationRepository repository) {
		return UpdatedConfiguration.newInstance(repository);
	}
	
	public abstract void addSetting(
			final int index, final Setting<? extends Object> sttng);
	
	public abstract void addSetting(final Setting<? extends Object> sttng);
	
	public abstract void addSettings(final int index, final Settings sttngs);
	
	public abstract void addSettings(final Settings sttngs);
	
	public abstract int getSettingCount();
	
	public abstract Settings getSettings();

	public abstract Setting<Object> removeSetting(final int index);
	
}